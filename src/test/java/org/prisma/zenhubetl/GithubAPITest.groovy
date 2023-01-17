package org.prisma.zenhubetl

import org.junit.Before
import org.junit.Test
import org.prisma.zenhubetl.dto.GithubEndpoints
import org.prisma.zenhubetl.dto.GithubIssue
import org.prisma.zenhubetl.dto.GithubMilestone
import org.prisma.zenhubetl.dto.GithubTimeline

class GithubAPITest {
	
	def owner = 'utelemaco'
	def repository = 'prisma-sandbox-project'
	def zenhubRepoId = '141082272'
	GithubAPI api = new GithubAPI()
	
	@Before
	public void prepareAPI() {
		String githubAccessToken = System.properties['githubAccessToken']
		if (githubAccessToken) {
			this.api = new GithubAPI(githubAccessToken)
		}
	}
	
	@Test
	public void getEndpoints() {
		GithubEndpoints endpoints =  api.endpoints
		assert endpoints
	}
	
	@Test
	public void getMilestones() {
		List<GithubMilestone> milestones = api.getMilestones(owner, repository)
		assert milestones
	}
	
	@Test
	public void getIssues() {
		List<GithubIssue> issues = api.getIssues(owner, repository)
		assert issues
	}
	
	@Test
	public void getIssuesWithComments() {
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

	@Test
	public void getTimeline() {
		List<GithubTimeline> timelines = api.getTimeline('mozilla', 'bedrock', '7046')
		assert timelines
	}

}
