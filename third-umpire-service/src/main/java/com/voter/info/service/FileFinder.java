package com.voter.info.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class FileFinder {
	
	private Properties getApplicationProperties() {
		Properties properties = new Properties();
		InputStream inputStream = getClass().getResourceAsStream(ServiceConstants.APP_PROPERTIES);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * 
	 */
	public void findPDF() {
		String defaultYear = ServiceConstants.DEFAULT_SEARCH_YEAR;
		String defaultAcNo = ServiceConstants.DEFAULT_SEARCH_AC_NO;
		String defaultPartNo = ServiceConstants.DEFAULT_SEARCH_PART_NO;
		findPDF(defaultYear, defaultAcNo, defaultPartNo);
	}
	
	/**
	 * 
	 * @param year
	 * @param acNo
	 * @param partNo
	 */
	public void findPDF(String year, String acNo, String partNo) {
		Extractor extractor = new Extractor();
		Map<String, String> polingDetails = extractor.extractPolingStationDetails(year, acNo);
		/*
		 * TODO: polingDetails contains acNo and part number as a key.
		 * These should be extracted and a final URL should be built. 
		 */
		partNo = String.format("%03d", Integer.parseInt(partNo));
		String searchKey = acNo + "|" + partNo;
		String acNoAndPartNo =  polingDetails.entrySet()
		                                     .stream()
		                                     .filter(mapper -> mapper.getKey().equals(searchKey))
		                                     .map(mapper -> mapper.getKey())
		                                     .collect(Collectors.joining());
		String docAcNo = acNoAndPartNo.split("\\|")[0];
		if(docAcNo.length() == 3)
			docAcNo = docAcNo + "0";
		String docPartNo = acNoAndPartNo.split("\\|")[1];
		String docUrl = getApplicationProperties().getProperty("DOC_BASE_URL");
		StringBuilder docURLBuilder = new StringBuilder(docUrl);
		docURLBuilder.append(acNoAndPartNo.split("\\|")[0]);
		docURLBuilder.append("/");
		docURLBuilder.append("AC");
		docURLBuilder.append(docAcNo);
		docURLBuilder.append(docPartNo);
		docURLBuilder.append(".pdf");
		
		
		System.out.println(docURLBuilder.toString());
		
		/*
		 * TODO: As the required document URL is built, create a
		 * Reader and parse the PDF file
		 */
	}
	
	public static void main(String[] args) {
		FileFinder finder = new FileFinder();
		finder.findPDF();
	}
}