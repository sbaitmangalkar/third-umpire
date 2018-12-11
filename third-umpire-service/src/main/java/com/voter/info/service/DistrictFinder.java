package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Finds districts and their specific URLs
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class DistrictFinder {
	private static Map<String, String> allDistrictDetails;
	
	private static Properties getProperties() {
		Properties properties = new Properties();
		InputStream inputStream = DistrictFinder.class.getResourceAsStream(ServiceConstants.APP_PROPERTIES);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * Builds <code>allDistrictDetails</code> when called.
	 * 
	 * @return
	 */
	private static Map<String, String> findAllDistricts() {
		WebClient client = new WebClient();
		
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(1000);
		
		try {
			String draftRollURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.RECENT_SEARCH_YEAR);
			//String draftRollURL = getProperties().getProperty("FINAL_ROLL_URL").replace("%d", ServiceConstants.RECENT_SEARCH_YEAR);
			HtmlPage draftRollPage = client.getPage(draftRollURL);
			List<HtmlAnchor> anchors = draftRollPage.getAnchors();
			
			Map<String, String> districtURLs = anchors.stream()
					                     .filter(anchor -> !anchor.asText().trim().contains("/ BANGALORE"))
					                     .collect(Collectors.toMap(anchor -> formatDistrictName(anchor.asText()), 
					                    		 anchor -> draftRollURL + "/" + anchor.getHrefAttribute()));
			
			
			
			HtmlAnchor bangaloreDistrictsAnchor = anchors.stream()
			                                   .filter(anchor -> anchor.asText().trim().contains("/ BANGALORE"))
			                                   .findFirst()
			                                   .get();
			
			Map<String, String> bangaloreDistrictURLs = getDistrictURLsForBangalore(bangaloreDistrictsAnchor);
			
			
			Map<String, String> allDistrictDetails = Stream.of(districtURLs, bangaloreDistrictURLs)
					                                       .flatMap(map -> map.entrySet().stream())
					                                       .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			return allDistrictDetails;
			       
		} catch (FailingHttpStatusCodeException | IOException e) {
			System.out.println("DistrictFinder::findAllDistricts()");
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} finally {
			if(client != null) {
				client.closeAllWindows();
				client = null;
			}
		}
		return null;
	}
	
	/**
	 * Returns URL of a given district.
	 * 
	 * @param districtName
	 * @return
	 */
	public static String getURLForDistrict(String districtName) {
		if(allDistrictDetails == null)
			allDistrictDetails = findAllDistricts();
		
		if(allDistrictDetails.containsKey(districtName))
			return allDistrictDetails.get(districtName.toUpperCase());
		else
			return null;
	}
	
	/**
	 * Returns a <code>Map<String, String></code> containing
	 * all the districts and their URLs.
	 * 
	 * @return
	 */
	public static Map<String, String> getAllDistrictDetails() {
		if(allDistrictDetails == null)
			allDistrictDetails = findAllDistricts();
		return allDistrictDetails;
	}
	
	/**
	 * Returns a <code>Map<String, String></code> containing district 
	 * names and URLs specific to Bangalore.
	 * 
	 * @param anchor
	 * @return
	 */
	private static Map<String, String> getDistrictURLsForBangalore(HtmlAnchor anchor) {
        WebClient client = new WebClient();
		
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(1000);
		
		String bangaloreDistrictURL = anchor.getHrefAttribute();
		String baseURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.RECENT_SEARCH_YEAR);
		//String baseURL = getProperties().getProperty("FINAL_ROLL_URL").replace("%d", ServiceConstants.RECENT_SEARCH_YEAR);;
		String draftRollURL = baseURL;
		baseURL = baseURL + "/" + bangaloreDistrictURL;
		
		try {
			HtmlPage bangaloreDistrictPage = client.getPage(baseURL);
			List<HtmlAnchor> bangaloreDistrictAnchors = bangaloreDistrictPage.getAnchors();
			Map<String, String> allBangaloreDistrictURLs = bangaloreDistrictAnchors.stream()
			                                                                       .filter(eachAnchor -> eachAnchor.asText().contains("/"))
			                                                                       .collect(Collectors.toMap(eachAnchor -> formatDistrictName(eachAnchor.asText()), 
			                                                                    		   eachAnchor -> draftRollURL + "/" + eachAnchor.getHrefAttribute()));
			return allBangaloreDistrictURLs;
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		} finally {
			if(client != null) {
				client.closeAllWindows();
				client = null;
			}
		}
		return null;
	}
	
	/**
	 * District names are listed in both Kannada and English languages.
	 * This method filters out the Kannada names and keeps their 
	 * English counterparts.
	 * 
	 * @param districtName
	 * @return
	 */
	private static String formatDistrictName(String districtName) {
		districtName = districtName.replaceAll("[^\\p{ASCII}]", "").trim();
		if(districtName.startsWith("/")) {
			districtName = districtName.replace("/ ", "");
		}
		if(districtName.startsWith(".")) {
			districtName = districtName.substring(districtName.indexOf("/") + 2, districtName.length());
		}
		return districtName;
	}
}
