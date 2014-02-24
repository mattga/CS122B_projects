package Types;

/**
 * Object Represents a Star.
 * Use inside looping structure to increase readability.
 * 
 * Object fields are equivalent name to table-column names.
 * 
 */
public class Star {
	public int id;
	public String first_name;
	public String last_name;
	public String dob;
	public String photo_url;
	
	/**
	 * Only populate this field if it is logical that on the page
	 * Stars have movies.
	 * 
	 * Vice-Versa in Movie data structure having Stars
	 */
	public Movie[] movies;
}
