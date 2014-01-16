

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Statement statement;
    
    public Login() {
        super();
        
        // TODO: Implement with connection pooling
        try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?user=moviedb&password=tiger");
			statement = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String email = request.getParameter("email");
		String pass = request.getParameter("password");
		
		if(email.equals("") || pass.equals(""))
			out.println("Please provide both username and password.");
		
		try {
			ResultSet rs = statement.executeQuery("SELECT * FROM customers WHERE email='" + email + "' AND password='" + pass + "'");
			
			if(!rs.first())
				out.println("Incorrect username or password.");
			
			out.println("Welcome " + rs.getString("first_name") + " " + rs.getString("last_name") + "!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
