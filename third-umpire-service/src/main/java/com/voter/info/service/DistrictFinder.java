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
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class DistrictFinder {
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
	 * 
	 * @return
	 */
	public static Map<String, String> findAllDistricts() {
		WebClient client = new WebClient();
		
		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(1000);
		
		try {
			String draftRollURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.HISTORY_SEARCH_YEAR);
			HtmlPage draftRollPage = client.getPage(draftRollURL);
			List<HtmlAnchor> anchors = draftRollPage.getAnchors();
			
			Map<String, String> districtURLs = anchors.stream()
					                     .filter(anchor -> !anchor.asText().trim().contains("/ BANGALORE"))
					                     .collect(Collectors.toMap(anchor -> formatDistrictName(anchor.asText()), anchor -> draftRollURL + "/" + anchor.getHrefAttribute()));
			
			
			
			HtmlAnchor bangaloreDistrictsAnchor = anchors.stream()
			                                   .filter(anchor -> anchor.asText().trim().contains("/ BANGALORE"))
			                                   .findFirst()
			                                   .get();
			
			Map<String, String> bangaloreDistrictURLs = getDistrictURLsForBangalore(bangaloreDistrictsAnchor);
			
			
			Map<String, String> karnatakaDistrictURLs = Stream.of(districtURLs, bangaloreDistrictURLs)
					                                          .flatMap(map -> map.entrySet().stream())
					                                          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			
			return karnatakaDistrictURLs;
			       
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
	
	public static String getURLForDistrict(String districtName) {
		Map<String, String> karnatakaDistrictURLs = findAllDistricts();
		if(districtName.equalsIgnoreCase("Bangalore")) {
			StringBuilder url = new  StringBuilder();
			url.append(karnatakaDistrictURLs.get("B.B.M.P(NORTH)"));
			url.append("|");
			url.append(karnatakaDistrictURLs.get("B.B.M.P(CENTRAL)"));
			url.append("|");
			url.append(karnatakaDistrictURLs.get("B.B.M.P(SOUTH)"));
			url.append("|");
			url.append(karnatakaDistrictURLs.get("BANGALORE URBAN"));
			
			return url.toString();
			
		} else {
			return karnatakaDistrictURLs.get(districtName.toUpperCase());
		}
	}
	
	/**
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
		String baseURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.HISTORY_SEARCH_YEAR);
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
	
	public static void main(String[] args) {
		findAllDistricts();
	}
}
