package com.voter.info.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.voter.info.model.SearchResult;
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
	public static List<Voter> findPerson(String firstName, String middleName, String lastName, String districtName) {
		//TODO: Implement findPerson(String name) method
		List<String> allPollingStationsInDistrict  = AssemblyConstituencyFinder.findAllAssemblyConstituencies(districtName)
		                                                              .entrySet()
		                                                              .parallelStream()
		                                                              .map(entry -> entry.getValue())
		                                                              .map(pl -> PollingStationFinder.findAllPollingStationsURL(pl))
		                                                              .flatMap(List::stream)
		                                                              .collect(Collectors.toList());
		
		System.out.println("Total no. of polling stations in " + districtName + ": " + allPollingStationsInDistrict.size());
		
		                                                              
		List<SearchResult> searchResults =  allPollingStationsInDistrict.parallelStream()
                                                                        .map(url -> PDFParser.partialParse(url, firstName, lastName))
                                                                        .filter(res -> res.getResult().contains(firstName))
                                                                        .collect(Collectors.toList());
		List<Voter> voters = processSearchResults(searchResults, firstName, lastName);
		voters.forEach(System.out::println);
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
	public static List<Voter> findPerson(String firstName,String middleName, String lastName, String districtName, String assemblyConstituencyName) {
		String assemblyConstituencyURL = AssemblyConstituencyFinder.getURLForAssemblyConstituency(districtName, assemblyConstituencyName);
		List<String> allPollingStationURLs = PollingStationFinder.findAllPollingStationsURL(assemblyConstituencyURL);
		List<SearchResult> searchResults =  allPollingStationURLs.parallelStream()
		                                                         .map(url -> PDFParser.partialParse(url, firstName, lastName))
		                                                         .filter(res -> res.getResult().contains(firstName))
		                                                         .collect(Collectors.toList());
		List<Voter> voters = processSearchResults(searchResults, firstName, lastName);
		voters.forEach(System.out::println);
		return voters;
	}
	
	/**
	 * 
	 * @param searchResults
	 * @return
	 */
	private static List<Voter> processSearchResults(List<SearchResult> searchResults, String firstName, String lastName) {
		List<Voter> voters = null;
		if(searchResults == null || searchResults.size() <= 0) {
			Voter badResult = new Voter("NA", "NA", "NA", "NA", "NA", "NA");
			voters = new ArrayList<>();
			voters.add(badResult);
			return voters;
		}
		if(searchResults.size() > 1) {
			voters = searchResults.stream()
			                      .map(res -> buildVoter(res,firstName, lastName))
			                      .collect(Collectors.toList());
		} else {
			SearchResult searchResult = searchResults.get(0);
			voters = new ArrayList<>();
			voters.add(buildVoter(searchResult,firstName, lastName));
		}
		return voters;
	}
	
	/**
	 * 
	 * @param searchResult
	 * @return
	 */
	private static Voter buildVoter(SearchResult searchResult, String firstName, String lastName) {
		Voter voter = null;
		String result = searchResult.getResult();
		if(result.split("\\|").length > 1)
			result = filterRequiredVoter(result, firstName, lastName);
			
		String probableAddress = searchResult.getProbableAddress();
		result = result.replaceAll("Photo", ""); 
		result = result.replaceAll("Not \\n", "");
		result = result.replaceAll("Available", "");
		 
		String[] resultPortions = result.split("\\n");
		
		String houseNumber = resultPortions[0];
		String dependentName = resultPortions[1];
		String name = resultPortions[2];
		String sexAndAge = null;
		String dependent = null;
		
		String address = houseNumber + ", " + probableAddress;
		
		if (resultPortions.length == 9) {
			//String id = resultPortions[4];
			sexAndAge = resultPortions[5];
			dependent = resultPortions[6];
		} else if (resultPortions.length == 8 || resultPortions.length > 10) {
			//String id = resultPortions[3];
			sexAndAge = resultPortions[4];
			dependent = resultPortions[5];
			dependent = dependent.substring(0, dependent.indexOf("'"));
		} else if(resultPortions.length == 10) {
			name = resultPortions[3];
			sexAndAge = resultPortions[6];
			dependent = resultPortions[7];
		} else {
			sexAndAge = resultPortions[3];
			dependent = resultPortions[4];
		}
		sexAndAge = sexAndAge.replace("Sex:", "");
		sexAndAge = sexAndAge.replace("Age:", "|");
		String sex = sexAndAge.split("\\|")[0].trim();
		String age = sexAndAge.split("\\|")[1].trim();
		age = String.valueOf(Integer.valueOf(age) + 1);
		
		voter = new Voter(name, dependent, dependentName, address, age, sex);
		return voter;
	}
	
	private static String filterRequiredVoter(String result, String firstName, String lastName) {
		String[] multipersonel = result.split("\\|");
		String res = Stream.of(multipersonel)
		                   .filter(str -> str.split("\\n")[2].trim().equalsIgnoreCase(firstName + " " + lastName))
		                   .findFirst()
		                   .get();
		return res;
	}
}
