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

	private static final String zenhubapi = 'https://api.zenhub.com'

	public ZenhubIssue getZenhubIssue(String repoId, def issueNumber) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/issues/${issueNumber}"
		callExternalAPI(url, ZenhubIssue.class, accessToken)
	}
	
	public ZenhubBoard getZenhubBoard(String repoId) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/board"
		callExternalAPI(url, ZenhubBoard.class, accessToken)
	}
	
	public ZenhubEpic getZenhubEpic(String repoId, def issueNumber) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/epics/${issueNumber}"
		callExternalAPI(url, ZenhubEpic.class, accessToken)
	}
	
	public ZenhubDependencies getZenhubDependencies(String repoId) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/dependencies"
		callExternalAPI(url, ZenhubDependencies.class, accessToken)
	}

	public ZenhubMilestoneStartDate getZenhubMilestoneStartDate(String repoId, String milestoneNumber) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/milestones/${milestoneNumber}/start_date"
		callExternalAPI(url, ZenhubMilestoneStartDate.class, accessToken)
	}
}
