package org.prisma.zenhubetl

import org.prisma.kip.domain.projectInstance.Project

class Zenhub2ProjectInstance {
	
	public Project loadFromZenhub(String repositoryName) {
		Project project = new Project()
		return project
	}

}
