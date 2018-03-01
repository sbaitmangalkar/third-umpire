package com.voter.info.service;

import java.util.List;

import com.voter.info.model.UserRequest;
import com.voter.info.model.Voter;
import com.voter.info.service.PDFParser;

public class PersonFinder {
	public static Voter findPerson(String firstName, String middleName, String lastName, String districtName) {
		//TODO: Implement findPerson(String name) method
		
		return null;
	}
	
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
	public static void main(String[] args) {
		VoterService vs = new VoterServiceImpl();
		UserRequest req = new UserRequest();
		req.setFirstName("");
		req.setLastName("");
		req.setDistrict("");
		req.setAssemblyConstituencyName("");
		vs.findVoter(req);
	}
}
