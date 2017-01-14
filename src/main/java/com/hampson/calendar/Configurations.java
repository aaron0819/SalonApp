package com.hampson.calendar;

import org.apache.commons.configuration.XMLPropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import java.util.Scanner;
import java.io.File;

public class Configurations {
	
	private static final XMLPropertiesConfiguration configs = getXMLConfigurations();

	public static final String CLIENT_ID = getProperty("clientId", configs);
	public static final String CLIENT_SECRET = getProperty("clientSecret", configs);
	public static final String REDIRECT_URI = getProperty("redirectUri", configs);
	public static final String APP_NAME = getProperty("appName", configs);

	private static String getProperty(String name, XMLPropertiesConfiguration configs) {
		if (configs.containsKey(name))
			return (String) configs.getProperty(name);
		else {
			Scanner in = new Scanner(System.in);
			String value = in.nextLine();
			configs.addProperty(name, value);
			try {
				configs.save();
			} catch (ConfigurationException ce) {
				throw new RuntimeException("Could not save configuration: " + name);
			} finally {
				in.close();
			}
			return value;
		}
	}
	
	private static XMLPropertiesConfiguration getXMLConfigurations() {
		try {
			return new XMLPropertiesConfiguration(new File("config.xml"));
		} catch (ConfigurationException ce) {
			throw new RuntimeException("Error initializing configuration file");
		}
	}
}
