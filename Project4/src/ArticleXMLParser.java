import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;

public class ArticleXMLParser extends DefaultHandler {

	public ArticleXMLParser(){
		
	}
	
	public void runExample() {
		parseDocument("final-data.xml");
		printData();
	}

	private void parseDocument(String filepath) {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse(new File(filepath), this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	private void printData(){
		
	}
	
	/*******************************************************************************************************
	 * Event Handlers
	 * 
	 * Possible Optimizations:
	 * - If an order can be assumed within the document tags of the XML, use switch instead of if-elses
	 * - Reduce space complexity by executing queries every time a document element is finished
	 * - Turn off auto commit and manually commit after parsing finishes
	 * - 
	 *******************************************************************************************************/
	
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

	}
}