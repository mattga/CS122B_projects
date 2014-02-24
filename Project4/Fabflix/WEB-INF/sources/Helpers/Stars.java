package Helpers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import Types.Genre;
import Types.Movie;
import Types.Star;

/**
 * Use this class to encapsulate behavior of queries 
 * related to Stars. This is a stand alone class that is called
 * from jsp files. 
 * 
 * Use as a Java Bean inside JSP files:
 * {@code <jsp:useBean name="REF_NAME"  class="Stars"/>}
 */
public class Stars {	
	private Connection conn;
	private PreparedStatement starPStmt, starMoviesPStmt;

	/**
	 * Set up the Database Connection.
	 */
	public Stars() {
		try {
			conn = MySQL.getInstance().getConnection();

			starPStmt = conn.prepareStatement("SELECT * FROM stars WHERE id= ? ");
			starMoviesPStmt = conn.prepareStatement("SELECT DISTINCT m.* FROM stars AS s, movies AS m, stars_in_movies AS sim WHERE s.id=sim.star_id AND sim.movie_id=m.id AND s.id=?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return a String-Array of the Genres.
	 * 
	 * @return String[]
	 */
	public Genre[] getGenres() {
		return null;
	}
	
	/**
	 * Returns a Movie-Array of the movies.
	 * @return Movie[]
	 */
	public Star[] getAllStars() {
		ArrayList<Star> stars = new ArrayList<Star>();
		try {
			ResultSet rs = conn.prepareStatement("SELECT * FROM stars ORDER BY `last_name` ASC;").executeQuery();
			if(rs.first()) {
				for(;!rs.isAfterLast(); rs.next()){
					Star s = new Star();
					s.id = rs.getInt("id");
					s.first_name	= rs.getString("first_name");
					s.last_name		= rs.getString("last_name");
					stars.add(s);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stars.toArray(new Star[stars.size()]);
	}
	
	public HashMap<String, LinkedList<Star>> OrderIntoABCGroups(Star[] stars) {
		String groups = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		HashMap<String, LinkedList<Star>> map = new HashMap<>();
		for (char c : groups.toCharArray()) {
			LinkedList<Star> cs = new LinkedList<>();
			for (int i = 0; i < stars.length; i++) {
				if (stars[i].last_name.startsWith(String.valueOf(c))) {
					cs.add(stars[i]);
				}
			}
			map.put(String.valueOf(c), cs);
		}
		return map;
	}
	
	public Star getStar(int id) {
		try {
			conn = MySQL.getInstance().getConnection();

			starPStmt.setInt(1,id);
			ResultSet rs = starPStmt.executeQuery();
			Star s = null;

			if(rs.first()) {
				s = new Star();
				s.id 			= rs.getInt("id");
				s.first_name	= rs.getString("first_name");
				s.last_name		= rs.getString("last_name");
				s.dob			= rs.getDate("dob").toString();
				s.photo_url		= rs.getString("photo_url");

				starMoviesPStmt.setInt(1,s.id);
				ResultSet rs2 = starMoviesPStmt.executeQuery();
				if(rs2.last()) {
					int i = 0;
					s.movies = new Movie[rs2.getRow()];
					Movie m = null;
					for(rs2.first(); !rs2.isAfterLast(); rs2.next()) {
						m = new Movie(); 
						m.id 			= rs2.getInt("id");
						m.title 		= rs2.getString("title");
						m.year 			= rs2.getInt("year");
						s.movies[i++]	= m;
					}
					rs2.close();
				} else {
					System.out.println("no movies");
					s.movies = new Movie[0];
				}
			}
			rs.close();
			conn.close();

			return s;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
