package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.ZenhubBoard
import org.prisma.zenhubetl.dto.ZenhubDependencies
import org.prisma.zenhubetl.dto.ZenhubEpic
import org.prisma.zenhubetl.dto.ZenhubIssue
import org.prisma.zenhubetl.dto.ZenhubMilestoneStartDate

class ZenhubAPI extends AbstractAPI {
	
	private String accessToken
	
	public ZenhubAPI(String accessToken) {
		this.accessToken = accessToken;
	}

	private appendAccessToken() {
		if (accessToken) {
			return "?access_token=${accessToken}"
		}
		''
	}

	private static final String zenhubapi = 'https://api.zenhub.io'

	public ZenhubIssue getZenhubIssue(String repoId, def issueNumber) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/issues/${issueNumber}${appendAccessToken()}"
		callExternalAPI(url, ZenhubIssue.class)
	}
	
	public ZenhubBoard getZenhubBoard(String repoId) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/board${appendAccessToken()}"
		callExternalAPI(url, ZenhubBoard.class)
	}
	
	public ZenhubEpic getZenhubEpic(String repoId, def issueNumber) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/epics/${issueNumber}${appendAccessToken()}"
		callExternalAPI(url, ZenhubEpic.class)
	}
	
	
	public ZenhubDependencies getZenhubDependencies(String repoId) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/dependencies${appendAccessToken()}"
		callExternalAPI(url, ZenhubDependencies.class)
	}

	public ZenhubMilestoneStartDate getZenhubMilestoneStartDate(String repoId, String milestoneNumber) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/milestones/${milestoneNumber}/start_date${appendAccessToken()}"
		callExternalAPI(url, ZenhubMilestoneStartDate.class)
	}
}
