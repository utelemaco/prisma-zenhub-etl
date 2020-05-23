package org.prisma.zenhubetl.dto

import groovy.transform.ToString

@ToString
class GithubTimeline {
	def id
	String event
	def created_at
}
