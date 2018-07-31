package org.prisma.zenhubetl.mapper

import org.prisma.kip.domain.projectInstance.Task
import org.prisma.zenhubetl.dto.GithubIssue

class GithubIssueMapper {
	
	Task githubIssueToTask(GithubIssue issue) {
		Task task = new Task()
		
		task.id = issue.id
		task.name = issue.title
		task.description = issue.body
		task.status = issue.state
		
		return task
	}

}