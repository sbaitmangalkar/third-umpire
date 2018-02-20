package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class Extractor {
	/**
	 * 
	 * @return
	 */
	private Properties getApplicationProperties() {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getResourceAsStream(ServiceConstants.APP_PROPERTIES);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, String> extractPolingStationDetails() {
		String defaultYear = ServiceConstants.DEFAULT_SEARCH_YEAR;
		String defaultAcNo = ServiceConstants.DEFAULT_SEARCH_AC_NO;
		
		return extractPolingStationDetails(defaultYear, defaultAcNo);
	}

	/**
	 * Returns a key,value store containing acNo 
	 * @return
	 */
	public Map<String, String> extractPolingStationDetails(String year, String acNo) {
		String url = getApplicationProperties().getProperty("TEST_URL");
		
		
		url = url + acNo;
		url = url.replace("%d", year);
		
		Map<String, String> partNoAndAnchors = null;
		
		WebClient webClient = new WebClient();
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.waitForBackgroundJavaScript(1000);

		try {
			HtmlPage page = webClient.getPage(url);
			List<HtmlAnchor> allAnchors = page.getAnchors();
			
			/*
			 * Extracting poling booth names 
			 */
			List<String> polingStationsEnglish = IntStream.range(0, allAnchors.size())
					                                          .filter(index -> index % 2 == 0)
					                                          .mapToObj(index -> allAnchors.get(index).asText())
					                                          .collect(Collectors.toList());
			
						
			/*
			 * Creating a <key,value> store containing the ACNO and part number
			 * as key and poling booth location as value 
			 */
			partNoAndAnchors = IntStream.range(1, polingStationsEnglish.size())
			                            .boxed()
			                            .collect(Collectors.toMap(index -> acNo + "|" + String.format("%03d", index), 
			                            		index -> polingStationsEnglish.get(index)));
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		
		return partNoAndAnchors;

	}
}
