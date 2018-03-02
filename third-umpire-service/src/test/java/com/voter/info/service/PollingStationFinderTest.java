package com.voter.info.service;

import org.junit.Test;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class PollingStationFinderTest {
	@Test
	public void test() {
		System.out.println(PollingStationFinder
				.findAllPollingStationsURL("http://ceokarnataka.kar.nic.in/FinalRoll-2017/Part_List.aspx?ACNO=156"));
	}
}
