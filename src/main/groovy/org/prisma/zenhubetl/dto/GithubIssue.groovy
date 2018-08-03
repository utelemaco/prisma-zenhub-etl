package org.prisma.zenhubetl.dto

import groovy.transform.ToString

@ToString
class GithubIssue {
	def id
	String number
	String title
	List<GithubLabel> labels
	String state
	def locked
	def assignee
	def assignees
	GithubMilestone milestone
	Integer comments
	List<GithubComment> listOfComments = []
	def created_at
	def updated_at
	def closed_at
	def author_association
	def body
	
}