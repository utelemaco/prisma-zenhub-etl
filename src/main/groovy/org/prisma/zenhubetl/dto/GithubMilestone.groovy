package org.prisma.zenhubetl.dto

import groovy.transform.ToString

@ToString
class GithubMilestone {
	
	def id
	def number
	def title
	def description
	def open_issues
	def closed_issues
	def state
	def created_at
	def updated_at
	def due_on
	def closed_at

}
