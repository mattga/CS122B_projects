import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Encapsulates behavior specific to customers. ** More Specifically
 * DeleteCustomer Method.
 */
public class Customer {
	private Scanner _s;

	public static enum Field {
		ID, NAME, EMAIL
	};

	public int id;
	public String first_name;
	public String last_name;
	public String cc_id;
	public String address;
	public String email;
	public String password;

	public Customer(Scanner s) {
		_s = s;
	}

	
	public Customer parseID(String line) {
		this.id = -1;
		while (this.id == -1) {
			try {
				this.id = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				System.out.println("Invalid Number Format, Enter an Integer Value.");
				line = _s.nextLine();
				this.id = -1;
			}
		}
		return this;
	}

	public int fetchNumericPromptAnswer() {
		int opt = -1;
		while (opt == -1) {
			try {
				opt = Integer.parseInt(_s.nextLine());
			} catch (NumberFormatException e) {
				opt = -1;
			}

			if (opt > 3)
				opt = -1;
			if (opt == -1)
				System.out.println("Please Enter a Number from 1 to 3.");

		}
		return opt;
	}

	
	public String composeInsertQuery() {
		return String.format("INSER INTO `customers` VALUES();");
	}

	
	public String composeFetchByQuery(Field field) {
		switch (field) {
		case EMAIL:
			return String.format("SELECT * FROM `customers` WHERE `email` = '%s' LIMIT 1", this.email);
		case ID:
			return String.format("SELECT * FROM `customers` WHERE `id` = '%s' LIMIT 1", this.id);
		case NAME:
			return String.format("SELECT * FROM `customers` WHERE `first_name` = '%s' AND `last_name` = '%s' LIMIT 1",
					this.first_name, this.last_name);
		}
		return null; // unreachable.
	}

	
	public String composeDeleteForeign(ResultSet rs) {
		this.id = -1;
		try {
			if (rs.first()) {
				id = Integer.parseInt(rs.getString(1));
			} else {
				return null; // No Foreign Keys
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return String.format("DELETE FROM `sales` WHERE `customer_id` = %d;", this.id);
	}

	
	public String composeDeleteQueryBy(Field field) {
		return composeDeleteByIDQuery();
	}

		
	public String composeDeleteByIDQuery() {
		return String.format("DELETE FROM `customers` WHERE `id` = '%s';", this.id);
	}

	
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("ID:         " + this.id);
		res.append("First Name: " + this.first_name);
		res.append("Last Name:  " + this.last_name);
		res.append("Address:    " + this.address);
		res.append("Email:      " + this.email);
		res.append("CC ID:      " + this.cc_id);
		res.append("ID:         " + this.password);
		return res.toString();
	}
}