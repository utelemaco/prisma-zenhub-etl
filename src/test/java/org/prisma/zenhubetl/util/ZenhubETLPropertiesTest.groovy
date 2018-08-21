package org.prisma.zenhubetl.util

import org.junit.Test

import util.ZenhubETLProperties

class ZenhubETLPropertiesTest {//extends GroovyTestCase {
	
	@Test
	public void getProperties () {
		Properties props = (new ZenhubETLProperties()).properties
		assert props
		assert 'https://api.github.com/' == props['github.api']
		
	}

}
