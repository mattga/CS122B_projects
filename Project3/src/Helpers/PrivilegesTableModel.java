package Helpers;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class PrivilegesTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum Resource {
		Databases,
		Tables,
		Columns,
		Procedures,
		None
	};
	private Resource res;
	private ResultSet rs, rs2;
	private ResultSetMetaData rsmd, rsmd2;
	private PreparedStatement globalStmt, userStmt, dbStmt, tableStmt, colStmt;
	private Map.Entry<String,String>[] privileges;

	public PrivilegesTableModel(Connection con) {
		this.res = Resource.None;
		try {
			// Prepare Statements
			this.userStmt = con.prepareStatement("SELECT Host,User from mysql.user");
			this.globalStmt = con.prepareStatement("SELECT * FROM mysql.user WHERE host=? AND user=?");
			this.dbStmt = con.prepareStatement("SELECT * FROM mysql.db WHERE host=? AND user=?");
			this.tableStmt = con.prepareStatement("SELECT * FROM mysql.tables_priv WHERE host=? AND user=?");
			this.colStmt = con.prepareStatement("SELECT * FROM mysql.columns_priv WHERE host=? AND user=?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setResource(Resource res, int userRow) throws SQLException {
		if(userRow < 0)
			return;

		ResultSet userRS = userStmt.executeQuery("SELECT Host,User from mysql.user");
		userRS.absolute(userRow+1);
		String host = userRS.getString("host");
		String user = userRS.getString("user");

		globalStmt.setString(1, host);
		globalStmt.setString(2, user);
		rs2 = globalStmt.executeQuery();
		rsmd2 = rs2.getMetaData();

		switch(res) {
		case Databases:
			dbStmt.setString(1, host);
			dbStmt.setString(2, user);
			rs = dbStmt.executeQuery();
			privileges = PrivilegeTablesParser.parseDatabasePrivileges(rs2, rs);
			break;
		case Tables:
			tableStmt.setString(1, host);
			tableStmt.setString(2, user);
			rs = tableStmt.executeQuery();
			break;
		case Columns:
			colStmt.setString(1, host);
			colStmt.setString(2, user);
			rs = colStmt.executeQuery();
			break;
		case Procedures:
			dbStmt.setString(1, host);
			dbStmt.setString(2, user);
			rs = dbStmt.executeQuery();
			privileges = PrivilegeTablesParser.parseProcedurePrivileges(rs2, rs);
			break;
		case None:
			break;
		}

		if(rs != null)
			rsmd = rs.getMetaData();

		this.res = res;
		this.fireTableStructureChanged();
	}

	public int getColumnCount() {
		switch(res) {
		case Databases: 
			return 2;
		case Tables:
			return 3;
		case Columns: 
			return 4;
		case Procedures: 
			return 2;
		case None:
			return 1;
		}

		return 0;
	}

	@Override
	public int getRowCount() {
		if(res == Resource.None)
			return 1;
		
		if(privileges != null)
			return privileges.length;
		
		System.out.println("Null privileges!!!");
		return 0;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(res) {
		case Databases:
			switch(columnIndex) {
			case 0: return "Database";
			case 1: return "Privileges";
			}
			break;
		case Tables:
			switch(columnIndex) {
			case 0: return "Database";
			case 1: return "Table";
			case 2: return "Privileges";
			}
			break;
		case Columns:
			switch(columnIndex) {
			case 0: return "Database";
			case 1: return "Table";
			case 2: return "Column";
			case 3: return "Privileges";
			}
			break;
		case Procedures:
			switch(columnIndex) {
			case 0: return "Database";
			case 1: return "Procedure Privileges";
			}
			break;
		case None:
			return "";
		}

		return "NONAME";
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			if(rs != null && rs.first())
				rs.absolute(rowIndex+1);

			switch(res) {
			case Databases:
				switch(columnIndex) {
				case 0:
					return privileges[rowIndex].getValue();
				case 1:
					return privileges[rowIndex].getKey();
				}
			case Tables:
				break;
			case Columns:
				break;
			case Procedures:
				switch(columnIndex) {
				case 0:
					return privileges[rowIndex].getValue();
				case 1:
					return privileges[rowIndex].getKey();
				}
			case None:
				return "Select a user and resource to display privileges for...";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "NOVALUE";
	}
}
