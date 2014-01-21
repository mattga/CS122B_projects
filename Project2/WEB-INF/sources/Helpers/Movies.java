package Helpers;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Types.Genre;
import Types.Movie;
import Types.Star;

/**
 * Stand-Alone Class used to fetch movies.
 *
 */
public class Movies {
	private Connection conn;
	private Statement statement;
	private PreparedStatement pstmt;

	public Movies() {
		
	}
	
	public Movie[] getAllMoviesByGenre(String genre) {
		ArrayList<Movie> resultSet = new ArrayList<Movie>();
		
		try {
			conn = MySQL.getInstance().getConnection();
			statement = conn.createStatement();

			System.out.print("Trying Fetch");
			pstmt = conn.prepareStatement("SELECT * FROM movies AS m, genres_in_movies AS gim, genres AS g " +
				"WHERE m.id = gim.movie_id AND gim.genre_id = g.id AND g.name = ?");
			pstmt.setString(1,genre);
			ResultSet rs = pstmt.executeQuery();
			
			if (!rs.first()) {
				System.out.print("No Movies");
				return new Movie[0]; // No Movies
			} else {
				System.out.print("Found Some:");
				for(; !rs.isAfterLast(); rs.next()) {
					Movie m = new Movie(); 
					m.id 		= rs.getInt("id");
					m.title 	= rs.getString("title");
					m.director 	= rs.getString("director");
					m.year 		= rs.getInt("year");
					m.trailer_url = rs.getString("trailer_url");
					m.genres    = new Genre[0];
					m.stars		= new Star[0];
					
					resultSet.add(m);
				}
			}
			rs.close();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Return a simple array of `Movie` Objects.
		return resultSet.toArray(new Movie[resultSet.size()]);
	}
}
