package org.prisma.zenhubetl

import org.prisma.kip.domain.projectInstance.Project
import org.prisma.zenhubetl.dto.GithubIssue
import org.prisma.zenhubetl.mapper.GithubIssueMapper

class Zenhub2ProjectInstance {
	
	GithubAPI api = new GithubAPI()
	GithubIssueMapper issueMapper = new GithubIssueMapper()
	
	public Project loadFromZenhub(String owner, String repository) {
		Project project = new Project()
		GithubAPI api = new GithubAPI()
		List<GithubIssue> issues = api.getIssues(owner, repository)
		
		issues.each {
			project.tasks << issueMapper.githubIssueToTask(it) 
		}
		
		return project
	}

}
