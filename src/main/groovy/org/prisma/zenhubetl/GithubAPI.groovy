package org.prisma.zenhubetl

class GithubAPI {
	
	public GithubEndpoints getEndpoints() {
		
		
//		def headers = setHeaders(projectRedmine.redmineConfiguration)
//		def url = urlProject(projectRedmine.redmineConfiguration.urlBase, projectRedmine.redmineProjectId)
//		RestTemplate redmineRest = buildRestTemplate(projectRedmine.redmineConfiguration)
//		def request = new HttpEntity<String>(headers)
//		def response = redmineRest.exchange(url, HttpMethod.GET, request, RedmineProjectResponse.class)
//		
//		if (response.statusCode != HttpStatus.OK) {
//			throw new Exception("Couldn't get project ${projectRedmine.redmineProjectId} in Redmine")
//		}
//		return response.body.project
	}
	
//	HttpHeaders setHeaders(RedmineConfiguration redmineConfiguration) {
//		def headers = new HttpHeaders()
//		headers.set("X-Redmine-API-Key", redmineConfiguration.accessKey)
//		headers.setContentType(MediaType.APPLICATION_JSON)
//		
//		return headers
//	}

}
