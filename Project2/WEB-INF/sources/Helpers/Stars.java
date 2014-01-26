package Helpers;
import Types.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		return null;
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
