package com.voter.info.service;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

public class AssemblyConstituencyFinderTest {
	private String districtName;
	
	@Before
	public void init() {
		districtName = "B.B.M.P(NORTH)";
	}
	
	@Test
	public void findAssemblyConstituenciesOfGivenDistrict() {
		List<String> allAssemblyConstituencies = AssemblyConstituencyFinder.findAllAssemblyConstituencies(districtName)
				 														   .entrySet()
				 														   .stream()
				 														   .map(e -> e.getKey())
				 														   .collect(Collectors.toList());
		System.out.println("No. of assembly constituencies: " + allAssemblyConstituencies.size());
		allAssemblyConstituencies.stream()
		                         .forEach(System.out::println);
	}
}
