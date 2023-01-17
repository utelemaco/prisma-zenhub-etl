package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.*

class GithubAPI extends AbstractAPI {
	
	private static final String githubapi = 'https://api.github.com'
	private String accessToken
	
	public GithubAPI() {
	}
	
	public GithubAPI(String accessToken) {
		this.accessToken = accessToken;
	}

	public GithubEndpoints getEndpoints() {
		def url = "${githubapi}"
		callExternalAPI(url, GithubEndpoints.class, this.accessToken)
	}
	
	public List<GithubMilestone> getMilestones(String owner, String repository) {
		def url = "${githubapi}/repos/${owner}/${repository}/milestones"
		callExternalAPI(url, GithubMilestone[].class, this.accessToken)
	}
	
	public List<GithubIssue> getIssues(String owner, String repository) {
		List<GithubIssue> allIssues = []
		def stop = false
		def page = 1
		while (!stop) {
			def url = "${githubapi}/repos/${owner}/${repository}/issues?page=${page}&state=all"
			def issues = callExternalAPI(url, GithubIssue[].class, this.accessToken)
			allIssues += issues.toList()
			if (!issues) {
				stop = true
			}
			page++
		}
		
		return allIssues
		
	}
	
	public List<GithubComment> getComments(String owner, String repository, def issueId) {
		def url = "${githubapi}/repos/${owner}/${repository}/issues/${issueId}/comments"
		callExternalAPI(url, GithubComment[].class, this.accessToken)
	}

	public List<GithubTimeline> getTimeline(String owner, String repository, def issueId) {
		def url = "${githubapi}/repos/${owner}/${repository}/issues/${issueId}/timeline"
		callExternalAPI(url, GithubTimeline[].class, this.accessToken)
	}
	
	public List<GithubIssue> getIssuesWithComments(String owner, String repository) {
		List<GithubIssue> issues = getIssues(owner, repository)
		
		issues.each { issue ->
			if (issue.comments) {
				issue.listOfComments += getComments(owner, repository, issue.number)
			}
		}
		
		return issues;
	}
	
}
