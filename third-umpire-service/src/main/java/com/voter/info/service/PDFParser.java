package com.voter.info.service;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.voter.info.model.SearchResult;
import com.voter.info.util.MemoryManager;

/**
 * 
 * @author Shyam | catch.shyambaitmangalkar@gmail.com
 *
 */
public class PDFParser {
	@SuppressWarnings("unused")
	private static void fullParse(String fileURL, String firstName) {
		try {
			long before = MemoryManager.getMemoryUse();
			PdfReader reader = new PdfReader(new URL(fileURL).openStream());
			int totalPages = reader.getNumberOfPages();
			// System.out.println(totalPages);
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
	public static SearchResult partialParse(String fileURL, String firstName, String lastName) {
		SearchResult searchResult = new SearchResult();
		try {

			PdfReader reader = new PdfReader(new RandomAccessFileOrArray(new URL(fileURL).openStream()), null);
			int totalPages = reader.getNumberOfPages();

			String text = IntStream.range(1, totalPages)
					               .mapToObj(pageNumber -> extractTextFromPDFPage(reader, pageNumber))
					               .filter(pageText -> StringUtils.contains(pageText, firstName))
					               .collect(Collectors.joining());
			String probableAddress = text.split("\\n")[0];

			
			 /*text = text.replaceAll("[ ]{2,}", " "); text = text.replaceAll("Name:", "");
			 text = text.replaceAll("Photo", ""); text = text.replaceAll("Not \\n", "");
			 text = text.replaceAll("Available", ""); text =
			 text.replaceAll("(?m)^[ \\t]*\\r?\\n", "");*/
			 

			// System.out.println(text);
			String[] entries = text.split("Name :");

			String result = Stream.of(entries)
					              .filter(e -> e.contains(lastName))
					              .filter(e -> e.contains(firstName))
					              .map(e -> e.trim())
					              .collect(Collectors.joining("|"));
			if (probableAddress.contains(")"))
				probableAddress = probableAddress.substring(probableAddress.indexOf(")") + 1, probableAddress.length());
			if (probableAddress.contains("Continued"))
				probableAddress = StringUtils.replace(probableAddress, "Continued", "").trim();

			searchResult.setResult(result);
			searchResult.setProbableAddress(probableAddress);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchResult;
	}

	/**
	 * 
	 * @param reader
	 * @param pageNumber
	 * @return
	 */
	private static String extractTextFromPDFPage(PdfReader reader, int pageNumber) {

		try {
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy strategy = parser.processContent(pageNumber, new SimpleTextExtractionStrategy());

			String textFromPage = strategy.getResultantText();
			return textFromPage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String name = "";
		
		String firstName = name.split("\\s")[0];
		String lastName = name.split("\\s")[1];
		SearchResult res = partialParse(
				"http://ceokarnataka.kar.nic.in/DraftRolls_2016/DraftRolls_2016/English/WOIMG/AC156/AC1560162.pdf",
				firstName, lastName);
		System.out.println(res);

	}

}
