<%@ page import="java.util.Map" %>
<%@ page import="dbObjects.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: tpre939
  Date: 22/01/2018
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Smart Cookie</title>


    <!--font-->
    <link href="https://fonts.googleapis.com/css?family=Alegreya+Sans" rel="stylesheet">

    <%--Icon--%>
    <link rel="icon" href="images/cookies.ico">

    <script
            src="https://code.jquery.com/jquery-2.2.4.min.js"
            integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
            crossorigin="anonymous"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>


    <script src="https://www.google.com/recaptcha/api.js" async defer></script>

    <!-- Bootstrap Date-Picker Plugin -->
    <script type="text/javascript" src="bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <link href="bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">


    <%--Formatting Text in writing article--%>
    <script src="https://cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>

    <!--custom css-->
    <link rel="stylesheet" href="index.css" type="text/css">
    <meta http-equiv="Cache-Control" content="no-cache"/>

</head>
<body>


<c:if test="${cookie.containsKey('username')}">
    <c:if test="${sessionScope.name == null}">
    <c:redirect url="LoginServlet?cookie=true"/>
    </c:if>
</c:if>


<div class="container" id="alerts">
    <div class="row">
        <div class="col-xs-4"></div>
        <div class="col-xs-4">
            <div>
                <c:choose>
                    <c:when test="${recaptcha != null}">
                        <div class="alert alert-danger" role="alert">
                            Register user failed
                        </div>
                    </c:when>
                    <c:when test="${loginFailed != null}">
                        <div class="alert alert-danger" role="alert">
                            Incorrect user/pass combination
                        </div>
                    </c:when>
                    <c:when test="${login == 'fail'}">
                        <div class="alert alert-danger" role="alert">
                            Username already taken
                        </div>
                    </c:when>
                    <c:when test="${logout != null}">
                        <div class="alert alert-info" role="alert">
                            Logout Successful
                        </div>
                    </c:when>
                    <c:when test="${deleted != null}">
                        <div class="alert alert-info" role="alert">
                            Article successfully deleted
                        </div>
                    </c:when>
                    <c:when test="${reset == 'fail'}">
                        <div class="alert alert-danger" role="alert">
                            Email not found or timed out
                        </div>
                    </c:when>
                    <c:when test="${reset == 'sent'}">
                        <div class="alert alert-info" role="alert">
                            Email successfully sent
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="col-xs-4"></div>
    </div>
</div>


<nav class="navbar navbar-fixed-top" style="z-index: 1">
    <div class="container-fluid" style="z-index: 1">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" id="home" href="index.jsp">SmartCookie</a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">


        <ul class="nav navbar-nav navbar-right">

            <c:choose>
                <c:when test="${sessionScope.name != null}">
                    <li id="avatarButton"><a href="MultimediaServlet?allMedia=true"><span
                            class="glyphicon glyphicon-picture"></span> Gallery</a></li>

                    <li id="avatarButton"><a href="profilePage.jsp?fromProfilePage=1"><span
                            class="glyphicon glyphicon-user"></span> Profile</a></li>

                    <li id="logoutButton"><a href="LoginServlet?logout=true"><span
                            class="glyphicon glyphicon-log-out"></span> Logout</a></li>

                </c:when>


                <c:otherwise>
                    <li id="avatarButton"><a href="MultimediaServlet?allMedia=true"><span
                            class="glyphicon glyphicon-picture"></span> Gallery</a></li>

                    <li id="loginButton"><a href="#" data-toggle="modal" data-target=".login-register-form"><span
                            class="glyphicon glyphicon-log-in"></span> Login</a></li>
                    <li id="signupButton"><a href="#" data-toggle="modal" data-target=".login-register-form"><span
                            class="glyphicon glyphicon-pencil"></span> Register</a></li>
                </c:otherwise>
            </c:choose>

        </ul>
        </div>
    </div>
</nav>

<div class="container">



    <div id="banner" class="banner jumbotron row">

        <c:choose>
            <c:when test="${sessionScope.name != null}">
                <h1>Hi there ${sessionScope.name}</h1><br/>

                <a href="#" data-toggle="modal" data-target=".write-article"><span
                        class="glyphicon glyphicon-plus"></span> Create Article</a>
            </c:when>
            <c:otherwise>
                <h1>Smart Cookie</h1>
                <p>
                <p class="banner">Smart Cookie is a blog where you can upload your favourite recipes</p>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="row">

        <div class="col-xs-12 col-sm-6 align-left" style="text-align: left">
            <form id="sortingForm">
                <span>Sort by:</span>
                <select id="sorting" name="sorting">

                    <option selected value="newestArticleFirst" name="newestArticleFirst">Newest articles first</option>
                    <option name="oldestArticleFirst" value="oldestArticleFirst">Oldest articles first</option>
                    <option name="titleAsc" value="titleAsc">Recipe title (ascending)</option>
                    <option name="titleDesc" value="titleDesc">Recipe title (descending)</option>
                    <option name="unameAsc" value="unameAsc">Username (ascending)</option>
                    <option name="unameDesc" value="unameDesc">Username(descending)</option>
                </select>
            </form>
        </div>

        <div class="col-xs-12 col-sm-6 align-right" style="text-align: right">
            <input type="text" id="searchText" name="search" size="40" placeholder="Search for recipe title here">
            <span id="search" class="glyphicon glyphicon-search"></span>
        </div>
    </div>

    <div class="row display-flex">
        <div id="ajaxThumbnail" class="row display-flex"></div>
    </div>

