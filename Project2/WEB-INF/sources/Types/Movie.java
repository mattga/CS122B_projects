package Types;

/**
 * Object Represents a Movie.
 * Use inside looping structure to increase readability.
 * 
 * Object fields are equivalent name to table-column names.
 * 
 */
public class Movie {
	// Fields in `movies` table
	public int id;
	public String title;
	public int year;
	public String director;
	public String banner_url;
	public String trailer_url;
	
	
	/**
	 * Only populate this field if it is logical that on the page
	 * Movies have genres.
	 * 
	 * Vice-Versa in Genre data structure having movies
	 */
	public Genre[] genres;
	
	/**
	 * Only populate this field if it is logical that on the page
	 * Movies have stars.
	 * 
	 * Vice-Versa in Star data structure having Movies
	 */
	public Star[] stars;
}
