package org.prisma.zenhubetl.mapper

import org.prisma.kip.domain.processDefinition.Activity
import org.prisma.kip.domain.projectInstance.Iteration
import org.prisma.kip.domain.projectInstance.ProcessInstance
import org.prisma.kip.domain.projectInstance.Task
import org.prisma.kip.domain.util.Effort
import org.prisma.kip.domain.util.Priority
import org.prisma.zenhubetl.dto.MapEntry
import org.prisma.zenhubetl.dto.ZenhubConfig
import org.prisma.zenhubetl.dto.GithubIssue
import org.prisma.zenhubetl.dto.GithubLabel
import org.prisma.zenhubetl.dto.GithubMilestone
import org.prisma.zenhubetl.dto.ZenhubIssue
import org.prisma.zenhubetl.dto.ZenhubMilestoneStartDate

class GithubIssueMapper {
	
	Task githubIssueToTask(GithubIssue githubIssue, ZenhubIssue zenhubIssue, ZenhubConfig zenhubConfig) {
		Task task = new Task()
		
		task.id = githubIssue.id
		task.businessKey = githubIssue.number
		task.name = githubIssue.title
		task.description = githubIssue.body
		
		task.priority = loadIssuePrioriry(githubIssue, zenhubConfig)
		task.status = loadIssueStatus(zenhubIssue, zenhubConfig)
		task.effort = loadIssueEffort(zenhubIssue)
		task.implementedActivities = loadIssuesActivities(githubIssue, zenhubConfig)
		
		return task
	}
	
	ProcessInstance githubIssueToProcessInstance(GithubIssue githubIssue, ZenhubIssue zenhubIssue) {
		ProcessInstance processInstance = new ProcessInstance()
		processInstance.id = githubIssue.id
		processInstance.businessKey = githubIssue.number
		processInstance.name = githubIssue.title
		return processInstance
	}
	
	Priority loadIssuePrioriry(GithubIssue issue, ZenhubConfig zenhubConfig) {

		// When the parameter 'option' is null or the priority map is empty, do not try to extract priority
		if (zenhubConfig.prioritiesMap.isEmpty()) {
			return null
		}

		Priority priority = null

		for (GithubLabel label: issue.labels) {
			def priorityMapEntry = zenhubConfig.prioritiesMap.find { entry -> entry.key == label.name }


			if (!priorityMapEntry) {
				//entry not found in the map... continue to the next label.
				continue
			}

			//If an entry is found or if the current priority has a lower value
			if (!priority || priority.value < priorityMapEntry.value) {
				priority = new Priority(label: priorityMapEntry.key, value: Integer.valueOf(priorityMapEntry.value))
			}

		}

		return priority
	}
	
	def loadIssueStatus(ZenhubIssue zenhubIssue, ZenhubConfig zenhubConfig) {
		if (zenhubConfig.statusMap.isEmpty()) {
			return null
		}

		def statusMapEntry = zenhubConfig.statusMap.find { entry -> entry.key == zenhubIssue.pipeline?.name }

		if (!statusMapEntry) {
			//entry not found in the map... returning null
			return null
		}

		return statusMapEntry.value
	}

	List<Activity> loadIssuesActivities(GithubIssue githubIssue, ZenhubConfig zenhubConfig) {
		if (zenhubConfig.processActivitiesMap.isEmpty() || githubIssue.labels.isEmpty()) {
			return Collections.emptyList()
		}

		List<Activity> activities = []
		githubIssue.labels.each { label ->
			MapEntry processActivityMapEntry = zenhubConfig.processActivitiesMap.find { label.name == it.key }
			if (processActivityMapEntry) {
				activities << new Activity(name: processActivityMapEntry.value)
			}

		}

		return activities
	}
	
	Effort loadIssueEffort(ZenhubIssue zenhubIssue) {
		new Effort(estimated: zenhubIssue.estimate?.value)
	}
	
	Iteration githubMilestoneToIteration(GithubMilestone milestone, ZenhubMilestoneStartDate zenhubMilestoneStartDate) {
		Iteration iteration = new Iteration()
		
		iteration.id = milestone.id
		iteration.businessKey = milestone.number
		iteration.name = milestone.title
		iteration.description = milestone.description
		iteration.status = milestone.state
		if (zenhubMilestoneStartDate?.start_date) {
			iteration.startDate = Date.parse("yyyy-MM-dd'T'HH:mm:sss", zenhubMilestoneStartDate.start_date.replaceAll("Z", ""))
		}

		if (milestone.due_on) {
			iteration.finishDate = Date.parse("yyyy-MM-dd'T'HH:mm:ss", milestone.due_on.replaceAll("Z", ""))
		}
		
		return iteration
	}


}
