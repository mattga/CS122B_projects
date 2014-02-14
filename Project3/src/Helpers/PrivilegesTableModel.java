package Helpers;

import java.util.LinkedHashSet;
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

		try {
			rs.last();
			int specRowCount = rs.getRow();
			if (specRowCount < 0)
				return specRowCount;
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
					if(!rs.first())
						return "All";
					else
						return rs.getString("Db");
				case 1:
					return getPrivileges(rowIndex);
				}
			case Tables:
				break;
			case Columns:
				break;
			case Procedures:
				switch (columnIndex) {
				case 0:
					if(!rs.first())
						return "All";
					else
						return rs.getString("Db");
				case 1:
					return getPrivileges(rowIndex);
				}
			case None:
				return "Select a user and resource to display privileges for...";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "NOVALUE";
	}

	private String getPrivileges(int row) {
		Set<String> set = new LinkedHashSet<>();

		try {
			switch(res) {
			case Databases:
				// Fetch global privileges
				int colCount = rsmd2.getColumnCount();
				rs2.first();
				for(int i = 3; i < colCount; i++) {
					String colName = rsmd2.getColumnName(i);
					if (!colName.contains("_priv"))
						continue;
					if(rs2.getString(i).equals("Y"))
						set.add(colName.substring(0, colName.length()-5));
				}

				// Fetch specific privileges
				if(rs.first()) {
					colCount = rsmd.getColumnCount();
					for(int i = 4; i < colCount; i++) {
						String colName = rsmd.getColumnName(i);
						if (!colName.contains("_priv"))
							continue;
						if(rs.getString(i).equals("Y"))
							set.add(colName.substring(0, colName.length()-5));
					}
				}
				break;
			case Tables:
				if(rs.first()) {

				}
				break;
			case Columns:
				if(rs.first()) {

				}
				break;
			case Procedures:
				// Fetch global privileges
				rs2.first();
				if(rs2.getString("Execute_priv").equals("Y"))
					set.add("Execute");
				if(rs2.getString("Create_routine_priv").equals("Y"))
					set.add("Create_routine");
				if(rs2.getString("Alter_routine_priv").equals("Y"))
					set.add("Alter_routine");

				// Fetch specific privileges
				if(rs.first()) {
					if(rs.getString("Execute_priv").equals("Y"))
						set.add("Execute");
					if(rs.getString("Create_routine_priv").equals("Y"))
						set.add("Create_routine");
					if(rs.getString("Alter_routine_priv").equals("Y"))
						set.add("Alter_routine");
				}
				break;
			case None:
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Object[] setArray = set.toArray();
		String privSet = "";
		for (int i = 0; i < set.size(); i++)
			privSet += (i==0?"":",") + (String)setArray[i];

		return privSet;
	}
}
