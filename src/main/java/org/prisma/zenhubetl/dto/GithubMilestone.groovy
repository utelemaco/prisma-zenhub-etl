package org.prisma.zenhubetl.dto

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode(includes=['number'])
class GithubMilestone {
	
	def id
	String number
	String title
	String description
	def open_issues
	def closed_issues
	String state
	def created_at
	def updated_at
	def due_on
	def closed_at

}
