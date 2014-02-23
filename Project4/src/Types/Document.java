package Types;

import java.util.LinkedList;
import java.util.List;

public class Document {
	/** Document Attributes **/
	public String title;
	public Integer start_page;
	public Integer end_page;
	public Integer year;
	public String volume;
	public Integer number;
	public String url;
	public String ee;
	public String cdrom;
	public String cite;
	public String crossref;
	public String isbn;
	public String series;
	public String key;
	public String mdate;

	/** Editor Attributes **/
	public String editor_name;

	/** Book Title Attributes **/
	public String book_title;

	/** Genre Attributes **/
	public String genre_name;

	/** Publisher Attributes **/
	public String publisher_name;

	/** Author Attributes **/
	public List<String> author_names = new LinkedList<>();
	
	

	/**	Genereate a string representation of the object.**/
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("BOOK DOCUMENT:\n");
		out.append("title: "+ title + "\n");
		out.append("editor: "+ editor_name + "\n");
		out.append("start_page: "+ start_page + "\n");
		out.append("end_page: "+ end_page + "\n");
		out.append("year: "+ year + "\n");
		out.append("volume: "+ volume + "\n");
		out.append("number: "+ number + "\n");
		out.append("url: "+ url + "\n");
		out.append("ee: "+ ee + "\n");
		out.append("cdrom: "+ cdrom + "\n");
		out.append("cite: "+ cite + "\n");
		out.append("crossref: "+ crossref + "\n");
		out.append("isbn: "+ isbn + "\n");
		out.append("series: "+ series + "\n");
		out.append("key: "+ key + "\n");
		out.append("mdate: "+ mdate + "\n");
		out.append("Author Names: ");
		for (String author : author_names) {
			out.append(author + ", ");
		}
		out.append("\n\n");
		return out.toString();
	}
}
