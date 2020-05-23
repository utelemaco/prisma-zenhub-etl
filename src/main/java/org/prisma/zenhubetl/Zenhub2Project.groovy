package org.prisma.zenhubetl

import org.prisma.kip.domain.processDefinition.Artifact
import org.prisma.kip.domain.projectInstance.BlockTask
import org.prisma.kip.domain.projectInstance.Document
import org.prisma.kip.domain.projectInstance.DocumentUsage
import org.prisma.kip.domain.projectInstance.Iteration
import org.prisma.kip.domain.projectInstance.Project
import org.prisma.kip.domain.projectInstance.Task
import org.prisma.kip.domain.projectInstance.TaskDependency
import org.prisma.kip.domain.projectInstance.UsageEnum
import org.prisma.zenhubetl.dto.GithubComment
import org.prisma.zenhubetl.dto.GithubIssue
import org.prisma.zenhubetl.dto.GithubMilestone
import org.prisma.zenhubetl.dto.ZenhubBoard
import org.prisma.zenhubetl.dto.ZenhubConfig
import org.prisma.zenhubetl.dto.ZenhubDependencies
import org.prisma.zenhubetl.dto.ZenhubEpic
import org.prisma.zenhubetl.dto.ZenhubIssue
import org.prisma.zenhubetl.mapper.GithubIssueMapper

class Zenhub2Project {
	
	GithubAPI githubAPI = new GithubAPI()
	GithubIssueMapper githubMapper = new GithubIssueMapper()
	
	ZenhubAPI zenhubAPI

	ZenhubConfig zenhubConfig
	
	public Zenhub2Project(ZenhubConfig zenhubConfig) {
		super();
		this.zenhubConfig = zenhubConfig;
		this.githubAPI = new GithubAPI(zenhubConfig.githubAccessToken)
		this.zenhubAPI = new ZenhubAPI(zenhubConfig.zenhubAccessToken)
	}

	public Project loadFromZenhub() {
		Project project = new Project()
		
		//List<GithubIssue> githubIssues = githubAPI.getIssuesWithComments(githubOwner, githubRepoName)
		List<GithubIssue> githubIssues = githubAPI.getIssues(zenhubConfig.githubOwner, zenhubConfig.githubRepoName)
		
		List<ZenhubIssue> zenhubIssues = loadZenhubIssuesFromGithubIssues(githubIssues)
		
		loadIterations(project, githubIssues)
		loadProcessInstances(project, githubIssues, zenhubIssues)
		
		loadTasks(project, githubIssues, zenhubIssues)
		
		linkProcessInstancesAndTasks(project)
		
		loadDocuments(project, githubIssues)
		
		loadDependencies(project)
		
		return project
	}
	
	List<ZenhubIssue> loadZenhubIssuesFromGithubIssues(List<GithubIssue> githubIssues) {
		List<ZenhubIssue> zenhubIssues = []
		ZenhubBoard zenhubBoard = zenhubAPI.getZenhubBoard(zenhubConfig.zenhubRepoId)
		zenhubBoard.pipelines.each { pipeline ->
			pipeline.issues.each { zenhubIssue ->
				zenhubIssue.pipeline = pipeline
				zenhubIssues << zenhubIssue
			}
		}
		
		return zenhubIssues
	}
	
	def loadIterations(Project project, List<GithubIssue> githubIssues) {
		Set<GithubMilestone> milestones = []
		
		githubIssues.each {
			if (it.milestone) {
				milestones << it.milestone
			}
		}
		
		milestones.each {
			project.iterations << githubMapper.githubMilestoneToIteration(it)
		}
	}
	
	def loadProcessInstances(Project project, List<GithubIssue> githubIssues, List<ZenhubIssue> zenhubIssues) {
		
		zenhubIssues.eachWithIndex { zenhubIssue, index ->
			if (zenhubIssue.is_epic) {
				GithubIssue githubIssue = githubIssues.find { it.number == zenhubIssue.issue_number }
				project.processInstances << githubMapper.githubIssueToProcessInstance(githubIssue, zenhubIssue)
				
				githubIssues.removeAll { it.number == githubIssue.number}
			}
			
		}
	}


