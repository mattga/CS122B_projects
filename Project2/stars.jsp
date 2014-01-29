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
    
    <ul class="pagination">
    <% for (char letter : ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())) { %>
        <li><a href="#letter-<%=letter%>"><%=letter%></a></li>
    <% } %>
    </ul>
    <%
    for (char key : ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray())) {
    %>
        <h2 id="letter-<%=key%>"><%=key%></h2>
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
    <div style="position:fixed;bottom:-10px;padding:15px;left:50%;margin-left:450px;background-color:#DDD;border-radius:10px;"><a href="#">TOP</a></div>
</div>
<%@ include file="_template/_foot.jsp" %>
