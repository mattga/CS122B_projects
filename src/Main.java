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
					DisplayDatabaseMetaData();
					break;
				case QUERY:
					RunUserInputQuery();
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

	public static void printResultSet(ResultSet rs) {
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
					// System.out.println(rs.getRow());
					for(int i = 1; i <= colNum; i++) {
						if(rsmd.getColumnType(i) == java.sql.Types.DATE) {
							if(10 > maxColWidths[i-1])
								maxColWidths[i-1] = 10;
						}
						else if(rs.getString(i).length() > maxColWidths[i-1]) {
							maxColWidths[i-1] = rs.getString(i).length();
						}
					}
				}

				// Print Column names
				for(int i = 1; i <= colNum; i++) {
					System.out.printf("%-" + (maxColWidths[i-1]+2) + "s", rsmd.getColumnName(i));
				}
				System.out.println();

				// Print Data
				for(rs.first(); !rs.isAfterLast(); rs.next()) {
					for(int i = 1; i <= colNum; i++) {
						if(rs.getString(i) == null)
							System.out.printf("%-" + (maxColWidths[i-1]+2) + "s", "NULL");
						else
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
		Customer c 	 = new Customer(s);
		
		int 	 res = 0;
		String   delQuery    = null;
		Customer.Field field = null;

		System.out.print("Enter Customer ID: ");
		c.parseID(s.nextLine());
		
		delQuery = c.composeDeleteForeign( statement.executeQuery(c.composeFetchByQuery(Customer.Field.ID)));
		field = Customer.Field.ID;

		// delQuery tells us if the customer exists
		if (delQuery != null) {
			statement.executeUpdate(delQuery);
			res = statement.executeUpdate(c.composeDeleteQueryBy(field));
		} else {
			System.out.println("Did not find any customer with given parameter.");
		}
		
		System.out.println("Removed: " + res + " Customer from the table.\n");
		return true;
	}

	private static boolean DisplayDatabaseMetaData() throws SQLException {
		System.out.println(new DatabaseMetaData(statement));
		return true;
	}

	private static boolean RunUserInputQuery() throws SQLException {
		UserSQLQuery usql = new UserSQLQuery(statement);
		System.out.println("Enter a SQL Query: ");
		while (!usql.isValid(s.nextLine())){
			System.out.println("Invalid Query Try Again.");
		}		
		usql.executeQuery();
		return true;
	}
}
