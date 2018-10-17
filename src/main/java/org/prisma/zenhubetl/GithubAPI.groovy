package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.*
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class GithubAPI extends AbstractAPI {
	
	private static final String githubapi = 'https://api.github.com'
	private String accessToken
	
	public GithubAPI() {
	}
	
	public GithubAPI(String accessToken) {
		this.accessToken = accessToken;
	}
	
	private String appendAccessToken() {
		if (accessToken) {
			return "access_token=${accessToken}"
		}
		return ''
	}

	public GithubEndpoints getEndpoints() {
		def url = "${githubapi}?${appendAccessToken()}"
		callExternalAPI(url, GithubEndpoints.class)
	}
	
	public List<GithubMilestone> getMilestones(String owner, String repository) {
		def url = "${githubapi}/repos/${owner}/${repository}/milestones?${appendAccessToken()}"
		callExternalAPI(url, GithubMilestone[].class)
	}
	
	public List<GithubIssue> getIssues(String owner, String repository) {
		def url = "${githubapi}/repos/${owner}/${repository}/issues?state=all&${appendAccessToken()}"
		callExternalAPI(url, GithubIssue[].class)
	}
	
	public List<GithubComment> getComments(String owner, String repository, def issueId) {
		def url = "${githubapi}/repos/${owner}/${repository}/issues/${issueId}/comments?${appendAccessToken()}"
		callExternalAPI(url, GithubComment[].class)
	}
	
	public List<GithubIssue> getIssuesWithComments(String owner, String repository) {
		List<GithubIssue> issues = getIssues(owner, repository)
		
		issues.each { issue ->
			if (issue.comments) {
				issue.listOfComments << getComments(owner, repository, issue.number)
			}
		}
		
		return issues;
	}
	
}