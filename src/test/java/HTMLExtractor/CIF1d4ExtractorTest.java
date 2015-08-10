package STIXExtractor;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.mitre.stix.stix_1.STIXPackage;

import org.junit.Test;

import static org.junit.Assert.*;

import STIXExtractor.CIF1d4Extractor;

/**
 * Unit test for CIF1d4Extractor Extractor.
 */
public class CIF1d4ExtractorTest	{
	
	/**
	 * Test empty document
	 */
	@Test
	public void test_empty_document()	{

		System.out.println("STIXExtractor.CIF1d4ExtractorTest.test_empty_document()");

		String cifInfo = "";

		CIF1d4Extractor cifExtractor = new CIF1d4Extractor(cifInfo);
		STIXPackage stixPackage = cifExtractor.getStixPackage();

		System.out.println("Testing that package is null");
		assertTrue(stixPackage == null);
	}

	/**
	 * Test one element
	 */
	@Test
	public void test_one_element()	{

		System.out.println("STIXExtractor.CIF1d4ExtractorTest.test_one_element()");

		String cifInfo = "103.36.125.189";

		CIF1d4Extractor cifExtractor = new CIF1d4Extractor(cifInfo);
		STIXPackage stixPackage = cifExtractor.getStixPackage();
		
		System.out.println("Validating CIF1d4 stixPackage");
		assertTrue(cifExtractor.validate(stixPackage));

		Document doc = Jsoup.parse(stixPackage.toXMLString(), "", Parser.xmlParser());
		Element element = doc.select("cybox|Observable").first();
		
		System.out.println("Testing Source");
		assertEquals(element.select("cyboxCommon|Information_Source_Type").text(), "1d4.us");
		System.out.println("Testing IP String");
		assertEquals(element.select("AddressObj|Address_Value").text(), "103.36.125.189");
		System.out.println("Testing IP Long");
		assertEquals(element.select("cybox|Object").attr("id"), "stucco:ip-1730444733");
		System.out.println("Testing Description");
		assertEquals(element.select("cybox|Description").text(), "103.36.125.189");
		System.out.println("Testing Title");
		assertEquals(element.select("cybox|Title").text(), "IP");
		System.out.println("Testing Keywords (Tags)");
		assertEquals(element.select("cybox|Keyword").text(), "Scanner");
	}

	/**
	 * Test four elements
	 */
	@Test
	public void test_four_elements()	{

		System.out.println("STIXExtractor.CIF1d4ExtractorTest.test_four_elements()");

		String cifInfo = 
			"112.120.48.179\n" + 
			"113.195.145.12\n" +
			"113.195.145.70\n" + 
			"113.195.145.80";

		CIF1d4Extractor cifExtractor = new CIF1d4Extractor(cifInfo);
		STIXPackage stixPackage = cifExtractor.getStixPackage();
		
		System.out.println("Validating CIF1d4 stixPackage");
		assertTrue(cifExtractor.validate(stixPackage));

		Document doc = Jsoup.parse(stixPackage.toXMLString(), "", Parser.xmlParser());
		System.out.println("Testing 1st element:");
		Element element = doc.select("cybox|Observable:has(AddressObj|Address_Value:matches(^112.120.48.179\\Z))").first();
		
		System.out.println("Testing Source");
		assertEquals(element.select("cyboxCommon|Information_Source_Type").text(), "1d4.us");
		System.out.println("Testing IP String");
		assertEquals(element.select("AddressObj|Address_Value").text(), "112.120.48.179");
		System.out.println("Testing IP Long");
		assertEquals(element.select("cybox|Object").attr("id"), "stucco:ip-1886924979");
		System.out.println("Testing Description");
		assertEquals(element.select("cybox|Description").text(), "112.120.48.179");
		System.out.println("Testing Title");
		assertEquals(element.select("cybox|Title").text(), "IP");
		System.out.println("Testing Keywords (Tags)");
		assertEquals(element.select("cybox|Keyword").text(), "Scanner");
		
		System.out.println("Testing 2nd element:");
		element = doc.select("cybox|Observable:has(AddressObj|Address_Value:matches(^113.195.145.12\\Z))").first();
		
		System.out.println("Testing Source");
		assertEquals(element.select("cyboxCommon|Information_Source_Type").text(), "1d4.us");
		System.out.println("Testing IP String");
		assertEquals(element.select("AddressObj|Address_Value").text(), "113.195.145.12");
		System.out.println("Testing IP Long");
		assertEquals(element.select("cybox|Object").attr("id"), "stucco:ip-1908642060");
		System.out.println("Testing Description");
		assertEquals(element.select("cybox|Description").text(), "113.195.145.12");
		System.out.println("Testing Title");
		assertEquals(element.select("cybox|Title").text(), "IP");
		System.out.println("Testing Keywords (Tags)");
		assertEquals(element.select("cybox|Keyword").text(), "Scanner");
	
		System.out.println("Testing 3rd element:");
		element = doc.select("cybox|Observable:has(AddressObj|Address_Value:matches(^113.195.145.70\\Z))").first();
		
		System.out.println("Testing Source");
		assertEquals(element.select("cyboxCommon|Information_Source_Type").text(), "1d4.us");
		System.out.println("Testing IP String");
		assertEquals(element.select("AddressObj|Address_Value").text(), "113.195.145.70");
		System.out.println("Testing IP Long");
		assertEquals(element.select("cybox|Object").attr("id"), "stucco:ip-1908642118");
		System.out.println("Testing Description");
		assertEquals(element.select("cybox|Description").text(), "113.195.145.70");
		System.out.println("Testing Title");
		assertEquals(element.select("cybox|Title").text(), "IP");
		System.out.println("Testing Keywords (Tags)");
		assertEquals(element.select("cybox|Keyword").text(), "Scanner");
		
		System.out.println("Testing 4rd element:");
		element = doc.select("cybox|Observable:has(AddressObj|Address_Value:matches(^113.195.145.80\\Z))").first();
		
		System.out.println("Testing Source");
		assertEquals(element.select("cyboxCommon|Information_Source_Type").text(), "1d4.us");
		System.out.println("Testing IP String");
		assertEquals(element.select("AddressObj|Address_Value").text(), "113.195.145.80");
		System.out.println("Testing IP Long");
		assertEquals(element.select("cybox|Object").attr("id"), "stucco:ip-1908642128");
		System.out.println("Testing Description");
		assertEquals(element.select("cybox|Description").text(), "113.195.145.80");
		System.out.println("Testing Title");
		assertEquals(element.select("cybox|Title").text(), "IP");
		System.out.println("Testing Keywords (Tags)");
		assertEquals(element.select("cybox|Keyword").text(), "Scanner");
	}
}
