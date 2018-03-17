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
 * Parses a given URL that contains a PDF file.
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
	 * Returns a <code>SearchResult</code> after parsing a 
	 * PDF file present in the given URL and searching for a given name.
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

			
			//text = text.replaceAll("(?m)^[ \\t]*\\r?\\n", "");
			 

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
			reader.close();
		} catch (IOException e) {
			System.out.println("PDFParser::partialParse(String fileURL, String firstName, String lastName)");
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		return searchResult;
	}

	/**
	 * Extracts required text from a PDF page.
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
			System.out.println("PDFParser::extractTextFromPDFPage(PdfReader reader, int pageNumber)");
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
		return null;
	}

}
