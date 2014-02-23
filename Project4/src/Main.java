
public class Main {
	public static void main (String [] args) {
		ArticleXMLParser sax = new ArticleXMLParser("res/dblp-data-small/final-data.xml");
		sax.runExample();
	}
}
