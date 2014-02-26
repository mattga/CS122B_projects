import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Helpers.MySQL;

/**
 * Servlet implementation class Login
 */
@WebServlet("/ajaxSearch")
public class AjaxSearchServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MySQL mysql;
	private ResultSet results;

	public AjaxSearchServelet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Parse any expected parameters...
		String keyword = request.getParameter("kw");
		
//		System.out.println(keyword);
		response.addHeader("Status", "200 OK");
		response.addHeader("Content-Type", "text/plain");
		
		PrintWriter out = response.getWriter();
		if (keyword.equals("")) {
			 out.print("{\"result\":[]}");
			 out.close();
			 return;
		}
		// if(keyword == null) {
		// 	out.print("{\"result\":[\"No results\"]}");
		// 	return;
		// }
		
		// Print a JSON Object -- this is done manuallu, or we can look for Libraries...
		// Manually may be the best if the results we want to return are simple enough...

		// Tokenize keywords
		String[] keywords = keyword.split(" ");
		String query = "SELECT title FROM movies WHERE ";

		// Format WHERE clause with the last keyword being any words prefix and the rest matching some word
		for (int i = 0; i < keywords.length; i++) {
			if (i < keywords.length-1)
				query += "LOCATE('" + keywords[i] + "', title) > 0 AND ";
			else
				query += "title LIKE '%" + keywords[i] + "%'";
		}
		// System.out.println(query);

		// Execute & output results
		out.print("{\"result\":[");
		try {
			results = mysql.getInstance().getUnpooledStatement().executeQuery(query);
			if(results.first())
				for(boolean first = true;!results.isAfterLast();results.next()) {
					out.print((first?"\"":",\"") + results.getString(1) + "\"");
					first = false;
				}
		} catch (SQLException ex) {
			 ex.printStackTrace();
		} finally {
			try {
				mysql.getConnection().close();
			} catch (Exception e) {
				 e.printStackTrace();
			}
		}
		out.println("]}");
		out.close();
	}
}