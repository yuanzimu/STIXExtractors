package STIXExtractor;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;					
import javax.xml.parsers.ParserConfigurationException;

import org.mitre.stix.stix_1.STIXPackage;
import org.mitre.stix.stix_1.STIXHeaderType;
import org.mitre.stix.common_1.ExploitTargetsType;
import org.mitre.stix.common_1.StructuredTextType;
import org.mitre.stix.common_1.ReferencesType;
import org.mitre.stix.exploittarget_1.ExploitTarget;
import org.mitre.stix.exploittarget_1.VulnerabilityType;

import org.xml.sax.SAXException;			

public class CveExtractor extends HTMLExtractor	{
						
	private static final Logger logger = LoggerFactory.getLogger(CveExtractor.class);
	private STIXPackage stixPackage;

	public CveExtractor(String cveInfo)	{
		stixPackage = extract(cveInfo);
	}
					
	public STIXPackage getStixPackage() {
		return stixPackage;
	}

	private STIXPackage extract (String cveInfo)	{

		try	{
			GregorianCalendar calendar = new GregorianCalendar();
			XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(				
				new GregorianCalendar(TimeZone.getTimeZone("UTC")));
			stixPackage = new STIXPackage()				
 				.withSTIXHeader(new STIXHeaderType().
					withTitle("CVE")) 
				.withTimestamp(now)
	 			.withId(new QName("gov.ornl.stucco", "CVE-" + UUID.randomUUID().toString(), "stucco"));
			ExploitTargetsType exploitTargets = new ExploitTargetsType();			
	
			Document doc = Jsoup.parse(cveInfo);
			Elements entries = doc.select("item");

			for (Element entry : entries)	{	
				String cveId = "cve-";
				ExploitTarget exploitTarget = new ExploitTarget();
				VulnerabilityType vulnerability = new VulnerabilityType();
				ReferencesType referencesType = new ReferencesType();

				//cve		
				if (entry.hasAttr("name"))	{
					vulnerability
						.withCVEID(entry.attr("name"));
					cveId = cveId + entry.attr("name");
				}

				//description
				if (entry.select("desc").hasText())
					vulnerability
						.withDescriptions(new StructuredTextType()
							.withValue(entry.select("desc").text()));
				
				//status 
				if (entry.select("status").text().equals("Candidate"))
					vulnerability
						.withIsPubliclyAcknowledged(false);
				
				if (entry.select("status").text().equals("Entry")) 
					vulnerability
						.withIsPubliclyAcknowledged(true);
										
				//references
				Elements references = entry.select("ref");
				if (references.size() > 0)	{
					for (Element reference : references)	{
						if (reference.hasAttr("url"))
							referencesType
								.withReferences(reference.attr("url"));
						else
							referencesType
								.withReferences(reference.attr("source") + ":" + reference.text());
					}
	
					vulnerability
						.withReferences(referencesType);
				}

				//comments
				Elements comments = entry.select("comment");			
				for (Element comment : comments)	{
					vulnerability
						.withShortDescriptions(new StructuredTextType()		//list
							.withValue(comment.select("comment").text()));
				}
		
				exploitTargets
					.withExploitTargets(exploitTarget		//list
						.withId(new QName("gov.ornl.stucco", cveId, "stucco"))
						.withTitle("CVE")
						.withVulnerabilities(vulnerability	//list
							.withSource("CVE")));	
			}				
			
			stixPackage
				.withExploitTargets(exploitTargets);
			
		} catch (DatatypeConfigurationException e)	{
			e.printStackTrace();
		} 

		return stixPackage;
	}
	
	boolean validate(STIXPackage stixPackage) {
		
		try	{
			return stixPackage.validate();
		}			
		catch (SAXException e)	{
			e.printStackTrace();
		}
		return false;
	}
}