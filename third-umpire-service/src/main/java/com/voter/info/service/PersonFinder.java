package com.voter.info.service;

import com.voter.info.model.Voter;

public class PersonFinder {
	public static Voter findPerson(String name, String districtName) {
		//TODO: Implement findPerson(String name) method
		
		return null;
	}
	
	public static Voter findPerson(String name, String districtName, String assemblyConstituencyName) {
		//TODO: Implement findPerson(String name, String assemblyConstituencyName) method
		String assemblyConstituencyURL = AssemblyConstituencyFinder.getURLForAssemblyConstituency(districtName, assemblyConstituencyName);
		return null;
	}
}
