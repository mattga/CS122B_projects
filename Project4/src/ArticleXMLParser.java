import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import Helpers.MySQL;
import Types.Document;
import Types.Element;

public class ArticleXMLParser extends DefaultHandler {

	private List<Document> mDocuments = new ArrayList<Document>();;
	private Document mCurrentDocument;
	private String mCurrentString;
	private String mFilePath;
	private static int counter = 1;
	
	public ArticleXMLParser(String filePath){
		mFilePath = filePath;
	}

	public void parse() {
		parseDocument(mFilePath);
	}

	public void insertRecords() {
		parseDocument(mFilePath);
		for (Document d : mDocuments) {
			try {
				int titleId = MySQL.insertBooktitle(d.book_title);
				int editorId = MySQL.insertEditor(d.editor_name);
				int genreId = MySQL.insertGenre(d.genre_name);
				int publisherId = MySQL.insertPublisher(d.publisher_name);
				int documentId = MySQL.insertDocument(d.title, d.start_page, d.end_page, 
						d.year, d.volume, d.number, 
						d.url, d.ee, d.cdrom, d.cite, d.crossref, 
						d.isbn, d.series, String.valueOf(editorId), String.valueOf(genreId), String.valueOf(titleId), String.valueOf(publisherId));

				// Loop through all authors.
				int[] authorIds = new int[d.author_names.size()];
				int i = 0;
				for (String author : d.author_names)
					authorIds[i++] = MySQL.insertAuthor(author);
				// Insert all authors...
				for (int j = 0; j < authorIds.length; j++) 
					MySQL.insertAuthorMapping(authorIds[j], documentId);

			} catch (SQLException e) { e.printStackTrace();}
		}

	}

	private void insertRecord(Document d) {
		try {
			int titleId = MySQL.insertBooktitle(d.book_title);
			int editorId = MySQL.insertEditor(d.editor_name);
			int genreId = MySQL.insertGenre(d.genre_name);
			int publisherId = MySQL.insertPublisher(d.publisher_name);
			int documentId = MySQL.insertDocument(d.title, d.start_page, d.end_page, 
					d.year, d.volume, d.number, 
					d.url, d.ee, d.cdrom, d.cite, d.crossref, 
					d.isbn, d.series, String.valueOf(editorId), String.valueOf(genreId), String.valueOf(titleId), String.valueOf(publisherId));

			// Loop through all authors.
			int[] authorIds = new int[d.author_names.size()];
			int i = 0;
			for (String author : d.author_names)
				authorIds[i++] = MySQL.insertAuthor(author);
			// Insert all authors...
			for (int j = 0; j < authorIds.length; j++) 
				MySQL.insertAuthorMapping(authorIds[j], documentId);

		} catch (SQLException e) { e.printStackTrace();}

	}

	private void parseDocument(String filepath) {

		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the file and also register this class for call backs
			sp.parse(new File(filepath), this);
//			SQLWarning warning = MySQL.getConnection().getWarnings();
//			while (warning != null) {
//				System.out.println(warning.getErrorCode() + " - " + warning.getMessage());
//				warning = warning.getNextWarning();
//			}
			MySQL.commitAll();
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}

	public void printData(){
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
//			System.out.println(mCurrentDocument);
			//			mDocuments.add(mCurrentDocument);
			mCurrentDocument.genre_name = qName;
			insertRecord(mCurrentDocument);
			break;
		case TITLE: 
			mCurrentDocument.title = mCurrentString;
			break;
		case BOOKTITLE:
			mCurrentDocument.book_title = mCurrentString;
			break;
		case EDITOR: 
			mCurrentDocument.editor_name = mCurrentString;
			break;
		case AUTHOR: 
			mCurrentDocument.author_names.add(mCurrentString);
			break;
		case PAGES:
			if (!mCurrentString.equals("") && mCurrentString.split("-").length > 1 && mCurrentString != null) {
				try {
					mCurrentDocument.start_page = Integer.parseInt(mCurrentString.split("-")[0]);
					mCurrentDocument.end_page = Integer.parseInt(mCurrentString.split("-")[1]);
				} catch(Exception e) {
					// catching a number formatting error in big-file
					System.out.println("Invalid Page Format.");
					mCurrentDocument.start_page = 0;
					mCurrentDocument.end_page = 0;
				}
			}
			break;
		case YEAR: 
			mCurrentDocument.year = !mCurrentString.equals("") ? Integer.parseInt(mCurrentString) : 0;
			break;
		case VOLUME: 
			try {
				mCurrentDocument.volume = Integer.parseInt(mCurrentString);
			} catch (Exception e) {
				System.out.println("Invalid Numeric Format in Volume Input.");
				mCurrentDocument.volume = 0;// Non Numeric Chars in Input
			}
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
		case PUBLISHER:
			mCurrentDocument.publisher_name = mCurrentString;
			break;
		default://do nothing?
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