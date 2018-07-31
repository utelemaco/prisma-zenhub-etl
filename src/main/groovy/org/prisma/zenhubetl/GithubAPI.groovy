package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class GithubAPI {
	
	private static final String githubapi = 'https://api.github.com/'
	
	public GithubEndpoints getEndpoints() {
		def url = githubapi
		RestTemplate restTemplate = new RestTemplate()
		def response = restTemplate.getForEntity(url, GithubEndpoints.class)
		
		if (response.statusCode != HttpStatus.OK) {
			throw new Exception("Error accessing github api.")
		}
		return response.body
	}
	
	public List<GithubMilestone> getMilestones(String owner, String repository) {
		def url = "${githubapi}/repos/${owner}/${repository}/milestones"
		RestTemplate restTemplate = new RestTemplate()
		def response = restTemplate.getForEntity(url, GithubMilestone[].class)
		
		if (response.statusCode != HttpStatus.OK) {
			throw new Exception("Error accessing github api.")
		}
		return response.body
	}
	
	public List<GithubIssue> getIssues(String owner, String repository) {
		def url = "${githubapi}/repos/${owner}/${repository}/issues"
		RestTemplate restTemplate = new RestTemplate()
		def response = restTemplate.getForEntity(url, GithubIssue[].class)
		
		if (response.statusCode != HttpStatus.OK) {
			throw new Exception("Error accessing github api.")
		}
		return response.body
	}
	
//	HttpHeaders setHeaders(RedmineConfiguration redmineConfiguration) {
//		def headers = new HttpHeaders()
//		headers.set("X-Redmine-API-Key", redmineConfiguration.accessKey)
//		headers.setContentType(MediaType.APPLICATION_JSON)
//		
//		return headers
//	}

}
