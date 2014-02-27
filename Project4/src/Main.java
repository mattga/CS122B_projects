
public class Main {
	public static void main (String [] args) {

		// Run Article Parser...
		long start = System.currentTimeMillis();
		ArticleXMLParser sax = new ArticleXMLParser("res/dblp-data-big/dblp-data.xml");
		sax.parse();
		long parsingend = System.currentTimeMillis();
		System.out.println("Parsing time: " + (parsingend-start) + " ms");

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
