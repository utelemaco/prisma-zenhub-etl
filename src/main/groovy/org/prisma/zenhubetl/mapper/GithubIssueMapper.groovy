package org.prisma.zenhubetl.mapper

import org.prisma.kip.domain.projectInstance.Iteration
import org.prisma.kip.domain.projectInstance.Task
import org.prisma.kip.domain.util.Effort
import org.prisma.kip.domain.util.Priority
import org.prisma.zenhubetl.dto.GithubIssue
import org.prisma.zenhubetl.dto.GithubLabel
import org.prisma.zenhubetl.dto.GithubMilestone
import org.prisma.zenhubetl.dto.ZenhubIssue

class GithubIssueMapper {
	
	private static final List<String> priorityValues = ['very low', 'low', 'normal', 'high', 'very high'] 
	
	Task githubIssueToTask(GithubIssue githubIssue, ZenhubIssue zenhubIssue) {
		Task task = new Task()
		
		task.id = githubIssue.id
		task.businessKey = githubIssue.number
		task.name = githubIssue.title
		task.description = githubIssue.body
		
		task.priority = loadIssuePrioriry(githubIssue)
		
		task.status = loadIssueStatus(zenhubIssue)
		task.effort = loadIssueEffort(zenhubIssue)
		
		return task
	}
	
	Priority loadIssuePrioriry(GithubIssue issue) {
		
		GithubLabel priorityLabel = issue.labels.find { it.name.toLowerCase().startsWith('priority') }
		if (!priorityLabel) {
			return null
		}
		
		String[] priorityLabelAsArray = priorityLabel.name.split(':')
		Priority priority = new Priority()
		
		/**
		 * If the Priority Label does not have a 'xxxx:xxxx' format.
		 */
		if (priorityLabelAsArray.length < 2) {
			priority.label = priorityLabelAsArray[0]
			priority.value = 0
			return priority
		}
		
		priority.label = priorityLabelAsArray[1]
		priority.value = priorityValues.indexOf(priority.label.toLowerCase())
		return priority
	}
	
	def loadIssueStatus(ZenhubIssue zenhubIssue) {
		return zenhubIssue.pipeline?.name
	}
	
	Effort loadIssueEffort(ZenhubIssue zenhubIssue) {
		new Effort(estimated: zenhubIssue.estimate?.value)
	}
	
	Iteration githubMilestoneToIteration(GithubMilestone milestone) {
		Iteration iteration = new Iteration()
		
		iteration.id = milestone.id
		iteration.businessKey = milestone.number
		iteration.name = milestone.title
		iteration.description = milestone.description
		iteration.status = milestone.state
		
		return iteration
	}

}