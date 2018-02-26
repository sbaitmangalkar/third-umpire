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
	 * 
	 * @param districtURL
	 */
	public static Map<String, String> findAllAssemblyConstituencies(String districtName) {
		WebClient client = new WebClient();

		client.getOptions().setThrowExceptionOnFailingStatusCode(false);
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(true);
		client.waitForBackgroundJavaScript(1000);
		String draftRollURL = getProperties().getProperty("DRAFT_ROLL_URL").replace("%d", ServiceConstants.HISTORY_SEARCH_YEAR);
		
		try {
			String districtURL = DistrictFinder.getURLForDistrict(districtName);
			HtmlPage assemblyConstPage = client.getPage(districtURL);
			List<HtmlAnchor> allPageAnchors = assemblyConstPage.getAnchors();
			Map<String, String> asseblyConstituencyDetails = allPageAnchors.stream()
			                                                               .filter(anchor -> anchor.asText().contains("/"))
			                                                               .collect(Collectors.toMap(anchor -> formatAssemblyConstituencyName(anchor.asText()), 
			                                                            		   anchor -> draftRollURL + "/" + anchor.getHrefAttribute()));
			return asseblyConstituencyDetails;
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param districtURL
	 * @param assemblyConstituencyName
	 * @return
	 */
	public static String getURLForAssemblyConstituency(String districtName, String assemblyConstituencyName) {
		return findAllAssemblyConstituencies(districtName).get(assemblyConstituencyName);
	}
	
	/**
	 * 
	 * @param assemblyConstName
	 * @return
	 */
	private static String formatAssemblyConstituencyName(String assemblyConstName) {
		assemblyConstName = assemblyConstName.substring(assemblyConstName.indexOf("/") + 2, assemblyConstName.length());
		return assemblyConstName;
	}
	
	public static void main(String[] args) {
		findAllAssemblyConstituencies("http://ceokarnataka.kar.nic.in/DraftRolls_2016/AC_List_B3.aspx?DistNo=32");
	}
}
