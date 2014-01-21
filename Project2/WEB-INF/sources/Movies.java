import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

public class Movies extends HttpServlet {
	private Connection conn;
	private Statement statement;
	private PreparedStatement pstmt;

	public Movies() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?user=moviedb&password=tiger");
			statement = conn.createStatement();

			pstmt = conn.prepareStatement("SELECT * FROM movies AS m, genres_in_movies AS gim, genres AS g WHERE m.id = gim.movie_id AND gim.genre_id = g.id AND g.name = ?");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String genre = request.getParameter("genre");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = genre + " Movies";
		String docType =
		"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
		"Transitional//EN\">\n";
		out.println(docType +
			"<HTML>\n" +
			"<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
			"<BODY BGCOLOR=\"#FDF5E6\">\n" +
			"<H1>" + title + "</H1>");

		try {
			pstmt.setString(1,genre);
			ResultSet rs = pstmt.executeQuery();

			if (!rs.first())
				out.println("<I>No Movies</I>");
			else {
				out.println("<UL>");
				for(; !rs.isAfterLast(); rs.next())
					out.println("<LI>" + rs.getString("title"));
				out.println("</UL>");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		out.println("</BODY></HTML>");
	}
}
