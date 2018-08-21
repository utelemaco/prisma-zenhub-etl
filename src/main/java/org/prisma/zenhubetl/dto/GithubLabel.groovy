package org.prisma.zenhubetl.dto

import groovy.transform.ToString

@ToString
class GithubLabel {
	def id
	String name
	String color
}
