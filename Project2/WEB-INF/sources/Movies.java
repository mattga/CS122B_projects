import Types.*;

/**
 * Use this class to encapsulate behavior of queries 
 * related to Movies. This is a stand alone class that is called
 * from jsp files. 
 * 
 * Use as a Java Bean inside JSP files:
 * {@code <jsp:useBean name="REF_NAME"  class="Movies"/>}
 */
public class Movies {	
	private MySQL _sql;
	/**
	 * Set up the Database Connection.
	 */
	public Movies(){
		_sql = MySQL.getInstance();
	}
	
	/**
	 * Return a String-Array of the Genres.
	 * 
	 * @return String[]
	 */
	public String[] getGenres() {
		return null;
	}
	
	/**
	 * Returns a Movie-Array of the movies.
	 * @return Movie[]
	 */
	public Movie[] getAllMovies() {
		return null;
	}
	
}
