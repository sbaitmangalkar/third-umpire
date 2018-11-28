package com.voter.info.service;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for <code>PersonFinder</code>
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
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
	public void findPersonInAnAssemblyConstituency() {
		long startTime = System.currentTimeMillis();
		PersonFinder.findPerson(firstName, middleName, lastName, districtName, assemblyConstituencyName);
		long endTime = System.currentTimeMillis();

		long totaltime = ((endTime - startTime) / 1000) / 60;
		System.out.println("Time taken to crawl entire assembly constituency -> " + totaltime + " min(s).");
	}

	@Test
	public void findPersonInDistrict() {
		long startTime = System.currentTimeMillis();
		PersonFinder.findPerson(firstName, middleName, lastName, districtName);
		long endTime = System.currentTimeMillis();

		long totaltime = ((endTime - startTime) / 1000) / 60;
		System.out.println("Time taken to crawl entire district records -> " + totaltime + " min(s).");
	}
}
