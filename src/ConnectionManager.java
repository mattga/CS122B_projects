import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
	private static final String jdbcDriver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost/moviedb";
	
	private String getURL(String user, String pass) {
		return url + "?user=" + user + "&password=" + pass;
	}
	
	public Statement connect(String user, String pass) {
		Statement statement = null;
		try {
			Class.forName(jdbcDriver);
			Connection con = DriverManager.getConnection(getURL(user, pass));
			statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error occurred: " + e.getMessage() + " (" + e.getErrorCode() + ")\n");
		}
		return statement;
	}
}
