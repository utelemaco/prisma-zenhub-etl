package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.*
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class GithubAPI extends AbstractAPI {
	
	private static final String githubapi = 'https://api.github.com'
	private static final String accessToken = '?access_token=f6579c0741820fc120f8d48992e0dd094e957d80'
	
	public GithubEndpoints getEndpoints() {
		def url = "${githubapi}${accessToken}"
		callExternalAPI(url, GithubEndpoints.class)
	}
	
	public List<GithubMilestone> getMilestones(String owner, String repository) {
		def url = "${githubapi}/repos/${owner}/${repository}/milestones${accessToken}"
		callExternalAPI(url, GithubMilestone[].class)
	}
	
	public List<GithubIssue> getIssues(String owner, String repository) {
		def url = "${githubapi}/repos/${owner}/${repository}/issues${accessToken}"
		callExternalAPI(url, GithubIssue[].class)
	}
	
	public List<GithubComment> getComments(String owner, String repository, def issueId) {
		def url = "${githubapi}/repos/${owner}/${repository}/issues/${issueId}/comments${accessToken}"
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
	
	
//	HttpHeaders setHeaders(RedmineConfiguration redmineConfiguration) {
//		def headers = new HttpHeaders()
//		headers.set("X-Redmine-API-Key", redmineConfiguration.accessKey)
//		headers.setContentType(MediaType.APPLICATION_JSON)
//		
//		return headers
//	}

}
