import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Helpers.Movies;
import Helpers.MySQL;
import Helpers.Stars;
import Types.Movie;
import Types.Star;


public class AjaxMovieInfoServelet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private MySQL mysql;
	private ResultSet results;

	public AjaxMovieInfoServelet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = 0;
		
		if (request.getParameter("id") != null && request.getParameter("id") != "")
			id = Integer.parseInt(request.getParameter("id"));

		response.addHeader("Status", "200 OK");
		response.addHeader("Content-Type", "text/plain");
		
		Movies ms = new Movies();
		Movie m = ms.getMovie(id);

		PrintWriter out = response.getWriter();
		out.append("{\"result\":{");
		out.append("\"banner\":\""+ m.banner_url +"\",");
		out.append("\"year\":\""+ m.year +"\",");
		out.append("\"stars\":[");
		int i = 0;
		for (Star s : m.stars) { 
			out.append("\""+ s.first_name +" "+ s.last_name +"\"");
			if (i++ != m.stars.length-1)  
				out.append(",");
		}
		out.append("]");
		out.append("}}");
	}
	
}
