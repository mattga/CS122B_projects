
public class Main {
	public static void main (String [] args) {
		
		// Run Article Parser...
		 ArticleXMLParser sax = new ArticleXMLParser("res/dblp-data-small/final-data.xml");
		 sax.runExample();
//		 sax.insertRecords();
		
//		try {
//			System.out.println(MySQL.insertBooktitle("book title1"));
//			System.out.println(MySQL.insertGenre("genre1"));
//			
////			DocumentsXMLParser parser = new DocumentsXMLParser();
////			parser.parseFile("res/dblp-data-small/final-data.xml");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}
}
