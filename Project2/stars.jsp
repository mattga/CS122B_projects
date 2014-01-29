<%@ page import="Types.Star" %>
<%@ page import="Types.User" %>
<%@ page import="Helpers.Stars" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.util.*" %>
<%
    // Check session for logged in...
    User u = (User)session.getAttribute("user");
    if(u == null)
        response.sendRedirect("login.jsp?return=stars.jsp");
    
    
    String url = "star.jsp?id=%ID";
    Stars stars = new Stars();

    // used inside _head* jsp file
    String document_title = "Browse Star Directory"; 
    HashMap<String, LinkedList<Star>> sm = stars.OrderIntoABCGroups(stars.getAllStars());
%>
<%@ include file="_template/_head.jsp" %>
    <div class="col-md-12">

    <h1>Star Directory</h1>

    <%
    for (char key : ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())) {
    %>
        <h2><%=key%></h2>
        <% if (sm.get(String.valueOf(key)).size() > 0) { %>
            <ul>
                <% for (Star st : sm.get(String.valueOf(key))) { %>
                    <li><a href="star.jsp?id=<%=st.id%>"><%=st.first_name%> <%=st.last_name%></a></li>
                <% } %>
            </ul>
        <% } else { %>
            <p>No Stars.</p>
        <%
        }
    }
    %>
    
</div>
<%@ include file="_template/_foot.jsp" %>
