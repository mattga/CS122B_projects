import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/ajaxSearch")
public class AjaxSearchServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public AjaxSearchServelet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Parse any expected parameters...
		String keyword = request.getParameter("kw");
		
		response.addHeader("Status", "200 OK");
		response.addHeader("Content-Type", "text/plain");
		PrintWriter out = response.getWriter();
		
		// Print a JSON Object -- this is done manuallu, or we can look for Libraries...
		// Manually may be the best if the results we want to return are simple enough...
		out.print("{\"result\":[\"Movie One\", \"Movie Two\"]}");
		
	}

}
