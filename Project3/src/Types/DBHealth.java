package Types;

import Helpers.UserSQLQuery;

/**
 * Genereate a report about the health of the database.
 */
public class DBHealth {
	
	public DBHealth(UserSQLQuery usql) {
	}
	
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
	 */
	private static final String EXPIRED_CC = "SELECT * " +
											 "FROM  `creditcards` " +
											 "WHERE  `expiration` <  ?"; // Preg/Rep or Prep. To Specify
	
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
	 */
	private static final String DOB_OUT_OF_RANGE = "SELECT *  FROM  `stars`  WHERE  `dob` <  ? OR `dob` > ?";
	
	/**
	 * Customer email has no @sign
	 */
	private static final String INVALID_EMAIL = "SELECT * FROM  `customers` WHERE  `email` NOT LIKE  '%@%'";

}
