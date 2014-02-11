package Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Singleton Implementation of the MySQL handle that is
 * used to query the database.
 * 
 * Use the static {@code MySQL.getInstance()} method to 
 * retreive an instance and a connection to the database.
 */
public class MySQL {

	private BoneCP connectionPool = null;
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

			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl("jdbc:mysql://localhost:3306/moviedb"); // jdbc url specific to your database, eg jdbc:mysql://127.0.0.1/yourdb
			config.setUsername("testuser"); 
			config.setPassword("testpass");
			config.setMinConnectionsPerPartition(5);
			config.setMaxConnectionsPerPartition(10);
			config.setPartitionCount(1);
			connectionPool = new BoneCP(config); // setup the connection pool

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
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Statement getStatement() {
		try {
			return connectionPool.getConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
