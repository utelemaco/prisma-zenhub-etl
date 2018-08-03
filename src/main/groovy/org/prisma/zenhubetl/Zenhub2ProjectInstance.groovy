package org.prisma.zenhubetl

import org.prisma.kip.domain.processDefinition.Artifact
import org.prisma.kip.domain.projectInstance.Document
import org.prisma.kip.domain.projectInstance.DocumentUsage
import org.prisma.kip.domain.projectInstance.Iteration
import org.prisma.kip.domain.projectInstance.Project
import org.prisma.kip.domain.projectInstance.Task
import org.prisma.kip.domain.projectInstance.UsageEnum
import org.prisma.zenhubetl.dto.GithubComment
import org.prisma.zenhubetl.dto.GithubIssue
import org.prisma.zenhubetl.dto.GithubMilestone
import org.prisma.zenhubetl.dto.ZenhubIssue
import org.prisma.zenhubetl.mapper.GithubIssueMapper

class Zenhub2ProjectInstance {
	
	GithubAPI githubAPI = new GithubAPI()
	GithubIssueMapper issueMapper = new GithubIssueMapper()
	
	ZenhubAPI zenhubAPI = new ZenhubAPI()
	
	public Project loadFromZenhub(String githubOwner, String githubRepoName, String zenhubRepoId) {
		Project project = new Project()
		
		List<GithubIssue> githubIssues = githubAPI.getIssuesWithComments(githubOwner, githubRepoName)
		List<ZenhubIssue> zenhubIssues = loadZenhubIssuesFromGithubIssues(zenhubRepoId, githubIssues)
		
		loadIterations(project, githubIssues)
		loadTasks(project, githubIssues, zenhubIssues)
		loadDocuments(project, githubIssues)
		
		return project
	}
	
	List<ZenhubIssue> loadZenhubIssuesFromGithubIssues(String zenhubRepoId, List<GithubIssue> githubIssues) {
		List<ZenhubIssue> zenhubIssues = []
		githubIssues.each { githubIssue ->
			zenhubIssues << zenhubAPI.getZenhubIssue(zenhubRepoId, githubIssue.number)
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
			project.iterations << issueMapper.githubMilestoneToIteration(it)
		}
	}


	def loadTasks(Project project, List<GithubIssue> githubIssues, List<ZenhubIssue> zenhubIssues) {
		githubIssues.each { githubIssue ->
			ZenhubIssue zenhubIssue = zenhubIssues.find { it.number == githubIssue.number }
			Task task = issueMapper.githubIssueToTask(githubIssue, zenhubIssue)
			project.tasks << task
			
			if (githubIssue.milestone) {
				Iteration iteration = project.iterations.find { it.businessKey == githubIssue.milestone.number }
				iteration?.tasks << task
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
	

}
