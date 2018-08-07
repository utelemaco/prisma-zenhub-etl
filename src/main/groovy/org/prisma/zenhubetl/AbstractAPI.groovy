package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.GithubEndpoints
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate

class AbstractAPI {
	
	def callExternalAPI(def url, Class responseClass) {
		println "requesting $url"
		RestTemplate restTemplate = new RestTemplate()
		def response = restTemplate.getForEntity(url, responseClass)
		
		if (response.statusCode != HttpStatus.OK) {
			throw new Exception("Error accessing external api.")
		}
		return response.body
	}

}
