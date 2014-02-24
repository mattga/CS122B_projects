package Helpers;
import Helpers.MySQL;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import Types.User;

/**
 * Servlet implementation class Login
 */
public class Login {
	private PreparedStatement pstmt;

	public Login() {
		try {
			Connection conn = MySQL.getInstance().getConnection();

			pstmt = conn.prepareStatement("SELECT * FROM customers WHERE email=? AND password=?");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public User userLogin(String email, String password) {
		User u = null;
		try {
			Connection conn = MySQL.getInstance().getConnection();

			pstmt.setString(1,email);
			pstmt.setString(2,password);

			ResultSet rs = pstmt.executeQuery();
			
			if(rs.first()) {
				u = new User();
				u.id 			= rs.getInt("id");
				u.first_name	= rs.getString("first_name");
				u.last_name 	= rs.getString("last_name");
				u.email			= email;
			}

			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return u;
	}

}
