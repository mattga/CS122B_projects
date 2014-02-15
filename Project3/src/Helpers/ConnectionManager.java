package Helpers;
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

	public Connection getConnection() {
		return con;
	}
	
	public Statement getStatement() {
		return stmt;
	}

	public boolean createStoredProcedure() throws SQLException {
		if(stmt == null)
			return false;
		
		String addMovieProc =
				"CREATE PROCEDURE moviedb.add_movie (title varchar(100), " +
						"year int, " +
						"director varchar(100), " +
						"star_fn varchar(50), " +
						"star_ln varchar(50), " +
						"genre_name varchar(32)) " +
				"BEGIN " +
					"SET @genre_id = -1; " +
					"SET @star_id = -1; " +
					"SET @movie_id = -1; " +
					
					"SELECT DISTINCT max(id) INTO @genre_id FROM genres " + 
						"WHERE name=genre_name; " +
					"IF @genre_id < 0 THEN " +
						"SELECT \"Genre does not exist. Creating...\"; " +
						"INSERT INTO genres (name) VALUES (genre_name); " +
						"SET @genre_id = LAST_INSERT_ID(); " +
						"SELECT CONCAT(\"Genre created with id \",@genre_id); " +
					"ELSE " +
						"SELECT CONCAT(\"Genre already exists with id \",@genre_id); " +
					"END IF; " +
					
					"SELECT DISTINCT max(id) INTO @star_id FROM stars " + 
						"WHERE first_name=star_fn AND last_name=star_ln; " +
					"IF @star_id < 0 THEN " +
						"SELECT \"Star does not exist. Creating...\"; " +
						"INSERT INTO stars (first_name, last_name) VALUES (star_fn, star_ln); " +
						"SET @star_id = LAST_INSERT_ID(); " +
						"SELECT CONCAT(\"Star created with id \",@star_id); " +
					"ELSE " +
						"SELECT CONCAT(\"Star already exists with id \",@star_id); " +
					"END IF; " +
					
					"SELECT DISTINCT max(id) INTO @movie_id FROM movies " + 
						"WHERE title=title AND year=year AND director=director; " +
					"IF @movie_id < 0 THEN " +
						"SELECT \"Movie does not exist. Creating...\"; " +
						"INSERT INTO movies (title,year,director) VALUES (title,year,director); " +
						"SET @movie_id = LAST_INSERT_ID(); " +
						"SELECT CONCAT(\"Movie created with id \",@movie_id); " +
					"ELSE " +
						"SELECT CONCAT(\"Movie already exists with id \",@movie_id); " +
					"END IF; " +
					
					"INSERT INTO stars_in_movies VALUES (@star_id, @movie_id); " +
					"SELECT \"Star (id @star_id) and movie (id @movie_id) linked\"; " +
					"INSERT INTO genres_in_movies VALUES (@genre_id, @movie_id); " +
					"SELECT \"Genre (id @genre_id) and movie (id @movie_id) linked\"; " +
				"END ";

		stmt.executeUpdate("DROP PROCEDURE add_movie");
		stmt.executeUpdate(addMovieProc);
		
		return true;
	}
}
