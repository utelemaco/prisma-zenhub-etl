package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.ZenhubBoard
import org.prisma.zenhubetl.dto.ZenhubIssue

class ZenhubAPI extends AbstractAPI {
	
	
	private static final String zenhubapi = 'https://api.zenhub.io/'
	private static final String accessToken = '?access_token=5e9b1c914d2959a5feddd3f3335671d68570f8883bfa5fca774c78b3dc711e9321f3ab5b20d6c45e'

	public ZenhubIssue getZenhubIssue(String repoId, def issueNumber) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/issues/${issueNumber}${accessToken}"
		ZenhubIssue zenhubIssue = callExternalAPI(url, ZenhubIssue.class)
		zenhubIssue.number = issueNumber
		zenhubIssue
	}
	
	public ZenhubBoard getZenhubBoard(String repoId) {
		def url = "${zenhubapi}/p1/repositories/${repoId}/board${accessToken}"
		callExternalAPI(url, ZenhubBoard.class)
	}
}