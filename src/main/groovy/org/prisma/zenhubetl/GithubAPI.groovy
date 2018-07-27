package org.prisma.zenhubetl

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class GithubAPI {
	
	private static final String githubapi = 'https://api.github.com/'
	
	public GithubEndpoints getEndpoints() {
		def url = githubapi
		RestTemplate restTemplate = new RestTemplate()
		def request = new HttpEntity<String>()
		def response = restTemplate.exchange(url, HttpMethod.GET, request, GithubEndpoints.class)
		
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
