<!DOCTYPE html>
<html>
<head>
    <meta name="content-type" description="utf-8">
        <title>
        <% out.println(document_title); %> | Fab.Flix.io
    </title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
    <!--link href="css/ui-lightness/jquery-ui-1.10.4.custom.css" rel="stylesheet"-->
    <!-- <link rel="stylesheet" type="text/css" href="css/custom.css"> -->
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]><script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script><script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script><![endif]-->
    <script src="js/jquery.min.js"></script>
    <!--script src="js/jquery-ui-1.10.4.custom.min.js"></script-->
    <script src="js/underscore.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <style>
        #info-hover {
            position:fixed;
            top:0;
            left:0;
            display:none;
            border:#aaa;
            background-color:#fff;
            border-radius: 10px;
            padding:10px;
            z-index: 1000;
            box-shadow: 0 0 10px #000;
        }

        #searchbox-wrapper {
            position:relative;
            top:0;
            left:0;
        }

        #searchbox-results {
            position: absolute;
            top:33px;
            left:0px;
            z-index: 100;
            background-color: #fff;
            border:solid 1px #AAA;
            width:293px;
        }

        #searchbox-results button:hover {
            background-color:#EEE;
        }

        #searchbox-results button {
            background-color:transparent;
            border:none;
            width:100%;
            margin:0;
            padding:2px 10px;
            text-align:left;
            /*display:none;*/
        }
    </style>
     <!-- Autocompletion JQuery function -->
    <script>
        $(function() {
            // just tracking mouse to place the hover over in the right place...
            $(document).mousemove(function(e){window.mouseXPos = e.pageX;window.mouseYPos = e.pageY;}); 

            $('#searchbox-results').hide();// Hide to start...
            $('#searchField').on('blur', function() {
                // hide when clicks away from...
                _.delay(function(){$('#searchbox-results').fadeOut();}, 250);
                }); 
            $('#searchbox-results').on('click', '.movie-suggestion', function() { 
                console.log('Clicked Suggestion');
                $('#searchField').val($(this).text());
                $('#searchbox-results').hide();
            });
            // Debounce is used to throttle the number of times the function is called.
            $('#searchField').on('input', _.debounce(function() {
                // Cancel Previous Request....
                if (window.ajaxRequest && window.ajaxRequest.readyState != 4)
                    window.ajaxRequest.abort();
                
                
                // Options to pass to Ajax Method...
                var ajaxOptions = {
                    url: 'ajaxSearch',
                    method: 'POST',
                    data: {'kw' : $('#searchField').val()},
                    error:  function(e){ 
                        console.log(new Date()); // Date to distiguish errors
                        console.log(e);
                    }
                };
                
                // Function to execute once the ajax has returned...
                var finishedFetching = function(data){
                    console.log(data);
                    // Parse the String into something useable...
                    data = _.uniq(JSON.parse(data).result);
                    var html = "";
                    if (data == [])
                        html = "No Results Found";
                    else 
                        for (var i in data)
                            html += '<button class="movie-suggestion">'+data[i]+'</button>';
                    
                    $('#searchbox-results').html(html);
                    $('#searchbox-results').show();
                    $('.movie-suggestion').on('click', function() { 
                        $('#searchField').val($(this).text());
                    });
                };
                
                // Set up the Ajax Call
                window.ajaxRequest = $.ajax(ajaxOptions).done(finishedFetching);
            }, 250));
        });

        function displayMovieInfo(id) {
                                    // Options to pass to Ajax Method...
                var ajaxOptions = {
                    url: 'ajaxMovie',
                    method: 'POST',
                    data: {'id' : id},
                    error:  function(e){ 
                        console.log(new Date()); // Date to distiguish errors
                        console.log(e);
                    }
                };
                

            window.ajaxRequest = $.ajax(ajaxOptions).done(function(data) {
                data = JSON.parse(data);
                var html = "<img src=\""+ data.result.banner +"\"><br>";
                html += "<b>Year</b>:"+ data.result.year +"<br>"; 
                html += "<b>Stars</b>: ";
                for (var i in data.result.stars)
                    html += data.result.stars[i] + "<br>";
                
                $("#info-hover").html(html).show();
                $("#info-hover").css({left:window.mouseXPos,top:window.mouseYPos - $(document).scrollTop()});
            });
        }

        function hideMovieInfo(id) {
            $("#info-hover").fadeOut();
        }
    </script>
</head>
<body>
    <div id="info-hover"><!--Used for the hover box...--></div>
    <div class="container">
        <header class="row">
            <!-- Title Menu -->
            <div class="col-md-8">
                <h1>Fab.Flix.io</h1>
            </div>
            
            <!-- Search Bar -->
            <div class="col-md-4">
                <!-- Input Group for Search Box -->
                <div id="searchbox-wrapper">
                    <form action="search.jsp" method="post" id="searchForm">
                    <div class="input-group" style="margin:15px 0 5px;">
                        <input type="text" class="form-control" id="searchField" name="title" placeholder="Movie Name" autocomplete="off">
                        <div class="input-group-btn">
                            <button class="btn btn-primary">Search</button>
                        </div>
                    </div>
                    </form>
                    <div id="searchbox-results"></div>
                </div>
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
