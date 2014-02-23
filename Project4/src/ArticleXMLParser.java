import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import Types.Document;
import Types.Element;

public class ArticleXMLParser extends DefaultHandler {

	private List<Document> mDocuments = new ArrayList<Document>();;
	private Document mCurrentDocument;
	private String mCurrentString;
	private String mFilePath;
	
	public ArticleXMLParser(String filePath){
		mFilePath = filePath;
	}
	
	public void runExample() {
		parseDocument(mFilePath);
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
		System.out.println("No of Documents '" + mDocuments.size() + "'.");
		for (Document d : mDocuments) {
			System.out.println(d);
		}
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
		//reset
		mCurrentString = "";
		if(qName.equalsIgnoreCase("article") ||
				qName.equalsIgnoreCase("inproceedings") ||
				qName.equalsIgnoreCase("proceedings") ||
				qName.equalsIgnoreCase("book") ||
				qName.equalsIgnoreCase("incollection") ||
				qName.equalsIgnoreCase("phdthesis") ||
				qName.equalsIgnoreCase("mastersthesis") ||
				qName.equalsIgnoreCase("www")) {
			//create a new instance of employee
			mCurrentDocument = new Document();
			mCurrentDocument.mdate = attributes.getValue("mdate"); 
			mCurrentDocument.key   = attributes.getValue("key");
		}

	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		mCurrentString = new String(ch,start,length).trim();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch(getEnum(qName)) {
		case DOCUMENT:
			mDocuments.add(mCurrentDocument);
			break;
		case TITLE: 
		    mCurrentDocument.title = mCurrentString;
		    break;
		case EDITOR: 
		    mCurrentDocument.editor_name = mCurrentString;
		    break;
		case AUTHOR: 
		    mCurrentDocument.author_names.add(mCurrentString);
		    break;
		case PAGES:
			mCurrentDocument.start_page = Integer.parseInt(mCurrentString.split("-")[0]);
		    mCurrentDocument.end_page = Integer.parseInt(mCurrentString.split("-")[1]);
		    break;
		case YEAR: 
		    mCurrentDocument.year = !mCurrentString.equals("") ? Integer.parseInt(mCurrentString) : 0;
		    break;
		case VOLUME: 
		    mCurrentDocument.volume = mCurrentString;
		    break;
		case NUMBER: 
		    mCurrentDocument.number = Integer.parseInt(mCurrentString);
		    break;
		case URL: 
		    mCurrentDocument.url = mCurrentString;
		    break;
		case EE: 
		    mCurrentDocument.ee = mCurrentString;
		    break;
		case CDROM: 
		    mCurrentDocument.cdrom = mCurrentString;
		    break;
		case CITE: 
		    mCurrentDocument.cite = mCurrentString;
		    break;
		case CROSSREF: 
		    mCurrentDocument.crossref = mCurrentString;
		    break;
		case ISBN: 
		    mCurrentDocument.isbn = mCurrentString;
		    break;
		case SERIES: 
		    mCurrentDocument.series = mCurrentString;
		    break;
		}
	}
	
	private Element getEnum(String tagName) {
		// Lower Case it, avoid typos... :-P
		tagName = tagName.toLowerCase();
		
		if (tagName.equals("author"))
		    return Element.AUTHOR;
		if (tagName.equals("editor"))
		    return Element.EDITOR;
		if (tagName.equals("title"))
		    return Element.TITLE;
		if (tagName.equals("booktitle"))
		    return Element.BOOKTITLE;
		if (tagName.equals("pages"))
		    return Element.PAGES;
		if (tagName.equals("year"))
		    return Element.YEAR;
		if (tagName.equals("address"))
		    return Element.ADDRESS;
		if (tagName.equals("journal"))
		    return Element.JOURNAL;
		if (tagName.equals("volume"))
		    return Element.VOLUME;
		if (tagName.equals("number"))
		    return Element.NUMBER;
		if (tagName.equals("month"))
		    return Element.MONTH;
		if (tagName.equals("url"))
		    return Element.URL;
		if (tagName.equals("ee"))
		    return Element.EE;
		if (tagName.equals("cdrom"))
		    return Element.CDROM;
		if (tagName.equals("cite"))
		    return Element.CITE;
		if (tagName.equals("publisher"))
		    return Element.PUBLISHER;
		if (tagName.equals("note"))
		    return Element.NOTE;
		if (tagName.equals("crossref"))
		    return Element.CROSSREF;
		if (tagName.equals("isbn"))
		    return Element.ISBN;
		if (tagName.equals("series"))
		    return Element.SERIES;
		if (tagName.equals("school"))
		    return Element.SCHOOL;
		if (tagName.equals("chapter"))
		    return Element.CHAPTER;

		
		// Default Return the document tag....?
		return Element.DOCUMENT;
	}
}