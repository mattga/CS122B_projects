package Types;

import java.util.Scanner;


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
	public int cartQuantity;
	
	
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
	
	public void getFromStdIn(Scanner s) {
		boolean requiredFieldsComplete = false;
		
		while(!requiredFieldsComplete){
			
			if (title == null || title.equals("")) {
				System.out.print("What is the title of the movie? ");
				title = s.nextLine();
			}
			
			if (year < 1000 || year > 9999) {
				System.out.print("What is the year of the movie? ");
				year = Integer.parseInt(s.nextLine());
			}
			
			if (director == null || director.equals("")) {
				System.out.print("What is the director of the movie? ");
				director = s.nextLine();
			}
			
			if (banner_url == null) {
				System.out.print("What is the banner url of the movie? ");
				banner_url = s.nextLine();
			}
			
			if (trailer_url == null) {
				System.out.print("What is the trailer url of the movie? ");
				trailer_url = s.nextLine();
			}
			
			if (genres == null || genres.length < 1) {
				System.out.print("What is the genre of the movie? ");
				genres = new Genre[1];
				genres[0] = new Genre();
				genres[0].name = s.nextLine();
			}
			
			if (stars == null || stars.length < 1) {
				System.out.print("Who is one star in the movie? ");
				String name = s.nextLine();
				stars = new Star[1];
				stars[0] = new Star();
				stars[0].first_name = name.split(" ")[0];
				stars[0].last_name = name.split(" ")[1];
			}
			
			requiredFieldsComplete = !title.equals("") && !(year < 1000 || year > 9999) && !director.equals("");
			
		}
		
//		// This is possibly a bad implementation with requiring the user to pre-specify the number of genres.
//		System.out.println("\nHow many genres does the movie have?");
//		int genreCount = s.nextInt();
//		if (genreCount > 0)
//			genres = new Genre[genreCount];
//		
//		for (int i = 0; i < genreCount; i++) {
//			genres[i] = new Genre();
//			genres[i].getInfoFromStdIn(s, null);
//		}
//		
//		// This is possibly a bad implementation with requiring the user to pre-specify the number of stars.
//		System.out.println("\nHow many stars does the movie have?");
//		int starCount = s.nextInt();
//		if (starCount > 0)
//			stars = new Star[starCount];
//		
//		StringBuilder errBuffer = new StringBuilder();
//		for (int i = 0; i < starCount; i++) {
//			stars[i] = new Star();
//			do { // Some Error Checking to make sure we are getting right data for stars.
//				errBuffer.setLength(0);
//				stars[i].getInfoFromStdIn(s, errBuffer);
//				System.out.println(errBuffer.toString() +"\n");
//			} while(!errBuffer.toString().equals(""));
//		}
	}
}