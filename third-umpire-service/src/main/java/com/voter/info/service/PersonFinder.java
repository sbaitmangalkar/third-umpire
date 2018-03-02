package com.voter.info.service;

import java.util.List;

import com.voter.info.model.Voter;

public class PersonFinder {
	/**
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param districtName
	 * @return
	 */
	public static Voter findPerson(String firstName, String middleName, String lastName, String districtName) {
		//TODO: Implement findPerson(String name) method
		
		return null;
	}
	
	/**
	 * 
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param districtName
	 * @param assemblyConstituencyName
	 * @return
	 */
	public static Voter findPerson(String firstName,String middleName, String lastName, String districtName, String assemblyConstituencyName) {
		//TODO: Implement findPerson(String name, String assemblyConstituencyName) method
		String assemblyConstituencyURL = AssemblyConstituencyFinder.getURLForAssemblyConstituency(districtName, assemblyConstituencyName);
		List<String> allPollingStationURLs = PollingStationFinder.findAllPollingStationsURL(assemblyConstituencyURL);
		allPollingStationURLs.parallelStream()
		                     .map(url -> PDFParser.partialParse(url, firstName, lastName))
		                     .filter(res -> res.getResult().contains(lastName))
		                     .forEach(System.out::println);
		return null;
	}
	
}
