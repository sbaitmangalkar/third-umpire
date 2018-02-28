package com.voter.info.service;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.voter.info.util.MemoryManager;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class PDFParser {
	public void fullParse(String fileURL, String firstName) {
		try {
			long before = MemoryManager.getMemoryUse();
			PdfReader reader = new PdfReader(new URL(fileURL).openStream());
			int totalPages = reader.getNumberOfPages();
			//System.out.println(totalPages);
			String text = IntStream.range(1, totalPages)
			                       .mapToObj(pageNumber -> extractTextFromPDFPage(reader, pageNumber))
			                       .filter(pageText -> pageText.contains(firstName))
			                       .collect(Collectors.joining());
			
			System.out.println(text);
			System.out.println("==========================");
			System.out.println("Memory used by full Parser: " + (MemoryManager.getMemoryUse() - before));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param fileURL
	 * @param firstName
	 */
	public void partialParse(String fileURL, String firstName) {
		try {
			
			PdfReader reader = new PdfReader(new RandomAccessFileOrArray(new URL(fileURL).openStream()), null);
			int totalPages = reader.getNumberOfPages();
			//System.out.println(totalPages);
			String text = IntStream.range(1, totalPages)
			                       .mapToObj(pageNumber -> extractTextFromPDFPage(reader, pageNumber))
			                       .filter(pageText -> StringUtils.contains(pageText, firstName))
			                       .collect(Collectors.joining());
			text = text.replaceAll("[ ]{2,}", " ");
			text = text.replaceAll("Name:", "");
			text = text.replaceAll("Name :", "");
			text = text.replaceAll("Photo", "");
			text = text.replaceAll("Not \\n", "");
			text = text.replaceAll("Available", "");
			text = text.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
					
			System.out.println(text);
			/*String[] entries = text.split("\\n");
			Stream.of(entries)
			      .forEach(System.out::println);*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param reader
	 * @param pageNumber
	 * @return
	 */
	private String extractTextFromPDFPage(PdfReader reader, int pageNumber) {
		
		//Rectangle2D rect2 = new Rectangle2D.Double(0, 840, 595, 840);
		//RenderFilter renderFilter = new RegionTextRenderFilter(rect2);
		try {
			/*
			 * Custom text extraction strategy
			 */
			//TextExtractionStrategy strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), renderFilter);
			String textFromPage = PdfTextExtractor.getTextFromPage(reader, pageNumber);
			return textFromPage;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		PDFParser parser = new PDFParser();
		String name = "";
		/*parser.fullParse(
				"http://ceokarnataka.kar.nic.in/DraftRolls_2016/DraftRolls_2016/English/WOIMG/AC156/AC1560162.pdf");*/
		
		parser.partialParse(
				"http://ceokarnataka.kar.nic.in/DraftRolls_2016/DraftRolls_2016/English/WOIMG/AC156/AC1560162.pdf", name);
		
	}

}