	def loadTasks(Project project, List<GithubIssue> githubIssues, List<ZenhubIssue> zenhubIssues) {
		githubIssues.each { githubIssue ->
			ZenhubIssue zenhubIssue = zenhubIssues.find { it.issue_number == githubIssue.number }
			
			if (!zenhubIssue) {
				zenhubIssue = zenhubAPI.getZenhubIssue(zenhubConfig.zenhubRepoId, githubIssue.number)
			}
			
			Task task = githubMapper.githubIssueToTask(githubIssue, zenhubIssue, zenhubConfig)
			project.tasks << task
			
			if (githubIssue.milestone) {
				Iteration iteration = project.iterations.find { it.businessKey == githubIssue.milestone.number }
				iteration?.tasks << task
			}
		}
	}
	
	
	def linkProcessInstancesAndTasks(Project project) {
		project.processInstances.each { processInstance ->
			ZenhubEpic zenhubEpic = zenhubAPI.getZenhubEpic(zenhubConfig.zenhubRepoId, processInstance.businessKey)
			zenhubEpic.issues.each { zenhubIssue ->
				Task task = project.tasks.find { it.businessKey == zenhubIssue.issue_number }
				if (task) {
					processInstance.tasks << task
					task.processInstance = processInstance
				} else {
					println "Something weird: Task not found with businessKey: ${ zenhubIssue.issue_number }"
				}

			}
		}
	}
	
	
	def loadDocuments(Project project, List<GithubIssue> githubIssues) {
		loadStackOverFlowDocuments(project, githubIssues)
	}
	
	def loadStackOverFlowDocuments(Project project, List<GithubIssue> githubIssues) {
		githubIssues.each { GithubIssue githubIssue ->
			if (githubIssue.comments) {
				githubIssue.listOfComments.each { GithubComment comment ->
					comment.body.eachLine { String line ->
						def bodyAsTokens = line.split(' ')
						bodyAsTokens.each { String tokenRaw ->
							String token = tokenRaw.toLowerCase()
							if (token.startsWith('https://stackoverflow.com')) {
								createAStackOverFlowDocument(project, token, githubIssue)
							}
						}
					} 
				}
			}
			
		}
	}

	def createAStackOverFlowDocument(Project project, String token, GithubIssue githubIssue) {
		Artifact stackoverflowArtifact = project.artifacts.find { it.code == 'stackoverflow' }
		if (!stackoverflowArtifact) {
			stackoverflowArtifact = new Artifact(code: 'stackoverflow', name:'stackoverflow', description:'A stackoverflow post')
			project.artifacts << stackoverflowArtifact
		}
		Document document = project.documents.find { it.businessKey == token }
		if (!document) {
			document = new Document(businessKey: token, name: token)
			project.documents << document
		}
		
		stackoverflowArtifact.instancedDocuments << document
		document.implementedArtifacts << stackoverflowArtifact

		Task task = project.tasks.find { it.businessKey == githubIssue.number }
		DocumentUsage documentUsage = new DocumentUsage()
		documentUsage.task = task
		documentUsage.document = document
		documentUsage.usage = UsageEnum.ACCESS

		task.documentsUsage << documentUsage
		document.documentsUsage << documentUsage
	}
	
	def loadDependencies(Project project) {
		ZenhubDependencies zenhubDependencies = zenhubAPI.getZenhubDependencies(zenhubConfig.zenhubRepoId)
		
		zenhubDependencies.dependencies.each { zenhubDependency ->
			/**
			 * This version only support finish-to-start
			 */
			String blockType = 'finish-to-start'
			
			Task blockingTask = project.tasks.find { it.businessKey == zenhubDependency.blocking.issue_number }
			Task blockedTask = project.tasks.find { it.businessKey == zenhubDependency.blocked.issue_number }
			
			blockingTask.blockingTasks << new BlockTask(task: blockedTask, blockType: blockType)
			blockedTask.blockedByTasks << new BlockTask(task: blockingTask, blockType: blockType)
			
			project.taskDependencies << new TaskDependency(blockedTask: blockedTask, blockingTask: blockingTask, dependencyType: blockType)
		}
		
	}
	

}
