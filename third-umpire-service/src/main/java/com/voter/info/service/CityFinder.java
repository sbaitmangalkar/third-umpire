package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class CityFinder {
	private static Properties getProperties() {
		Properties properties = new Properties();
		InputStream inputStream = CityFinder.class.getResourceAsStream(ServiceConstants.APP_PROPERTIES);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static void findAllCities() {
		WebClient client = new WebClient();
		
	}
}