</div>


    <!--Modal starts here-->
    <div class="row">

        <!-- Login / Register Modal-->
        <div class="modal fade login-register-form" role="dialog">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span class="glyphicon glyphicon-remove"></span>
                        </button>
                        <ul class="nav nav-tabs">
                            <li id="loginTab"><a data-toggle="tab" href="#login-form"><span
                                    class="glyphicon glyphicon-log-in"></span> Login</a></li>
                            <li id="signupTab"><a data-toggle="tab" href="#registration-form"><span
                                    class="glyphicon glyphicon-pencil"></span> Register</a></li>
                        </ul>
                    </div>
                    <div class="modal-body">
                        <div class="tab-content">
                            <div id="login-form">
                                <form action="LoginServlet" method="post">
                                    <div class="form-group">
                                        <label for="userID">Username/Email:</label>
                                        <input type="text" class="form-control" id="userID"
                                               placeholder="Enter your username" name="username" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="userPassword">Password:</label>
                                        <input type="password" class="form-control" id="userPassword"
                                               placeholder="Enter password" name="password">
                                    </div>
                                    <div class="checkbox">
                                        <label><input type="checkbox" name="remember"> Remember me</label>
                                    </div>
                                    <button type="submit" class="btn btn-default">Login</button><br />
                                    <a href="passwordRecovery.jsp">Forgot your password? Click here</a>
                                </form>
                            </div>
                            <div id="registration-form">
                                <form action="RegisterServlet" method="post">
                                    <div class="form-group">
                                        <label for="username">Your Username:</label>
                                        <input type="text" class="form-control" id="username"
                                               placeholder="Enter your username" name="username" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="firstname">Your First Name:</label>
                                        <input type="text" class="form-control" id="firstname"
                                               placeholder="Enter your first name" name="firstname" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="lastname">Your Last Name:</label>
                                        <input type="text" class="form-control" id="lastname"
                                               placeholder="Enter your last name" name="lastname" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="birthday">Your Birthday:</label>
                                        <input type="date" class="form-control" id="birthday"
                                               placeholder="Enter your birthday" name="birthday" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="country">Your Country:</label>
                                        <input type="text" class="form-control" id="country"
                                               placeholder="Enter your country" name="country" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="email">Email:</label>
                                        <input type="email" class="form-control" id="email"
                                               placeholder="Enter your email" name="email" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="password">Password:</label>
                                        <input type="password" class="form-control" id="password"
                                               placeholder="New password" name="password" pattern=".{4,}" title="Please enter at least 4 characters" required>
                                    </div>
                                    <div class="form-group">
                                        <label for="description">Description:</label>
                                        <textarea class="form-control" name="description" id="description" cols="40"
                                                  rows="2" required></textarea>
                                    </div>
                                    <div class="g-recaptcha"
                                         data-sitekey="6LdQB0MUAAAAAPPsP-ffo-oB4NGWmCDKqVfD7bRQ"></div>

                                    <button type="submit" class="btn btn-default">Register</button>
                                </form>
                            </div>

                        </div>
                    </div>

                </div>
            </div>
        </div>

    </div>

    <!--Modal ends here-->

<!--write article-->
<div class="modal fade write-article" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal">
                <span class="glyphicon glyphicon-remove"></span>
            </button>
        </div>
        <div class="modal-content">

            <div class="modal-body">
                <div class="tab-content">
                    <div id="new-article">
                        <form action="ArticleServlet" method="post" enctype="multipart/form-data">
                            <div class="form-group">
                                <label for="title">Title</label>
                                <input type="text" class="form-control" id="title" placeholder="Enter a title"
                                       name="title" required>
                            </div>
                            <div class="form-group">
                                <label for="articledescrip">Description</label>
                                <textarea class="form-control" name="recipedescrip" id="articledescrip" cols="40"
                                          rows="2" required></textarea>
                            </div>
                            <div class="form-group">
                                <label for="articleContent">Content</label>
                                <textarea  name="articleContent" id="articleContent" cols="40"
                                          rows="10"></textarea>
                            </div>

                            <script>
                                CKEDITOR.replace( 'articleContent' );
                            </script>

                            <div class="form-group">
                                <div class="input-append date form_datetime" data-date="2013-02-21T15:25:00Z">
                                    <label for="dtp_input1" class="col-md-3 control-label">Date and Time to post article</label>
                                    <input size="26" id="dtp_input1" name = "dateandtime" value="" type="text" placeholder="Will post immediately if not set" readonly>
                                    <span class="add-on"><i class="icon-remove"><span class="glyphicon glyphicon-remove"></span></i></span>
                                    <span class="add-on"><i class="icon-calendar"><span class="glyphicon glyphicon-th"></span></i></span>



                                <script type="text/javascript">
                                    $(".form_datetime").datetimepicker({
                                        format: "yyyy-mm-dd hh:ii:ss",
                                        autoclose: true,
                                        todayBtn: true,
                                        startDate: "2018-02-5 10:00:00",
                                        minuteStep: 10,
                                        pickerPosition: "top-right"
                                    });
                                </script>
                                </div>




                            </div>

                            <div class="form-group">
                                <label for="fileToUpload" class="col-md-4 control-label">Choose media to upload with article</label>
                                <input type="file" class="col-md-5" name="fileToUpload" id="fileToUpload" required multiple>
                            </div>
                            <br>
                            <br>
                            <input type="hidden" class="form-control" name="writingArticle" value="true">

                                <button type="submit" class="btn btn-default">Submit Article</button>

                        </form>
                        <div id="showMedia"></div>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>

<!-- Add article thumbnails in here (Responsive column widths -->

<div id="fading-div"></div>


<script src="modalAction.js" type="text/javascript"></script>
<script src="displayThumbnails.js" type="text/javascript"></script>
<script type="text/javascript" src="bootstrap-datetimepicker.js" charset="UTF-8"></script>
</body>
</html>
