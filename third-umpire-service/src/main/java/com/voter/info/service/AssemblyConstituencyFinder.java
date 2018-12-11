package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AssemblyConstituencyFinder {
	/**
	 * 
	 * @return
	 */
	private static Properties getProperties() {
		Properties properties = new Properties();
		InputStream inputStream = AssemblyConstituencyFinder.class.getResourceAsStream(ServiceConstants.APP_PROPERTIES);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Returns a <code> Map<String, String></code> containing all the
	 * assembly constituency names and their URLs for a given district.
	 * 
	 * @param districtName
	 */
	public static Map<String, String> findAllAssemblyConstituencies(String districtName) {
		WebClient client = new WebClient();

		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(1000);
		String draftRollURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.RECENT_SEARCH_YEAR);
		//String draftRollURL = getProperties().getProperty("FINAL_ROLL_URL").replace("%d", ServiceConstants.RECENT_SEARCH_YEAR);
		
		try {
			String districtURL = DistrictFinder.getURLForDistrict(districtName);
			
			HtmlPage assemblyConstPage = client.getPage(districtURL);
			List<HtmlAnchor> allPageAnchors = assemblyConstPage.getAnchors();
			Map<String, String> asseblyConstituencyDetails = allPageAnchors.stream()
			                                                               .filter(anchor -> !anchor.asText().equals("Home"))
			                                                               .collect(Collectors.toMap(anchor -> formatAssemblyConstituencyName(anchor.asText()), 
			                                                            		   anchor -> draftRollURL + "/" + anchor.getHrefAttribute()));
			return asseblyConstituencyDetails;
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			System.out.println("AssemblyConstituencyFinder::findAllAssemblyConstituencies(String districtName)");
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
	 * Returns URL for a given district and assembly constituency.
	 * 
	 * @param districtName
	 * @param assemblyConstituencyName
	 * @return
	 */
	public static String getURLForAssemblyConstituency(String districtName, String assemblyConstituencyName) {
		Map<String, String> assemblyConstituencyURLs = findAllAssemblyConstituencies(districtName);
		if(assemblyConstituencyURLs.containsKey(assemblyConstituencyName))
			return assemblyConstituencyURLs.get(assemblyConstituencyName);
		else
			return null;
	}
	
	/**
	 * Each assembly constituency has names listed in both 
	 * Kannada and English languages. This method keeps the
	 * English names and removes the Kannada counterparts since,
	 * the parser will not parse Kannada language.
	 * 
	 * @param assemblyConstName
	 * @return
	 */
	private static String formatAssemblyConstituencyName(String assemblyConstName) {
		assemblyConstName = assemblyConstName.replaceAll("[^\\p{ASCII}]", "|").trim();
		if(assemblyConstName.contains("/"))
			assemblyConstName = assemblyConstName.substring(assemblyConstName.indexOf("/") + 2, assemblyConstName.length());
		else
			assemblyConstName = assemblyConstName.substring(assemblyConstName.lastIndexOf("|") + 2, assemblyConstName.length());
		return assemblyConstName;
	}
}
