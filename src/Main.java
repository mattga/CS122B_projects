import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Main {
	private static Scanner s;
	private static Option option;
	private static String s1, s2, s3, s4;
	private static ConnectionManager cm;
	private static Statement statement;
	private static ResultSet rs;
	private static enum Option {PRINT_MOVIES, NEW_STAR, NEW_CUSTOMER, DELETE_CUSTOMER, METADATA, QUERY, EXIT_MENU, EXIT, INVALID, NONE};

	public static void main (String[] args) {
		// Initializations
		s = new Scanner(System.in);
		option = Option.NONE;
		cm = new ConnectionManager();
		statement = null;
		rs = null;

		try {
			while (true) {
				// Implementation of menu functions
				switch (option) {
				case PRINT_MOVIES:
					PrintMoviesFeatStar();
					break;
				case NEW_STAR:
					AddNewStar();
					break;
				case NEW_CUSTOMER:
					AddNewCustomer();
					break;
				case DELETE_CUSTOMER:
					DeleteCustomer();
					break;
				case METADATA:
					break;
				case QUERY:
					break;
				case EXIT:
					System.out.println("Program Terminated.");
					System.exit(1);
					break;
				case INVALID:
					System.out.println("Invalid command.");
					if (statement != null)
						break;
				case EXIT_MENU:
				default:
					System.out.print("MovieDB Login\nUsername: ");
					s1 = s.nextLine();
					System.out.print("Password: ");
					s2 = s.nextLine();
					statement = cm.connect(s1, s2);
				}

				// Display menu if logged in
				if (statement != null)
					option = menu();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void printResultSet(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			int[] maxColWidths = new int[colNum];

			if(rs.first()) {
				// Initialize column widths to size of column name
				for(int i = 1; i <= colNum; i++) {
					maxColWidths[i-1] = rsmd.getColumnName(i).length();
				}

				// Find max column widths from data
				for(; !rs.isAfterLast(); rs.next()) {
					System.out.println(rs.getRow());
					for(int i = 1; i <= colNum; i++)
						if(rs.getString(i).length() > maxColWidths[i-1])
							maxColWidths[i-1] = rs.getString(i).length();
				}

				// Print Column names
				for(int i = 1; i <= colNum; i++) {
					System.out.printf("%-" + (maxColWidths[i-1]+2) + "s", rsmd.getColumnName(i));
				}
				System.out.println();

				// Print Data
				for(rs.first(); !rs.isAfterLast(); rs.next()) {
					for(int i = 1; i <= colNum; i++) {
						System.out.printf("%-" + (maxColWidths[i-1]+2) + "s", rs.getString(i));
					}
					System.out.println();
				}
			}
			else {
				System.out.println("No results found.\n");
			}

		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}

	private static Option menu() {
		System.out.println("======================================= MovieDB Commands =======================================");
		System.out.printf("%-5s%-30s", "1)", "Print Movies Featuring Star");
		System.out.printf("%-5s%-16s", "2)", "New Star");
		System.out.printf("%-5s%-15s", "3)", "New Customer");
		System.out.printf("%-5s%-18s", "4)", "Delete Customer");
		System.out.printf("\n%-5s%-30s", "5)", "Metadata of Database");
		System.out.printf("%-5s%-16s", "6)", "Execute Query");
		System.out.printf("%-5s%-15s", "7)", "Exit Menu");
		System.out.printf("%-5s%-18s\n", "8)", "Exit Program");
		System.out.println("================================================================================================");

		try {
			return Option.values()[Character.getNumericValue(s.nextLine().charAt(0))-1];
		} catch(StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {} // Return INVALID for any other characters

		return Option.INVALID;
	}

	private static boolean PrintMoviesFeatStar() throws SQLException {
		// Get star name
		System.out.print("\nStar's first and/or last name (may leave one blank)\nFirst: ");
		s1 = s.nextLine();
		System.out.print("Last: ");
		s2 = s.nextLine();

		// If both are fields empty, ask again.
		if(s1.equals("") && s2.equals("")) {
			System.out.println("Please provide first and/or last name.\n");
			return false; //or break to menu?
		}

		// Query database & print results
		rs = statement.executeQuery("SELECT m.* FROM movies AS m, stars AS s, stars_in_movies AS sim WHERE m.id = sim.movie_id" +
				" AND sim.star_id = s.id" + (s1.equals("")?"":" AND s.first_name='" + s1 + "'") + (s2.equals("")?"":" AND s.last_name='" + s2 + "'"));
		printResultSet(rs);
		return true;
	}

	private static boolean AddNewStar() throws SQLException {
		String errBuff = "";

		System.out.print("First Name: ");
		s1 = s.nextLine();
		System.out.print("Last Name: ");
		s2 = s.nextLine();
		System.out.print("Date of Birth (YYYY-MM-DD): ");
		s3 = s.nextLine();
		System.out.print("Photo URL: ");
		s4 = s.nextLine();

		if(s1.equals("") && s2.equals(""))
			errBuff += "Please provide the star's name.\n";
		if(!s3.equals("") && !s3.matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$"))
			errBuff += "Date of Birth must be formatted YYYY-MM-DD.\n";
		System.out.print(errBuff + "\n");
		if(!errBuff.equals(""))
			return false;

		if(!s1.equals("") && s2.equals("")) {
			s2 = s1;
			s1 = "";
		}

		int res = statement.executeUpdate("INSERT INTO stars VALUES (NULL, '" + s1 + "', '" + s2 + "', " + (s3.equals("")?"NULL":"'"+s3+"'") + ", '" + s4 + "')");

		System.out.println("Star added at row " + res + " of table.\n");
		return true;
	}

	//Insert a customer into the database. Do not allow insertion of a customer if his credit card does not exist in the credit card table. The credit card table simulates the bank records.
	//If the customer has a single name, add it as his last_name and and assign an empty string ("") to first_name.
	private static boolean AddNewCustomer() throws SQLException {
		String errBuff = "";
		
		System.out.print("First Name: ");
		s1 = s.nextLine();
		System.out.print("Last Name: ");
		s2 = s.nextLine();
		System.out.print("Credit Card ID: ");
		s3 = s.nextLine();
		System.out.print("Address: ");
		s4 = s.nextLine();
		System.out.print("Email: ");
		String s5 = s.nextLine();
		System.out.print("Password: ");
		String s6 = s.nextLine();


		if(s1.equals("") && s2.equals(""))
			errBuff += "Please provide the star's name.\n";
		if(s3.equals(""))
			errBuff += "Please provide a credit card ID.\n";
		else if(!statement.executeQuery("SELECT * FROM creditcards WHERE id='" + s3 + "'").first())
			errBuff += "Credit card ID does not exist.\n";
		if(s4.equals(""))
			errBuff += "Please provide an address.\n";
		if(s5.equals(""))
			errBuff += "Please provide an email.\n";
		if(s6.equals(""))
			errBuff += "Please provide a password.\n";
		System.out.print(errBuff + "\n");
		if(!errBuff.equals(""))
			return false;

		if(!s1.equals("") && s2.equals("")) {
			s2 = s1;
			s1 = "";
		}

		int res = statement.executeUpdate("INSERT INTO customers VALUES (NULL, '" + s1 + "', '" + s2 + "', '"
				+ s3 + "', '" + s4 + "', '" + s5 + "', '" + s6 + "')");
		System.out.println("Star added at row " + res + " of table.\n");
		return true;
	}

	private static boolean DeleteCustomer() throws SQLException {
		Customer c 	 = new Customer();
		int      opt = c.displayDeletePrompt().fetchNumericPromptAnswer();
		int 	 res = -1;

		switch (opt) {
			case 1:
				System.out.print("ID: ");
				c.parseID(s.nextLine());
				
				// Remove From Foreign Tables
				statement.executeUpdate( c.composeDeleteForeign( statement.executeQuery(c.composeFetchByQuery(Customer.Field.ID))));
				// Remove Customer from `customer`
				res = statement.executeUpdate(c.composeDeleteByIDQuery());
				break;
			case 2:
				System.out.print("Full Name(First Last): ");
				c.parseFullName(s.nextLine());
				String q = c.composeFetchByQuery(Customer.Field.NAME);
				// Remove From Foreign Tables
				statement.executeUpdate( c.composeDeleteForeign( statement.executeQuery(c.composeFetchByQuery(Customer.Field.NAME))));
				// Remove Customer from `customer`
				res = statement.executeUpdate(c.composeDeleteByFullNameQuery());
				break;
			case 3:
				System.out.print("Email: ");
				c.parseEmail(s.nextLine());
				
				// Remove From Foreign Tables
				statement.executeUpdate( c.composeDeleteForeign( statement.executeQuery(c.composeFetchByQuery(Customer.Field.EMAIL))));
				// Remove Customer from `customer`
				res = statement.executeUpdate(c.composeDeleteByEmailQuery());
				break;
		}
		
		System.out.println("Removed: " + res + " of table.\n");
		return true;
	}


	/**
	 * Encapsulates behavior specific to customers.
	 * ** More Specifically DeleteCustomer Method.
	 */
	public static class Customer {
		public static enum Field {ID, NAME, EMAIL};
		public int id;
		public String first_name;
		public String last_name;
		public String cc_id;
		public String address;
		public String email;
		public String password;


		public Customer parseID(String line) {
			this.id = -1;
			while (this.id == -1) {
				try {
					this.id = Integer.parseInt(line);
				} catch (NumberFormatException e) {
					System.out.println("Invalid Number Format, Enter an Integer Value.");
					line = s.nextLine();
					this.id = -1;
				}
			}
			return this;
		}


		public Customer parseEmail(String line) {
			this.email = null;
			while (this.email == null) {
				this.email = line.matches(".*@.*") ? line : null;
				if (this.email == null) {
					System.out.println("Invalid Email Format");
					line = s.nextLine();
				}
			}
			return this;
		}


		public Customer parseFullName(String line) {
			while (line.equals("")) {
				System.out.println("Please Enter A Full Name, eg: John Smith");
				line = s.nextLine();
			}

			String result[] = line.split(" ");

			if (result.length > 1) {
				this.first_name = result[0];
				this.last_name = result[1];
			} else {
				this.first_name = result[0];
				this.last_name = "";
			}

			return this;
		}


		public Customer displayDeletePrompt() {
			System.out.println("Would You Like To Delete By:");
			System.out.println("1. ID");
			System.out.println("2. Full Name");
			System.out.println("3. Email");

			return this;
		}


		public int fetchNumericPromptAnswer() {
			int opt = -1;
			while (opt == -1) {
				try { 
					opt = Integer.parseInt(s.nextLine()); 
				} catch(NumberFormatException e) {opt = -1;}
			}
			return opt;
		}


		public String composeInsertQuery() {
			return String.format("INSER INTO `customers` VALUES();");
		}
		
		public String composeFetchByQuery(Field field) {
			switch(field){
				case EMAIL:
					return String.format("SELECT * FROM `customers` WHERE `email` = '%s' LIMIT 1", this.email);
				case ID:
					return String.format("SELECT * FROM `customers` WHERE `id` = '%s' LIMIT 1", this.id);
				case NAME:
					return String.format("SELECT * FROM `customers` WHERE `first_name` = '%s' AND `last_name` = '%s' LIMIT 1", this.first_name, this.last_name);
			}
			return null; // unreachable.
		}
		
		public String composeDeleteForeign(ResultSet rs) {
			this.id = -1; 
			try {
				if(rs.first()) {
					id = Integer.parseInt(rs.getString(1));
				}
				else {
					return null; // No Foreign Keys
				}
			} catch (SQLException e) { e.printStackTrace();}
			
			return String.format("DELETE FROM `sales` WHERE `customer_id` = %d;", this.id);
		}

		public String composeDeleteByEmailQuery() {
			return String.format("DELETE FROM `customers` WHERE `email` = '%s';", this.email);
		}


		public String composeDeleteByFullNameQuery() {
			return String.format("DELETE FROM `customers` WHERE `first_name` = '%s' AND `last_name` = '%s';", this.first_name, this.last_name);
		}


		public String composeDeleteByIDQuery() {
			return String.format("DELETE FROM `customers` WHERE `id` = '%s';", this.id);
		}

		
		public String toString(){
			StringBuffer res = new StringBuffer();
			res.append("ID:	        "+ this.id);
			res.append("First Name:	"+ this.first_name);
			res.append("Last Name:	"+ this.last_name);
			res.append("Address:	"+ this.address);
			res.append("Email:  	"+ this.email);
			res.append("CC ID:	    "+ this.cc_id);
			res.append("ID:	        "+ this.password);
			return res.toString();
		}
	}
}
