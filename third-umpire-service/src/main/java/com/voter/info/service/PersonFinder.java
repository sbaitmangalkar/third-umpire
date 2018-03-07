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
		return null;
	}
	
	/**
	 * 
	 * @param searchResults
	 * @return
	 */
	private static List<Voter> processSearchResults(List<SearchResult> searchResults, String firstName, String lastName) {
		List<Voter> voters = null;
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
		Voter voter = new Voter();
		String result = searchResult.getResult();
		//TODO: Create a diversion for multiple occurrences of names.
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
		voter.setFullName(name.trim());
		voter.setDependentName(dependentName.trim());
		
		String address = houseNumber + ", " + probableAddress;
		voter.setAddress(address);
		if (resultPortions.length == 9) {
			//String id = resultPortions[4];
			sexAndAge = resultPortions[5];
			dependent = resultPortions[6];
		} else if (resultPortions.length == 8 || resultPortions.length > 10) {
			//String id = resultPortions[3];
			sexAndAge = resultPortions[4];
			dependent = resultPortions[5];
			dependent = dependent.substring(0, dependent.indexOf("'"));
		} else {
			sexAndAge = resultPortions[3];
			dependent = resultPortions[4];
		}
		sexAndAge = sexAndAge.replace("Sex:", "");
		sexAndAge = sexAndAge.replace("Age:", "|");
		String sex = sexAndAge.split("\\|")[0].trim();
		String age = sexAndAge.split("\\|")[1].trim();
		voter.setSex(sex);
		voter.setAge(Integer.valueOf(age));
		voter.setDependent(dependent);
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
