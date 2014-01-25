package Helpers;
import Types.*;

/**
 * Use this class to encapsulate behavior of queries 
 * related to Stars. This is a stand alone class that is called
 * from jsp files. 
 * 
 * Use as a Java Bean inside JSP files:
 * {@code <jsp:useBean name="REF_NAME"  class="Stars"/>}
 */
public class Stars {	
	/**
	 * Set up the Database Connection.
	 */
	public Stars() {

	}
	
	/**
	 * Return a String-Array of the Genres.
	 * 
	 * @return String[]
	 */
	public Star[] getGenres() {
		return null;
	}
	
	/**
	 * Returns a Movie-Array of the movies.
	 * @return Movie[]
	 */
	public Star[] getAllStars() {
		return null;
	}
}
