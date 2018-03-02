package com.voter.info.service;

import org.junit.Before;
import org.junit.Test;

public class PersonFinderTest {
	private String firstName;
	private String middleName;
	private String lastName;
	private String districtName;
	private String assemblyConstituencyName;
	
	@Before
	public void init() {
		firstName = "Anusha";
		lastName = "Malagi";
		districtName = "B.B.M.P(NORTH)";
		assemblyConstituencyName = "Mahalakshmi Layout";
		
	}
	
	@Test
	public void testFor() {
		//TODO: findPerson method is not completely ready for use/test. 
		PersonFinder.findPerson(firstName, middleName, lastName, districtName, assemblyConstituencyName);
	}
}
