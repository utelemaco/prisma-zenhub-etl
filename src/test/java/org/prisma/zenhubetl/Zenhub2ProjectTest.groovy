package org.prisma.zenhubetl

import org.junit.Test
import org.prisma.kip.domain.projectInstance.Project
import org.prisma.zenhubetl.dto.MapEntry
import org.prisma.zenhubetl.dto.ZenhubConfig

class Zenhub2ProjectTest {
	
//	def githubOwner = 'gfrebello' 
//	def githubRepoName = 'qs-trip-planning-procedure' 
//	def zenhubRepoId = '147525741'

	
//	def githubOwner = 'mozilla'
//	def githubRepoName = 'bedrock'
//	def zenhubRepoId = '1616665'

//	def githubOwner = 'mozilla'
//	def githubRepoName = 'science.mozilla.org'
//	def zenhubRepoId = '23126617'

	def githubOwner = 'utelemaco'
	def githubRepoName = 'prisma-sandbox-project'
	def zenhubRepoId = '141082272'
	List<MapEntry> prioritiesMap = [new MapEntry(key:'Priority:Very High', value: "100"), new MapEntry(key:'Priority:High', value: "80"), new MapEntry(key:'Priority:Normal', value: "50"), new MapEntry(key:'Priority:Low', value: "20")]
	List<MapEntry> statusMap = [new MapEntry(key:'New Issues', value: "todo"), new MapEntry(key:'Backlog', value: "todo"), new MapEntry(key:'In Progress', value: "doing"), new MapEntry(key:'Review/QA', value: "doing"), new MapEntry(key:'Done', value: "done")]

//	def githubOwner = 'flutter'
//	def githubRepoName = 'flutter'
//	def zenhubRepoId = '31792824'



	
	
	@Test
	public void loadFromZenhub() {

		ZenhubConfig config = new ZenhubConfig()
		config.githubOwner = githubOwner
		config.githubRepoName = githubRepoName
		config.zenhubRepoId = zenhubRepoId
		config.prioritiesMap = prioritiesMap
		config.statusMap = statusMap

		config.githubAccessToken = System.properties['githubAccessToken']
		if (!config.githubAccessToken) {
			throw new Exception('System.property githubAccessToken not provided!')
		}

		config.zenhubAccessToken = System.properties['zenhubAccessToken']
		if (!config.zenhubAccessToken) {
			throw new Exception('System.property zenhubAccessToken not provided!')
		}

		Zenhub2Project service = new Zenhub2Project(config)
		Project project = service.loadFromZenhub()
		assert project
		
		assert project.processInstances
		assert project.processInstances.size() == 2
		
		//assert project.processInstances[0].tasks
		
		assert project.tasks
		assert project.tasks.size() == 14
		
		assert project.iterations
		assert project.iterations.size() == 2
		assert project.iterations[0].businessKey == '2'
		
		assert project.iterations[0].tasks
		assert project.iterations[0].tasks.size() == 3
		
		assert project.iterations[1].tasks
		assert project.iterations[1].tasks.size() == 5
		
		
		assert project.documents
		
		
		println "\n\n####################################\nIteration Perspective:"
		project.iterations.eachWithIndex { iteration, index ->
			println "     Iteration[${index+1}: ${iteration.name}]"
			println "       Tasks: "
			iteration.tasks.each { println "          ${it.businessKey} - ${it.name}"}
		}
		
		println "\n\n####################################\nProcess Instances Perspective:"
		project.processInstances.eachWithIndex { processInstance, index ->
			println "     Process Instance[${index+1}: ${processInstance.name}]"
			println "       Tasks: "
			processInstance.tasks.each { println "          ${it.businessKey} - ${it.name}"}
		}
					
		println "\n\n####################################\nProject Perspective:"
		project.tasks.each { println "          ${it.businessKey} - ${it.name}"}
		
		
		
		println '\n\nAbout Documents...'
		project.documents.each { doc ->
			println "   Documents: ${doc.businessKey}"
			
			doc.documentsUsage.each {
				println "   accessed by task: ${it.task.businessKey}"
			}
		}
		println '\n\nAbout Tasks...'
		project.tasks.each { task ->
			if (task.documentsUsage) {
				println "       Task: ${task.businessKey}"
				task.documentsUsage.each {
					println "      accessing doc: ${it.document.businessKey}"
				}
			}
		}
		
		println '\n\n\nAbout dependencies....'
		project.tasks.each { task ->
			if (task.blockingTasks) {
				task.blockingTasks.each { blockTask ->
				    println "\n -     #${task.businessKey} '${task.name}' is blocking #${blockTask.task.businessKey} '${blockTask.task.name}'"
				}
			}
			
			if (task.blockedByTasks) {
				task.blockedByTasks.each { blockTask ->
					println "\n -     #${task.businessKey} '${task.name}' is blocked by #${blockTask.task.businessKey} '${blockTask.task.name}'"
				}
			}
		}
		
		
		project.taskDependencies.each { taskDependency ->
			println "\n @     #${taskDependency.blockingTask.businessKey} '${taskDependency.blockingTask.name}' is blocking #${taskDependency.blockedTask.businessKey} '${taskDependency.blockedTask.name}'"
		}
		
	}

}
