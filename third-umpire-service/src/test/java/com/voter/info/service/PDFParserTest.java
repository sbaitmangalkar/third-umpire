package com.voter.info.service;

import org.junit.Before;
import org.junit.Test;

import com.voter.info.model.SearchResult;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class PDFParserTest {
	private String name;
	
	@Before
	public void init() {
		name = "Gopal Malagi";
	}
	
	@Test
	public void test() {
		String firstName = name.split("\\s")[0];
		String lastName = name.split("\\s")[1];
		SearchResult res = PDFParser.partialParse(
				"http://ceokarnataka.kar.nic.in/DraftRolls_2016/DraftRolls_2016/English/WOIMG/AC156/AC1560162.pdf",
				firstName, lastName);
		System.out.println(res);
	}
}
