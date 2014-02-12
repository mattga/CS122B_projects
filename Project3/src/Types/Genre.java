package Types;

import java.util.Scanner;

/**
 * Object Represents a Genre.
 * Use inside looping structure to increase readability.
 * 
 * Object fields are equivalent name to table-coulmn names.
 * 
 */
public class Genre {
	public int genre_id;
	public String name;
	
	/**
	 * Populate the current Genre Object With information from STDIN.
	 * @param Scanner s
	 */
	public void getInfoFromStdIn(Scanner s, StringBuilder errBuff){
		
		while(name == null || name.equals("")) {
			System.out.println("What is the name of the Genre?");
			name = s.nextLine();
			
			if (name.equals(""))
				System.out.println("Sorry, You must provide a genre name...");
		}
		
		
	}
}