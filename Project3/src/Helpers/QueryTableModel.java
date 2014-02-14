package Helpers;

import javax.swing.table.AbstractTableModel;
import javax.naming.CommunicationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

public class QueryTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rowCount;
	private String query;
	private Connection connection;
	private Statement statement;
	private ResultSet rs;
	private ResultSetMetaData rsmd;
	
	public QueryTableModel (String driver, String url, String query) throws SQLException, CommunicationException, ClassNotFoundException{
		this.query = query;
		Class.forName(driver);
		connection = DriverManager.getConnection(url);
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = statement.executeQuery(query);
		rsmd = rs.getMetaData();
		rs.last();
		rowCount = rs.getRow();
	}

	public QueryTableModel (Connection con, String query) throws SQLException, CommunicationException, ClassNotFoundException{
		this.query = query;
		connection = con;
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		rs = statement.executeQuery(query);
		rsmd = rs.getMetaData();
		rs.last();
		rowCount = rs.getRow();
	}
	
	public boolean executeQuery() {
		return executeQuery(query);
	}
	
	public boolean executeQuery(String query) {
		try {
			rs = statement.executeQuery(query);
			rsmd = rs.getMetaData();
			rs.last();
			rowCount = rs.getRow();
			rs.first();
		} catch (SQLException e) {
			rowCount = -1;
			e.printStackTrace();
			return false;
		}
		this.fireTableDataChanged();
		return true;
	}
	
	public boolean execute(String query) {
		try {
			return statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.fireTableDataChanged();
		return false;
	}
	
	public int executeUpdate(String query) {
		try {
			return statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.fireTableDataChanged();
		return -1;
	}
	
	public int getColumnCount(){
		try {
			return rsmd.getColumnCount();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return -1;
	}
	
	// columnIndex for 0...N columns
	public String getColumnName(int columnIndex) {
		try {
			return rsmd.getColumnName(columnIndex+1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return "NONAME";
	}

	public int getRowCount() {
		return rowCount;
	}

	// rowIndex/columnIndex for 0...N rows/columns
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			rs.absolute(rowIndex+1);  
			return rs.getObject(columnIndex+1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
}
