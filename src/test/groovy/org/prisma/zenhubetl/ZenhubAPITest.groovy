package org.prisma.zenhubetl

import org.junit.Test
import org.prisma.zenhubetl.dto.ZenhubBoard
import org.prisma.zenhubetl.dto.ZenhubIssue

class ZenhubAPITest {
	
	def repoId = '141082272'
	
	@Test
	public void getZenhubIssue() {
		ZenhubAPI api = new ZenhubAPI()
		ZenhubIssue zenhubIssue =  api.getZenhubIssue(repoId, 9)
		assert zenhubIssue
		assert zenhubIssue.estimate.value == 13
		assert zenhubIssue.pipeline.name == "New Issues"
		assert zenhubIssue.is_epic == false
	}
	
	@Test
	public void getZenhubPipelines() {
		ZenhubAPI api = new ZenhubAPI()
		ZenhubBoard zenhubBoard = api.getZenhubBoard(repoId)
		assert zenhubBoard
	}
	

	

}
