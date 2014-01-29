<!DOCTYPE html>
<html>
<head>
    <meta name="content-type" description="utf-8">
    <title>Fab Flix</title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <!-- <link rel="stylesheet" type="text/css" href="css/custom.css"> -->
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]><script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script><script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script><![endif]-->
</head>
<body>
    <div class="container">
        <header class="row">
            <!-- Title Menu -->
            <div class="col-md-8">
                <h1>Fab.Flix.Me</h1>
            </div>
            
            <!-- Search Bar -->
            <div class="col-md-4">

                <!-- Input Group for Search Box -->
                <div class="input-group" style="margin:20px 0 20px;">
                  <input type="text" class="form-control" placeholder="Star, Movie, or Genre">
                  <div class="input-group-btn">
                    <button class="btn btn-primary">Search</button>
                  </div>
                </div>

            </div>
        </header>
        
        <!-- Naviation -->
        <nav class="row">
            <ul class="nav nav-pills col-md-12">
                <li><a href="/">Home</a></li>
                <li><a href="browse.jsp?title">Browse By Title</a></li>
                <li><a href="browse.jsp?genre">Browse By Genre</a></li>
                <li><a href="cart.jsp">View Cart</a></li>
                <li><a href="logout.jsp">Log Out</a></li>
            </ul>
        </nav>

        <section class="row">
