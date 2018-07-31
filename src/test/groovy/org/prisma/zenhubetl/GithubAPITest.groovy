package org.prisma.zenhubetl

import org.junit.Test
import org.prisma.zenhubetl.dto.GithubEndpoints
import org.prisma.zenhubetl.dto.GithubMilestone

class GithubAPITest {
	
	def owner = 'utelemaco'
	def repository = 'prisma-sandbox-project'
	
	@Test
	public void getEndpoints() {
		GithubAPI api = new GithubAPI()
		GithubEndpoints endpoints =  api.endpoints
		assert endpoints
	}
	
	@Test
	public void getMilestones() {
		GithubAPI api = new GithubAPI()
		List<GithubMilestone> milestones = api.getMilestones(owner, repository)
		assert milestones
	}
	
	@Test
	public void getIssues() {
		GithubAPI api = new GithubAPI()
		List<GithubMilestone> issues = api.getIssues(owner, repository)
		assert issues
	}

}
