package Types;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Genereate a report about the health of the database.
 */
public class DBHealth {
	private Statement _s;
	private StringBuilder str = new StringBuilder();
	public DBHealth(Statement s) {
		_s = s;
	}
	
	
	
	public String generateReport(String savePath) {
		if (savePath == null) {
			savePath = "/DB_Health_Report.html";
		}
		
		// Gather the string up....
		StringBuilder buffer = new StringBuilder();
		
		try {
			buffer.append(processDOBOutOfRange(_s.executeQuery(String.format(DOB_OUT_OF_RANGE,
					new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()),
					"1900-01-01"))));
			buffer.append(processExpiredCC(_s.executeQuery(String.format(EXPIRED_CC, 
					new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())))));
			buffer.append(processDuplicateGenres(_s.executeQuery(DUPLICATE_GENRES)));
			buffer.append(processDuplicateMovies(_s.executeQuery(DUPLICATE_MOVIE)));
			buffer.append(processDuplicateStars(_s.executeQuery(DUPLICATE_STARS)));
			buffer.append(processInvalidEmails(_s.executeQuery(INVALID_EMAIL)));
			buffer.append(processNoGenreMovies(_s.executeQuery(NO_GENRE_MOVIES)));
			buffer.append(processNoMovieGenres(_s.executeQuery(NO_MOVIE_GENRES)));
			buffer.append(processNoMovieStars(_s.executeQuery(NO_MOVIE_STARS)));
			buffer.append(processNoStarMovies(_s.executeQuery(NO_STAR_MOVIES)));
			buffer.append(processStarMissingName(_s.executeQuery(STARS_MISSING_NAME)));
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("An Error Occurred While Running the DB Health Options...");
		}		
		
		// Write the report to a file....
		Writer w = null;
		try {
			w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(savePath), "utf-8"));
			w.write("<html><head></head><body>");
			w.write(buffer.toString());
			w.write("</body></html>");
			w.close();
		} catch (IOException e) {
			System.out.println("Trouble Creating File in: "+ savePath);
		}
		
		return buffer.toString();
	}
	
	
	
	private String processNoStarMovies(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Movies with No Stars</h1>");
		try {
			if (rs.first()) {
				String id;
				String title;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Movie ID", "Movie Title"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					title = rs.getString("title");
					str.append(String.format(TABLE_ROW_2, id, title ));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			System.out.println(SYS_ADMIN_ERROR);
		}
		
		return str.toString();
	}
	
	
	
	private String processNoMovieStars(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Stars with No Movies</h1>");
		try {
			if (rs.first()) {
				String id;
				String name;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Star ID", "Star Name"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					name = rs.getString("first_name") + rs.getString("last_name");
					str.append(String.format(TABLE_ROW_2, id, name));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		
		return str.toString();
	}
	
	
	
	private String processNoMovieGenres(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Genres with No Movies</h1>");
		try {
			if (rs.first()) {
				String id;
				String name;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Genre ID", "Genre Name"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					name = rs.getString("name");
					str.append(String.format(TABLE_ROW_2, id, name));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	
	private String processNoGenreMovies(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Movies with No Genres</h1>");
		try {
			if (rs.first()) {
				String id;
				String title;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Movie ID", "Movie Name"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					title = rs.getString("title");
					str.append(String.format(TABLE_ROW_2, id, title));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	
	private String processStarMissingName(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Stars Missing a First/Last Name</h1>");
		try {
			if (rs.first()) {
				String id;
				String title;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Star ID", "First/Last Name On File"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					title = rs.getString("first_name") + rs.getString("last_name");
					str.append(String.format(TABLE_ROW_2, id, title));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	/// SQL ERROR: no table creditcards......wtf?
	private String processExpiredCC(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Expired Credit Cards</h1>");
		try {
			if (rs.first()) {
				String id;
				String title;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Card ID", "First + Last + Expiration"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					title = rs.getString("first_name") +" "+ rs.getString("last_name") +" "+ rs.getString("expiration");
					str.append(String.format(TABLE_ROW_2, id, title));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	
	private String processDuplicateMovies(ResultSet rs) {	
		str.setLength(0);
		HashMap<String, ArrayList<Integer>> movies = new HashMap<>();
		str.append("<h1>Duplicate Movies</h1>");
		try {
			ArrayList<Integer> a = null;
			if (rs.first()) {
				// Get the values
				String key;
				Integer val;
				for(;!rs.isAfterLast(); rs.next()) {
					key = rs.getString("title");
					if (movies.containsKey(key)) {
						movies.get(key).add(rs.getInt("id"));
					} else {
						a = new ArrayList<Integer>();
						a.add(rs.getInt("id"));
						movies.put(key, a);
					}
				}
				
				// Append them to the output
				
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Movie ID", "Movie Title"));
				for (String k : movies.keySet()) {
					a = movies.get(k);
					for(Integer i : a) {
						str.append(String.format(TABLE_ROW_2, i, k));
					}
				}
				str.append("</table>");
				
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	
	private String processDuplicateStars(ResultSet rs) {
		str.setLength(0);
		HashMap<String, ArrayList<Integer>> movies = new HashMap<>();
		str.append("<h1>Duplicate Stars</h1>");
		try {
			ArrayList<Integer> a = null;
			if (rs.first()) {
				// Get the values
				String key;
				Integer val;
				for(;!rs.isAfterLast(); rs.next()) {
					key = rs.getString("first_name") + " " + rs.getString("last_name");
					if (movies.containsKey(key)) {
						movies.get(key).add(rs.getInt("id"));
					} else {
						a = new ArrayList<Integer>();
						a.add(rs.getInt("id"));
						movies.put(key, a);
					}
				}
				
				// Append them to the output
				
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Star ID", "Star Name"));
				for (String k : movies.keySet()) {
					a = movies.get(k);
					for(Integer i : a) {
						str.append(String.format(TABLE_ROW_2, i, k));
					}
				}
				str.append("</table>");
				
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	
	private String processDuplicateGenres(ResultSet rs) {
		str.setLength(0);
		HashMap<String, ArrayList<Integer>> movies = new HashMap<>();
		str.append("<h1>Duplicate Genres</h1>");
		try {
			ArrayList<Integer> a = null;
			if (rs.first()) {
				// Get the values
				String key;
				Integer val;
				for(;!rs.isAfterLast(); rs.next()) {
					key = rs.getString("name");
					if (movies.containsKey(key)) {
						movies.get(key).add(rs.getInt("id"));
					} else {
						a = new ArrayList<Integer>();
						a.add(rs.getInt("id"));
						movies.put(key, a);
					}
				}
				
				// Append them to the output
				
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Genre ID", "Genre Name"));
				for (String k : movies.keySet()) {
					a = movies.get(k);
					for(Integer i : a) {
						str.append(String.format(TABLE_ROW_2, i, k));
					}
				}
				str.append("</table>");
				
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	
	private String processDOBOutOfRange(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Stars with Invalid DOB</h1>");
		try {
			if (rs.first()) {
				String id;
				String title;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Customer ID", "Email"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					title = rs.getString("first_name") +" "+ rs.getString("last_name") +" "+ rs.getString("dob");
					str.append(String.format(TABLE_ROW_2, id, title));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	
	
	private String processInvalidEmails(ResultSet rs) {
		str.setLength(0);
		str.append("<h1>Customers with Invalid Emails</h1>");
		try {
			if (rs.first()) {
				String id;
				String title;
				str.append("<table>");
				str.append(String.format(TABLE_ROW_HEAD_2, "Customer ID", "Email"));
				for(;!rs.isAfterLast(); rs.next()) {
					id = rs.getString("id");
					title = rs.getString("email");
					str.append(String.format(TABLE_ROW_2, id, title));
				}
				str.append("</table>");
			} else {
				str.append("<p>NONE FOUND</p>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(SYS_ADMIN_ERROR);
		}
		return str.toString();
	}
	
	private static final String SYS_ADMIN_ERROR = "SQL EXEPTION -- CONTACT YOUR ADMINISTRATOR"; 
	/**
	 * Template for 2 Items
	 */
	private static final String TABLE_ROW_2 = "<tr>" +
			"<td>%s</td>" +
			"<td>%s</td>" +
			"</tr>";
	
	
	/**
	 * Template Header for 2 items
	 */
	private static final String TABLE_ROW_HEAD_2 = "<tr>" +
			"<th>%s</th>" +
			"<th>%s</th>" +
			"</tr>";
	
	
	/**
	 * Movies without any star.
	 */
	private static final String NO_STAR_MOVIES = "SELECT *  " +
												 "FROM `movies` m " +
												 "WHERE m.`id` NOT IN " +
													"(SELECT `movie_id` FROM `stars_in_movies`);";

	/**
	 * Stars without any movie.
	 */
	private static final String NO_MOVIE_STARS = "SELECT * " +
												 "FROM `stars` " +
												 "WHERE `id` NOT IN " +
												 "(SELECT `star_id` FROM `stars_in_movies`)";
	
	/**
	 * Genres without any movies.
	 */
	private static final String NO_MOVIE_GENRES = "SELECT * " +
												  "FROM `genres` " +
												  "WHERE `id` NOT IN " +
												  "(SELECT `genre_id` FROM `genres_in_movies`)";
	
	/**
	 * Movies without any genres.
	 */
	private static final String NO_GENRE_MOVIES = "SELECT * " +
												  "FROM `movies` " +
												  "WHERE `id` NOT IN " +
												  "(SELECT `movie_id` FROM `genres_in_movies`)";
	
	/**
	 * Stars with no first name or last name.
	 */
	private static final String STARS_MISSING_NAME = "SELECT * " +
													 "FROM `stars` " +
													 "WHERE first_name = '' OR last_name = '';";
	
	/**
	 * Expired customer credit card. Report expired credit cards only if they belong to existing customers.
	 * REQUIRES REPLACEMENT PARAMETERS
	 */
	private static final String EXPIRED_CC = "SELECT cc.* " +
											 "FROM  `creditcards` cc, `customers` cu " +
											 "WHERE  cc.`expiration` <  '%s' AND cu.`cc_id` = cc.`id`";
	
	//  Movie/star/genres that are the same. Make sure you group the IDs of the duplicates in your report. 
	//  Two movies are considered the same if (name, year) match, 
	//  and two stars if (first name, last name, dob) match.
	/**
	 * Duplicate Movies
	 */
	private static final String DUPLICATE_MOVIE = "SELECT * " +
												  "FROM `movies` m " +
												  "INNER JOIN ( SELECT `title`, `year`, COUNT(*) AS dupCount " +
												  				"FROM `movies` " +
												  				"GROUP BY `title`, `year` " +
												  				"HAVING COUNT(*) > 1 ) d " +
												  				"ON m.`title` = d.`title` AND m.`year` = d.`year` " +
												  "ORDER BY d.`dupCount` DESC;";
	/**
	 * Duplicate Stars
	 */
	private static final String DUPLICATE_STARS = "SELECT * " +
												  "FROM `stars` m " +
												  "INNER JOIN ( SELECT `first_name`, `last_name`, `dob`, COUNT(*) AS dupCount " +
												  				"FROM `stars` " +
												  				"GROUP BY `first_name`, `last_name`, `dob` " +
												  				"HAVING COUNT(*) > 1 ) d " +
												  				"ON m.`first_name` = d.`first_name` " +
												  				"AND m.`last_name` = d.`last_name` " +
												  				"AND m.`dob` = d.`dob` " +
												  "ORDER BY d.`dupCount` DESC;";
	/**
	 * Duplicate Genres
	 */
	private static final String DUPLICATE_GENRES = "SELECT * " +
												   "FROM `genres` m " +
												   "INNER JOIN ( SELECT `name`, COUNT(*) AS dupCount " +
												  				"FROM `genres` " +
												  				"GROUP BY `name`" +
												  				"HAVING COUNT(*) > 1 ) d " +
												  				"ON m.`name` = d.`name`" +
												   "ORDER BY d.`dupCount` DESC;";
	
	/**
	 * Birth date > today or year < ~1900. // Use Reg/Exp or Prep Statemtn to replace place holders.
	 * REQUIRES REPLACEMENT PARAMETERS
	 */
	private static final String DOB_OUT_OF_RANGE = "SELECT *  FROM  `stars`  WHERE  `dob` >  '%s' OR `dob` < '%s'";
	
	/**
	 * Customer email has no @sign
	 */
	private static final String INVALID_EMAIL = "SELECT * FROM  `customers` WHERE  `email` NOT LIKE  '%@%'";

}