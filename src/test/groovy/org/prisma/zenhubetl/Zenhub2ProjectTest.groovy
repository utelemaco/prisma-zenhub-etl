package org.prisma.zenhubetl

import org.junit.Test
import org.prisma.kip.domain.projectInstance.Project

class Zenhub2ProjectTest {
	
	def githubOwner = 'utelemaco'
	def githubRepoName = 'prisma-sandbox-project'
	def zenhubRepoId = '141082272'
	
	@Test
	public void loadFromZenhub() {
		Zenhub2ProjectInstance service = new Zenhub2ProjectInstance()
		Project project = service.loadFromZenhub(githubOwner, githubRepoName, zenhubRepoId)
		assert project
		
		assert project.iterations
		assert project.iterations.size() == 2
		assert project.iterations[0].businessKey == '2'
		
		assert project.iterations[0].tasks
		assert project.iterations[0].tasks.size() == 3
		
		assert project.iterations[1].tasks
		assert project.iterations[1].tasks.size() == 5
		
		
		assert project.documents
		
		
		project.tasks.each {
			println "${it}"
		}
		
		
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
		
	}

}