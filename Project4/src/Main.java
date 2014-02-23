import java.sql.SQLException;

import Helpers.MySQL;

public class Main {
	public static void main (String [] args) {
		try {
			System.out.println(MySQL.insertBooktitle("book title1"));
			System.out.println(MySQL.insertGenre("genre1"));
			
			DocumentsXMLParser parser = new DocumentsXMLParser();
			parser.parseFile("res/dblp-data-small/final-data.xml");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
