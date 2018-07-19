package util

class ZenhubETLProperties {
	
	Properties properties;
	
	Properties getProperties() {
		if (properties) {
			return properties
		}
		
		properties = new Properties()
		File propertiesFile = new File('zenhub-etl.properties')
		propertiesFile.withInputStream {
			properties.load(it)
		}
		
		return properties
		
	}

}