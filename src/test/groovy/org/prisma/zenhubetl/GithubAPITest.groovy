package org.prisma.zenhubetl

import org.junit.Test
import org.prisma.zenhubetl.dto.GithubEndpoints
import org.prisma.zenhubetl.dto.GithubIssue
import org.prisma.zenhubetl.dto.GithubMilestone

class GithubAPITest {
	
	def owner = 'utelemaco'
	def repository = 'prisma-sandbox-project'
	def zenhubRepoId = '141082272'
	
	//@Test
	public void getEndpoints() {
		GithubAPI api = new GithubAPI()
		GithubEndpoints endpoints =  api.endpoints
		assert endpoints
	}
	
	//@Test
	public void getMilestones() {
		GithubAPI api = new GithubAPI()
		List<GithubMilestone> milestones = api.getMilestones(owner, repository)
		assert milestones
	}
	
	//@Test
	public void getIssues() {
		GithubAPI api = new GithubAPI()
		List<GithubMilestone> issues = api.getIssues(owner, repository)
		assert issues
	}
	
	//@Test
	public void getIssuesWithComments() {
		GithubAPI api = new GithubAPI()
		List<GithubIssue> issues = api.getIssuesWithComments(owner, repository)
		assert issues
		issues.each { GithubIssue issue ->
			println "issue: ${issue.number}"
			println "   comments: ${issue.comments}"
			issue.listOfComments.each {
				println it
			}
		}
	}

}
