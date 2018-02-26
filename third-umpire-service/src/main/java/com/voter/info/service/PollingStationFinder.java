package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class PollingStationFinder {
	private static Properties getProperties() {
		Properties properties = new Properties();
		InputStream inputStream = PollingStationFinder.class.getResourceAsStream(ServiceConstants.APP_PROPERTIES);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	public static List<String> findAllPollingStationsURL(String assemblyConstituenceURL) {
		WebClient client = new WebClient();

		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(1000);
		
		String draftRollURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.HISTORY_SEARCH_YEAR);
		
		try {
			HtmlPage pollingStationPage = client.getPage(assemblyConstituenceURL);
			List<HtmlAnchor> allPageAnchors = pollingStationPage.getAnchors();
			List<String> pollingBoothURLs = allPageAnchors.stream()
			                                              .filter(eachAnchor -> eachAnchor.getHrefAttribute().contains("English"))
			                                              .map(eachAnchor -> draftRollURL + "/" + eachAnchor.getHrefAttribute())
			                                              .collect(Collectors.toList());
			pollingBoothURLs.forEach(System.out::println);
			
			return pollingBoothURLs;
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		findAllPollingStationsURL("http://ceokarnataka.kar.nic.in/DraftRolls_2016/Part_List.aspx?ACNO=156");
	}
}
