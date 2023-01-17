package org.prisma.zenhubetl

import org.prisma.zenhubetl.dto.GithubEndpoints
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class AbstractAPI {
	
	def callExternalAPI(def url, Class responseClass, String accessToken) {
		return callExternalAPI(url, responseClass, accessToken, 0);
	}

	def callExternalAPI(def url, Class responseClass, String accessToken, int numberOfAttempts) {
		println "[${numberOfAttempts}] requesting $url"
		try {
			RestTemplate restTemplate = new RestTemplate()
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept", "application/vnd.github.mockingbird-preview");
			headers.add("Authorization", accessToken);

			HttpEntity<String> httpEntity = new HttpEntity<>("body", headers);
			//def response = restTemplate.getForEntity(url, responseClass)
			def response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseClass)

			if (response.statusCode != HttpStatus.OK) {
				throw new Exception("Error accessing external api.")
			}
			return response.body
		} catch(Exception e) {
			if (numberOfAttempts > 10) {
				throw e;
			}
			println "[${numberOfAttempts}] error $e"
			println "  Trying again in 60 seconds..."
			sleep(10000)
			println "  Trying again in 50 seconds..."
			sleep(10000)
			println "  Trying again in 40 seconds..."
			sleep(10000)
			println "  Trying again in 30 seconds..."
			sleep(10000)
			println "  Trying again in 20 seconds..."
			sleep(10000)
			println "  Trying again in 10 seconds..."
			sleep(10000)
			return callExternalAPI(url, responseClass, ++numberOfAttempts)
		}
	}

}
