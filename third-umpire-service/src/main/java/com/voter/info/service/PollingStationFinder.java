package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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
		List<String> pollingBoothURLs = null;
		WebClient client = new WebClient();

		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(1000);
		
		//String draftRollURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.HISTORY_SEARCH_YEAR);
		String draftRollURL = getProperties().getProperty("FINAL_ROLL_URL").replace("%d", ServiceConstants.RECENT_SEARCH_YEAR);
		
		try {
			HtmlPage pollingStationPage = client.getPage(assemblyConstituenceURL);
			List<HtmlAnchor> allPageAnchors = pollingStationPage.getAnchors();
			if(draftRollURL.contains("_2016")) {
				pollingBoothURLs = allPageAnchors.stream()
                                                 .filter(eachAnchor -> eachAnchor.getHrefAttribute().contains("English"))
                                                 .map(eachAnchor -> draftRollURL + "/" + eachAnchor.getHrefAttribute())
                                                 .collect(Collectors.toList());
			} else {
				pollingBoothURLs = allPageAnchors.stream()
                                                 .filter(eachAnchor -> eachAnchor.getHrefAttribute().contains("English"))
                                                 .map(eachAnchor -> eachAnchor.getHrefAttribute().replace("%2f", "/"))
                                                 .map(eachAnchor -> StringUtils.substringAfter(eachAnchor, "=."))
                                                 .map(eachAnchor -> draftRollURL + eachAnchor)
                                                 .collect(Collectors.toList());
			}
			//pollingBoothURLs.forEach(System.out::println);
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			System.out.println("PollingStationFinder::findAllPollingStationsURL(String assemblyConstituenceURL)");
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			if(client != null) {
				client.closeAllWindows();
				client = null;
			}
		}
		return pollingBoothURLs;
	}
	
	public static void main(String[] args) {
		//System.out.println(findAllPollingStationsURL("http://ceokarnataka.kar.nic.in/DraftRolls_2016/Part_List.aspx?ACNO=156"));
		System.out.println(findAllPollingStationsURL("http://ceokarnataka.kar.nic.in/FinalRoll-2017/Part_List.aspx?ACNO=156"));
	}
}
