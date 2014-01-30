<%@ page import="Types.User" %>
<%@ page import="java.io.IOException" %>
<%
    User u = (User)session.getAttribute("user");
    if (u == null) {
        response.sendRedirect("login.jsp");
    } 
    // Display the page below if the user is logged in....
%>
<% 
    // used inside _head* jsp file
    String document_title = "Member Login"; 
%>
<%@ include file="_template/_head.jsp" %>
    <div class="col-md-12">

    <jsp:useBean id="login" class="Helpers.Login"/>
    <h1>Advanced Search</h1>
    <p>
        Advanced search allows you to search for the movies and match the fields you want. Leave any field you do not want a match for blank.
    </p>
    </div>
    <div class="col-md-4">
    <form action="search.jsp" method="GET">
        <div class="form-group">
            <label for="title">Movie Title</label>
            <input type="text" class="form-control" id="title" name="title" placeholder="Movie Title">
        </div>
        <div class="form-group">
            <label for="year">Release Year</label>
            <input type="text" class="form-control" id="year" name="year" placeholder="Relase Year">
        </div>
        <div class="form-group">
            <label for="director">Movie Director</label>
            <input type="text" class="form-control" id="director" name="director" placeholder="Movie Director">
        </div>
        <div class="form-group">
            <label for="starfn">Star First Name</label>
            <input type="text" class="form-control" id="starfn" name="starfn" placeholder="Star's First Name">
        </div>
        <div class="form-group">
            <label for="starln">Star Last Name</label>
            <input type="text" class="form-control" id="starln" name="starln" placeholder="Star's Last Name">
        </div>
        
        <button type="submit" class="btn btn-primary">Search</button>
    </form>
    </div>
<%@ include file="_template/_foot.jsp" %>