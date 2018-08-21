package org.prisma.zenhubetl

import org.junit.Before
import org.junit.Test
import org.prisma.zenhubetl.dto.ZenhubBoard
import org.prisma.zenhubetl.dto.ZenhubDependencies
import org.prisma.zenhubetl.dto.ZenhubEpic
import org.prisma.zenhubetl.dto.ZenhubIssue

class ZenhubAPITest {
	
	def repoId = '141082272'
	ZenhubAPI api
	
	@Before
	public void prepareAPI() {
		String zenhubAccessToken = System.properties['zenhubAccessToken']
		if (!zenhubAccessToken) {
			throw new Exception('System.property zenhubAccessToken not provided!')
		}
		this.api = new ZenhubAPI(zenhubAccessToken)
	}
	
	@Test
	public void getZenhubIssue() {
		ZenhubIssue zenhubIssue =  api.getZenhubIssue(repoId, 9)
		assert zenhubIssue
		assert zenhubIssue.estimate.value == 13
		assert zenhubIssue.pipeline.name == "New Issues"
		assert zenhubIssue.is_epic == false
	}
	
	@Test
	public void getZenhubPipelines() {
		ZenhubBoard zenhubBoard = api.getZenhubBoard(repoId)
		assert zenhubBoard
	}
	
	@Test
	public void getZenhubEpic() {
		ZenhubEpic zenhubEpic = api.getZenhubEpic(repoId, 8)
		assert zenhubEpic
	}
	
	@Test
	public void getZenhubDependencies() {
		ZenhubDependencies zenhubDependencies = api.getZenhubDependencies(repoId)
		assert zenhubDependencies
		assert zenhubDependencies.dependencies.size() == 2 
	}

}