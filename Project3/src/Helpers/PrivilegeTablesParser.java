package Helpers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PrivilegeTablesParser {
	@SuppressWarnings("unchecked")
	public static Map.Entry<String,String>[] parseDatabasePrivileges(ResultSet globalPrivs, ResultSet dbPrivs) throws SQLException {
		Map<String,String> map = new HashMap<String,String>(); // key=privilege value=database

		ResultSetMetaData globalMetaDData = globalPrivs.getMetaData();
		ResultSetMetaData dbMetaData = dbPrivs.getMetaData();

		// Fetch global privileges
		int colCount = globalMetaDData.getColumnCount();
		globalPrivs.first();
		for(int i = 1; i < colCount; i++) {
			String colName = globalMetaDData.getColumnName(i);
			if (colName.contains("_priv"))
				if(globalPrivs.getString(i).equals("Y"))
					map.put(colName.substring(0, colName.length()-5), "All");
		}

		// Fetch individual db privileges
		if(dbPrivs.first()) {
			colCount = dbMetaData.getColumnCount();
			for(int i = 1; i < colCount; i++) {
				String colName = dbMetaData.getColumnName(i);
				if (colName.contains("_priv"))
					if(dbPrivs.getString(i).equals("Y")) {
						String value = map.get(colName.substring(0, colName.length()-5));
						if(value == null || !value.equals("All")) {
							map.put(colName.substring(0, colName.length()-5), dbPrivs.getString("Db"));
						}
					}
			}
		}

		return (Map.Entry<String,String>[])map.entrySet().toArray(new Map.Entry[map.size()]);
	}

	@SuppressWarnings("unchecked")
	public static Map.Entry<String,String>[] parseTablePrivileges(ResultSet tablePrivs) throws SQLException {
		Map<String,String> map = new HashMap<String,String>(); // key=privilege value=database_table

		// Fetch individual db privileges
		if(tablePrivs.first()) {
			for(;!tablePrivs.isAfterLast();tablePrivs.next()) {
				String value = tablePrivs.getString("Db") + "_" + tablePrivs.getString("Table_name");
				String[] keys = tablePrivs.getString("Column_priv").split(",");
				for(String key : keys)
					map.put(key, value);
			}
		}

		return (Map.Entry<String,String>[])map.entrySet().toArray(new Map.Entry[map.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public static Map.Entry<String,String>[] parseColumnPrivileges(ResultSet colPrivs) throws SQLException {
		Map<String,String> map = new HashMap<String,String>(); // key=privilege value=database_table_column

		// Fetch individual db privileges
		if(colPrivs.first()) {
			for(;!colPrivs.isAfterLast();colPrivs.next()) {
				String value = colPrivs.getString("Db") + "_" + colPrivs.getString("Table_name") + "_" + colPrivs.getString("Column_name");
				String[] keys = colPrivs.getString("Column_priv").split(",");
				for(String key : keys)
					map.put(key, value);
			}
		}

		return (Map.Entry<String,String>[])map.entrySet().toArray(new Map.Entry[map.size()]);
	}

	@SuppressWarnings("unchecked")
	public static Map.Entry<String,String>[] parseProcedurePrivileges(ResultSet globalPrivs, ResultSet dbPrivs) throws SQLException {
		Map<String,String> map = new HashMap<String,String>();

		// Fetch global privileges
		globalPrivs.first();
		if(globalPrivs.getString("Alter_routine_priv").equals("Y"))
			map.put("Alter_routine", "All");
		if(globalPrivs.getString("Create_routine_priv").equals("Y"))
			map.put("Create_routine", "All");
		if(globalPrivs.getString("Execute_priv").equals("Y"))
			map.put("Execute", "All");

		// Fetch individual db privileges
		if(dbPrivs.first()) {
			String value = null;
			if(dbPrivs.getString("Alter_routine_priv").equals("Y")) {
				value = map.get("Alter_routine");
				if(value == null || !value.equals("All"))
					map.put("Alter_routine", dbPrivs.getString("Db"));
			}
			if(dbPrivs.getString("Create_routine_priv").equals("Y")) {
				value = map.get("Create_routine");
				if(value == null || !value.equals("All"))
					map.put("Create_routine", dbPrivs.getString("Db"));
			}
			if(dbPrivs.getString("Execute_priv").equals("Y")) {
				value = map.get("Execute");
				if(value == null || !value.equals("All"))
					map.put("Execute", dbPrivs.getString("Db"));
			}
		}

		return (Map.Entry<String,String>[])map.entrySet().toArray(new Map.Entry[map.size()]);
	}
}
