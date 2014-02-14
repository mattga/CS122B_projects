import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserSQLQuery {
	public enum Type {UPDATE, SELECT};
	
	
	private Statement _stmt;
	private String _query;
	private Type _type;
	private ResultSet _rs;
	
	public String[] updateQueries = new String[] {"insert", "update", "delete"};
	public String[] selectQueries = new String[] {"show", "select"};
	  
	
	
	public UserSQLQuery(Statement s) {
		_stmt = s;
	}
	
	public void executeQuery() throws SQLException {
		int result = -1;
		switch (_type) {
			case UPDATE:
				try {
					result = _stmt.executeUpdate(_query);
					System.out.println("Modified "+ result + "Rows");
				} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
					System.out.println("Cannot Update Record Due to Constraint Violations");
				} catch (Exception e) {
					System.out.println("Could not Excute Given Query.");
				}
				break;
			case SELECT:
				_rs = _stmt.executeQuery(_query);
				Main.printResultSet(_rs);
				break;
		}
	}
	
	public boolean parseQuery(String query) {
		return isValid(query); // Check for validity, check type and save iff valid.
	}
	
	public boolean isValid(String query) {
		boolean valid = false;
		for (String s : updateQueries) {
			valid = valid || query.toLowerCase().contains(s);
		}
		
		for (String s : selectQueries) {
			valid = valid || query.toLowerCase().contains(s);
		}
		
		if (valid) {
			_query = query;
			determineType(_query);
		}
		
		return valid;
	}
	
	public void determineType(String query){
		for (String s : updateQueries) {
			if (query.toLowerCase().contains(s))
				_type = Type.UPDATE;
		}
		
		for (String s : selectQueries) {
			if (query.toLowerCase().contains(s))
				_type = Type.SELECT;
		}
	}
}
