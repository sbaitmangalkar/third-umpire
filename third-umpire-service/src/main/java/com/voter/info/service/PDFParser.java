package com.voter.info.service;

import java.io.IOException;
import java.net.URL;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PDFParser {
	public void parse(String fileURL) {
		try {
			PdfReader reader = new PdfReader(new URL(fileURL).openStream());
			String textFromPage = PdfTextExtractor.getTextFromPage(reader, 4);
			
			System.out.println(textFromPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		PDFParser parser = new PDFParser();
		parser.parse("http://ceokarnataka.kar.nic.in/DraftRolls_2016/DraftRolls_2016/English/WOIMG/AC156/AC1560162.pdf");
		//parser.parse("http://ceokarnataka.kar.nic.in/DraftRolls_2018/English/AC156/AC1560180.pdf");
	}
	
}
