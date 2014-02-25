<!DOCTYPE html>
<html>
<head>
    <meta name="content-type" description="utf-8">
        <title>
        <% out.println(document_title); %> | Fab.Flix.io
    </title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <link href="css/ui-lightness/jquery-ui-1.10.4.custom.css" rel="stylesheet">
    <!-- <link rel="stylesheet" type="text/css" href="css/custom.css"> -->
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]><script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script><script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script><![endif]-->
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery-ui-1.10.4.custom.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</head>
<body>
    <div class="container">
        <header class="row">
            <!-- Title Menu -->
            <div class="col-md-8">
                <h1>Fab.Flix.io</h1>
            </div>
            
            <!-- Search Bar -->
            <div class="col-md-4">

                <!-- Autocompletion JQuery function -->
                <script>
                $(function() {
                    //global variable to track some state
                    resultFetched = false;

                    // The data source...
                    var availableTags = [
                    "test1",
                    "test2"
                    ];

                    var options = {'source': availableTags};
                    $('#searchField').autocomplete(options);

                    $('#searchField').on('input', function() {
                        // We already fetched result once... 
                        // Used to temporarly only fetch resulte once...
                        // Change to match some other condition...
                        if (window.resultFetched)
                            return;

                        // Options to pass to Ajax Method...
                        var ajaxOptions = {
                            url:'ajaxSearch',
                            method:'POST',
                            data: '?kw=value',
                            error:  function(e){
                                        alert("ERROR OCCURRED -- CHECK CONSOLE...");
                                        console.log(e);
                                    }
                        };
                        
                        // Function to execute once the ajax has returned...
                        var finishedFetching = function(data){
                            console.log(data);
                            // Parse the String into something useable...
                            data = JSON.parse(data);
                            // Replace the data-set with something dynamic...
                            $('#searchField').autocomplete({'source': data.result});
                            window.resultFetched = true;
                        };
                        
                        // Set up the Ajax Call
                        $.ajax(ajaxOptions).done(finishedFetching);
                    });
                });
                </script>

                <!-- Input Group for Search Box -->
                <form action="search.jsp" method="post" id="searchForm">
                <div class="input-group" style="margin:15px 0 5px;">
                    <input type="text" class="form-control" id="searchField" name="title" placeholder="Movie Name">
                    <div class="input-group-btn">
                        <button class="btn btn-primary">Search</button>
                    </div>
                </div>
                </form>
                <p>
                    <a href="advanced_search.jsp">Advanced Search</a>
                </p>

            </div>
        </header>
        
        <!-- Naviation -->
        <nav class="row">
            <ul class="nav nav-pills col-md-12">
                <!-- <li><a href="">Home</a></li> -->
                <li><a href="browse.jsp?title">Browse By Title</a></li>
                <li><a href="browse.jsp">Browse By Genre</a></li>
                <li><a href="stars.jsp">Browse Stars</a></li>
                <li><a href="cart.jsp">View Cart</a></li>
                <!-- <li><a href="logout.jsp">Log Out</a></li> -->
            </ul>
        </nav>

        <section class="row">
