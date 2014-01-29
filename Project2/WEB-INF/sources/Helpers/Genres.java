package Helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Types.Genre;
import Types.Movie;
import Types.Star;

public class Genres {
	private Connection conn;
	private Statement statement;
	private PreparedStatement movieGenresPStmt;
	private ResultSet rs;

	public Genres() {
		try {
			conn = MySQL.getInstance().getConnection();
			// Prepare queries for a movies stars & genres.
			movieGenresPStmt = conn.prepareStatement("SELECT DISTINCT genres.* FROM genres");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Genre[] getAllGenres() {
		try {
			rs = movieGenresPStmt.executeQuery();
			List<Genre> genreList = getGenreList();
			return genreList.toArray(new Genre[genreList.size()]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Genre> getGenreList() throws SQLException {
		ArrayList<Genre> resultSet = new ArrayList<Genre>();

		if (!rs.first()) {
			System.out.println("No Genres");
		} else {
			System.out.println("Found Some Genres:");

			for(; !rs.isAfterLast(); rs.next()) {
				Genre g = new Genre(); 
				g.genre_id 	= rs.getInt("id");
				g.name 		= rs.getString("name");
				resultSet.add(g);
			}
		}
		rs.close();
		conn.close();
		return resultSet;
	}
}
