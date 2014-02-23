import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import org.xml.sax.helpers.DefaultHandler;

import Types.Element;
import Types.Document;

public class DocumentsXMLParser extends DefaultHandler implements ErrorHandler {
	private boolean charactersToParse;
	private Element state;
	private Document currentDoc;

	public DocumentsXMLParser(){

	}

	public void parseFile(String filePath) {
		try {
			// Configure parser factory
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			spf.setValidating(false);
			spf.setValidating(true);

			// Initialize and register content & error handler
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setErrorHandler(this);
			xmlReader.setContentHandler(this);
			xmlReader.parse(filePath);
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	/*******************************************************************************************************
	 * Content event Handlers
	 * 
	 * Possible Optimizations:
	 * - If an order can be assumed within the document tags of the XML, use switch instead of if-elses
	 * - Reduce space complexity by executing queries every time a document element is finished
	 * - Turn off auto commit and manually commit after parsing finishes
	 * 
	 *******************************************************************************************************/

	@Override
	public void startDocument() throws SAXException {
		charactersToParse = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(localName.equals("book")) {
			System.out.println(attributes.getValue(1));
			currentDoc = new Document();
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(charactersToParse)
			switch(state) {
			case AUTHOR:
				currentDoc.author_names.add(ch);
				break;
			case EDITOR:
				currentDoc.editor_name = String.valueOf(ch);
			case TITLE:
				currentDoc.title = String.valueOf(ch);
				break;
			case BOOKTITLE:
				currentDoc.book_title = String.valueOf(ch);
				break;
			case PAGES:
				currentDoc.start_page = Integer.valueOf(Integer.parseInt(String.valueOf(ch).split("-")[0]));
				currentDoc.end_page = Integer.valueOf(Integer.parseInt(String.valueOf(ch).split("-")[1]));
				break;
			case YEAR:
				currentDoc.year = Integer.valueOf(Integer.parseInt(String.valueOf(ch)));
				break;
			case ADDRESS:
				// ????
				break;
			case JOURNAL:
				// ????
				break;
			case VOLUME:
				break;
			case NUMBER:
				break;
			case MONTH:
				break;
			case URL:
				break;
			case EE:
				break;
			case CDROM:
				break;
			case CITE:
				break;
			case PUBLISHER:
				break;
			case NOTE:
				break;
			case CROSSREF:
				break;
			case ISBN:
				break;
			case SERIES:
				break;
			case SCHOOL:
				break;
			case CHAPTER:
				break;

			}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

	}

	/*******************************************************************************************************
	 * Error event Handlers
	 * 
	 *******************************************************************************************************/

	public void warning(SAXParseException spe) throws SAXException {
		System.out.println("Warning: " + spe.getMessage());
	}

	public void error(SAXParseException spe) throws SAXException {
		System.out.println("Error: " + spe.getMessage());
		if(spe.getMessage().equals("Document is invalid: no grammar found."))
			System.out.println("Warning: Continuing to parse document WITHOUT validation...");
	}

	public void fatalError(SAXParseException spe) throws SAXException {
		System.out.println("Fatal Error: " + spe.getMessage());
		throw spe;
	}
}