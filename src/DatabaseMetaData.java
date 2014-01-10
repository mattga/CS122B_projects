import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseMetaData {
	private Statement _stmt;
	private ArrayList<String> _tables;
	StringBuffer _buffer;
	private boolean _error = false;

	/**
	 * Build's a string with the meta data of the tables in the database.
	 * @param s
	 */
	public DatabaseMetaData(Statement s) {
		_stmt = s; // SQL-Connection
		_buffer = new StringBuffer(); // Output goes here
		_tables = new ArrayList<String>(); // List of tables
		
		try {
			// Put table names in `_tables`
			extractTableNames(_stmt.executeQuery(composeTableNamesQuery()));

			// fetch the string representation of each tables meta-data
			for (String name : _tables)
				_buffer.append(new TableMeta(name)); 
		} catch (SQLException e) { _error = true;}
	}
	
	/**
	 * Extracts the  table names from a result set of a `show table;` query
	 * @param ResultSet rs
	 */
	public void extractTableNames(ResultSet rs) {
		try {
			if(rs.first())
				for(rs.first(); !rs.isAfterLast(); rs.next()) 
					_tables.add(rs.getString(1));
		} catch (SQLException e) { _error = true; }
	}

	/**
	 * The Query for fetching table names.
	 * @return
	 */
	public String composeTableNamesQuery() {
		return String.format("show tables;");
	}

	/**
	 * Creates the Select Queries for all the tables in the DB.
	 * @param tables
	 * @return
	 */
	public String[] composeFetchAllQueries(String tables[]) {
		String queries[] = new String[tables.length];
		for (int i = 0; i < tables.length; i++)
			queries[i] = String.format("SELECT * FROM `%s`;", tables[i]);
		return queries;
	}

	/**
	 * The String representattion that will be shown on the screen, built up inside other methods
	 */
	public String toString() {
		if (_error)
			return "An error occured -- Could not fetch database metadata.";
		return _buffer.toString();
	}
	
	/**
	 * Represents a single tables meta data.
	 */
	public class TableMeta {
		public String name;
		public String query;
		public ResultSetMetaData md;
		public HashMap<String, String> fields;
		public int fieldcount;
		public int count = -1;
		
		public TableMeta(String name) {
			this.name = name;
			this.fields = new HashMap<String, String>();
			
			try {
				ResultSet rs = _stmt.executeQuery(composeSelectQuery());
				md = rs.getMetaData();
				fieldcount = md.getColumnCount();
				// Put the fields and their types in a map.
				for (int i = 1; i <= fieldcount; i++)
					fields.put(md.getColumnName(i), md.getColumnTypeName(i));
				
				
				// Count the number of items in the result set.
				for (; !rs.isAfterLast(); rs.next(), count++);
				
			} catch (SQLException e) {
				_error = true;
			}
		}
		
		
		public String composeSelectQuery(){
			query = String.format("SELECT * FROM `%s`", name);
			return query;
		}
		
		/**
		 * This is the string represnetation that will be displayed on screen.
		 */
		public String toString() {
			StringBuffer bf = new StringBuffer(); 			
			bf.append("Table:\t"+ name +"\n");
			bf.append("Record Count: "+ count +"\n");
			bf.append("Fields: \n");
			for (String key : fields.keySet()) {
				bf.append("\t"+ key +": "+fields.get(key) +"\n");
			}
			bf.append("\n\n");
			return bf.toString();
		}
		
	}
}
