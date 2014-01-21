package Helpers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Singleton Implementation of the MySQL handle that is
 * used to query the database.
 * 
 * Use the static {@code MySQL.getInstance()} method to 
 * retreive an instance and a connection to the database.
 */
public class MySQL {

	private Connection _connection;
	private Statement _statement;
	/**
	 * Holds Single Instance of MySQL, that is shared
	 * at runtime.
	 */
	private static MySQL _instance;
	
	/**
	 * Singleton Design Pattern to force a single
	 * instance of this class.
	 */
	private MySQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			_connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?user=moviedb&password=tiger");
			_statement = _connection.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Retreive instance of class, if one does not exist, 
	 * then create one and save for future queris.
	 * @return MySQL
	 */
	public static MySQL getInstance() {
		if (_instance == null) 
			_instance = new MySQL();
		return _instance;
	}
	
	public Connection getConnection() {
		return _connection;
	}
	
	public Statement getStatement() {
		return _statement;
	}
}
