package org.prisma.zenhubetl.dto

import groovy.transform.ToString

@ToString
class GithubComment {
	def id
	String body
	def created_at
}
