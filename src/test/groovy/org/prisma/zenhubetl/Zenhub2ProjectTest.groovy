package org.prisma.zenhubetl

import org.junit.Test
import org.prisma.kip.domain.projectInstance.Project

class Zenhub2ProjectTest {
	
	def owner = 'utelemaco'
	def repository = 'prisma-sandbox-project'
	
	@Test
	public void getEndpoints() {
		Zenhub2ProjectInstance service = new Zenhub2ProjectInstance()
		Project project = service.loadFromZenhub(owner, repository)
		assert project
	}

}
