package Helpers;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	private PreparedStatement pstmt, pstmt2, pstmt3;
	private ResultSet rs;

	public Movies() {
		try {
			conn = MySQL.getInstance().getConnection();

			// Prepare queries for a movies stars & genres.
			pstmt2 = conn.prepareStatement("SELECT stars.* FROM stars,stars_in_movies WHERE stars.id=stars_in_movies.star_id AND movie_id=?");
			pstmt3 = conn.prepareStatement("SELECT genres.* FROM genres,genres_in_movies WHERE genres.id=genres_in_movies.genre_id AND movie_id=?");
		
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Movie[] getAllMoviesByGenre(String genre) {
		try {
			conn = MySQL.getInstance().getConnection();

			System.out.println("Trying Fetch");
			pstmt = conn.prepareStatement("SELECT * FROM movies AS m, genres_in_movies AS gim, genres AS g " +
				"WHERE m.id = gim.movie_id AND gim.genre_id = g.id AND g.name = ?");
			pstmt.setString(1,genre);
			rs = pstmt.executeQuery();
			
			// Return a simple array of `Movie` Objects.
			List<Movie> movieList = getMovieList();
			return movieList.toArray(new Movie[movieList.size()]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Movie[] getAllMoviesByTitle(String title) {
		try {
			conn = MySQL.getInstance().getConnection();

			System.out.println("Trying Fetch");
			pstmt = conn.prepareStatement("SELECT * FROM movies AS m WHERE m.title LIKE ?");
			pstmt.setString(1,title+"%");
			rs = pstmt.executeQuery();

			// Return a simple array of `Movie` Objects.
			List<Movie> movieList = getMovieList();
			return movieList.toArray(new Movie[movieList.size()]);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Movie[] getAllMoviesBySearch(String title, String year, String director, String star_fn, String star_ln) {
		try {
			conn = MySQL.getInstance().getConnection();
			Statement statement = conn.createStatement();
			
			String query = "SELECT m.* FROM movies AS m, stars_in_movies AS sim, stars AS s WHERE m.id=sim.movie_id AND s.id=sim.star_id";
			if(title != null)
				query += " AND (m.title LIKE \'" + title + "%\' OR m.title LIKE \'%" + title + "\' or m.title LIKE \'%" + title + "%\')";
			if(year != null)
				query += " AND (m.year LIKE \'" + year + "%\' OR m.year LIKE \'%" + year + "\' or m.year LIKE \'%" + year + "%\')";
			if(director != null)
				query += " AND (m.director LIKE \'" + director + "%\' OR m.director LIKE \'%" + director + "\' or m.director LIKE \'%" + director + "%\')";
			if(star_fn != null)
				query += " AND (s.first_name LIKE \'" + star_fn + "%\' OR s.first_name LIKE \'%" + star_fn + "\' or s.first_name LIKE \'%" + star_fn + "%\')";
			if(star_ln != null)
				query += " AND (s.last_name LIKE \'" + star_ln + "%\' OR s.last_name LIKE \'%" + star_ln + "\' or s.last_name LIKE \'%" + star_ln + "%\')";
			
			rs = statement.executeQuery(query);

			List<Movie> movieList = getMovieList();
			statement.close();
			return movieList.toArray(new Movie[movieList.size()]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Movie> getMovieList() throws SQLException {
		ArrayList<Movie> resultSet = new ArrayList<Movie>();

		if (!rs.first()) {
			System.out.println("No Movies");
		} else {
			System.out.println("Found Some:");

			for(; !rs.isAfterLast(); rs.next()) {
				Movie m = new Movie(); 
				m.id 		= rs.getInt("id");
				m.title 	= rs.getString("title");
				m.director	= rs.getString("director");
				m.year 		= rs.getInt("year");
				m.banner_url  = rs.getString("banner_url");
				m.trailer_url = rs.getString("trailer_url");

				pstmt2.setInt(1,m.id);
				ResultSet rs2 = pstmt2.executeQuery();
				if(rs2.last()) {
					int i = 0;
					m.stars = new Star[rs2.getRow()];
					Star s = null;
					rs2.first();
					for(; !rs2.isAfterLast(); rs2.next()) {
						s = new Star();
						s.id 			= rs2.getInt("id");
						s.first_name 	= rs2.getString("first_name");
						s.last_name 	= rs2.getString("last_name");
						s.dob			= rs2.getDate("dob").toString();
						s.photo_url		= rs2.getString("photo_url");
						s.movies 		= new Movie[0];
						m.stars[i++] = s;
					}
				} else {
					System.out.println("no stars");
					m.stars = new Star[0];
				}

				pstmt3.setInt(1,m.id);
				rs2 = pstmt3.executeQuery();
				if(rs2.last()) {
					int i = 0;
					m.genres = new Genre[rs2.getRow()];
					Genre g = null;
					rs2.first();
					for(; !rs2.isAfterLast(); rs2.next()) {
						g = new Genre();
						g.genre_id 	= rs2.getInt("id");
						g.name 		= rs2.getString("name");
						m.genres[i++] = g;
					}
				} else {
					System.out.println("no genres");
					m.genres = new Genre[0];
				}
				resultSet.add(m);
			}
		}
		rs.close();
		conn.close();
		return resultSet;
	}
}