package org.prisma.zenhubetl.dto

import groovy.transform.ToString

@ToString
class GithubIssue {
	def id
	def number
	def title
	List<GithubLabel> labels
	def state
	def locked
	def assignee
	def assignees
	GithubMilestone milestone
	def comments
	def created_at
	def updated_at
	def closed_at
	def author_association
	def body
}