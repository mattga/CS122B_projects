import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
	private static final String jdbcDriver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost/moviedb?zeroDateTimeBehavior=convertToNull";
	private static Connection con;
	private static Statement stmt;

	private String getURL(String user, String pass) {
		return url + "&user=" + user + "&password=" + pass;
	}

	public Statement connect(String user, String pass) {
		if(stmt == null)
			try {
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(getURL(user, pass));
				stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("Error occurred: " + e.getMessage() + " (" + e.getErrorCode() + ")\n");
			}
		return stmt;
	}

	public boolean createStoredProcedure() throws SQLException {
		if(stmt == null)
			return false;
		
		String addMovieProc = "delimiter $$" +
				"CREATE PROCEDURE moviedb.add_movie (title varchar(100)," + 
													"year int," +
													"director varchar(100)," +
													"star_fn varchar(50)," +
													"star_ln varchar(50)," +
													"genre_name varchar(32))" +
				"BEGIN" +
					"INSERT INTO genres (name)" +
					"SELECT genre_name FROM DUAL" +
					"WHERE NOT EXISTS (SELECT * FROM genres" +
						"WHERE name=genre_name);" +
					"SET @genre_id = LAST_INSERT_ID();" +
					//TODO: add dynamic selects
					"INSERT INTO stars (first_name, last_name)" +
					"SELECT star_fn, star_ln FROM DUAL" +
					"WHERE NOT EXISTS (SELECT * FROM stars" +
						"WHERE first_name=star_fn AND last_name=star_ln);" +
					"SET @star_id = LAST_INSERT_ID();" +

					"INSERT INTO movies (title, year) VALUES (title, year);" +
					"SET @movie_id = LAST_INSERT_ID();" +

					"INSERT INTO stars_in_movies VALUES (@star_id, @movie_id);" +
					"INSERT INTO genres_in_movies VALUES (@genre_id, @movie_id);" +
				"END$$" +
				"delimiter ;";

		stmt.executeUpdate(addMovieProc);
		
		return true;
	}
}
