package Types;

import java.util.Scanner;

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
	
	public Star getInfoFromStdIn(Scanner s, StringBuilder errBuff) {
		System.out.print("\nWhat's the Star's First Name? ");
		first_name = s.nextLine();
		
		System.out.print("\nWhat's the Star's Last Name? ");
		last_name = s.nextLine();
		
		System.out.print("\nWhats the Star's DOB(YYYY-MM-DD)? ");
		dob = s.nextLine();
		
		System.out.print("\nPhoto URL: ");
		photo_url = s.nextLine();

		if (first_name.equals("") && last_name.equals("")) {
			System.out.println("Missing name");
			errBuff.append("Please provide the star's name.\n");
		}
		if (!dob.equals("") && !dob.matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")) {
			System.out.println("Date Mismatch");
			errBuff.append("Date of Birth must be formatted YYYY-MM-DD.\n");
		}
		
		System.out.print("\n");
		
		return this;
	}
}