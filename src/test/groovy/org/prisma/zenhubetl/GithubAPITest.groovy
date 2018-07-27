package org.prisma.zenhubetl

import org.junit.Test

class GithubAPITest {
	
	@Test
	public void getEndpoints() {
		GithubAPI api = new GithubAPI()
		GithubEndpoints endpoints =  api.endpoints
		assert endpoints
	}

}
