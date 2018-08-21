package org.prisma.zenhubetl

import static org.junit.Assert.*

import org.junit.Test
import org.prisma.zenhubetl.dto.GithubMilestone

class GithubMilestoneTest {

	@Test
	public void test() {
		
		GithubMilestone milestone1 = new GithubMilestone(number:'1')
		GithubMilestone milestone2 = new GithubMilestone(number:'1')
		GithubMilestone milestone3 = new GithubMilestone(number:'3')
		
		assert milestone1 == milestone2
		assert milestone1 != milestone3
		 
		
	}

}
