package org.prisma.zenhubetl

import org.prisma.domain.projectInstance.ProjectInstance

class Zenhub2ProjectInstance {
	
	public ProjectInstance loadFromZenhub(String repositoryName) {
		ProjectInstance project = new ProjectInstance()
		return project
	}

}
