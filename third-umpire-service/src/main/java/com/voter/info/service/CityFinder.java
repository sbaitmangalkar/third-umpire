package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

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
		
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		
		try {
			String draftRoolURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", "2016");
			HtmlPage draftRoolPage = client.getPage(draftRoolURL);
			List<HtmlAnchor> anchors = draftRoolPage.getAnchors();
			anchors.forEach(e -> System.out.println(e.asXml()));
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		findAllCities();
	}
}
