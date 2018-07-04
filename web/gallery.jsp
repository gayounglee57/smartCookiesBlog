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
    <link href="https://fonts.googleapis.com/css?family=Alegreya+Sans" rel="stylesheet">
    <link rel="icon" href="images/cookies.ico">

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <!-- Bootstrap Date-Picker Plugin -->
    <script type="text/javascript" src="bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <link href="bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">


    <!--font-->

    <!--custom css-->
    <link rel="stylesheet" href="index.css" type="text/css">

</head>
<body>


<nav class="navbar navbar-fixed-top" style="z-index: 1">
    <div class="container-fluid" style="z-index: 1">
        <div class="navbar-header">
            <a class="navbar-brand" id="home" href="index.jsp">SmartCookie</a>
        </div>
        <ul class="nav navbar-nav">
        </ul>

        <ul class="nav navbar-nav navbar-right">

            <c:choose>
                <c:when test="${sessionScope.name != null}">

                    <li id="avatarButton"><a href="profilePage.jsp?fromProfilePage=1"><span
                            class="glyphicon glyphicon-user"></span> Profile</a></li>

                    <li id="logoutButton"><a href="LoginServlet?logout=true"><span
                            class="glyphicon glyphicon-log-out"></span> Logout</a></li>

                </c:when>


                <c:otherwise>
                    <li id="loginButton"><a href="#" data-toggle="modal" data-target=".login-register-form"><span
                            class="glyphicon glyphicon-log-in"></span> Login</a></li>
                    <li id="signupButton"><a href="#" data-toggle="modal" data-target=".login-register-form"><span
                            class="glyphicon glyphicon-pencil"></span> Register</a></li>
                </c:otherwise>
            </c:choose>

        </ul>
    </div>
</nav>


<div class="container" style="z-index: 2">
    <div class="row">
        <div class="col-xs-4"></div>
        <div class="col-xs-4">
            <nav id="loginStatus" class="navbar">
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
                    <c:when test="${deleted != null}">
                        <div class="alert alert-info" role="alert">
                            Article successfully deleted
                        </div>
                    </c:when>
                </c:choose>
            </nav
        </div>
        <div class="col-xs-4"></div>
    </div>
</div>


<div class="container">


    <div id="banner" class="banner jumbotron">

        <c:choose>
            <c:when test="${sessionScope.name != null}">
                <h1>Hi there ${sessionScope.name}</h1><br/>

                <a href="#" data-toggle="modal" data-target=".write-article"><span
                        class="glyphicon glyphicon-plus"></span> Create Article</a>
                <br>
                <a href="MultimediaServlet?allMedia=true"><span
                        class="glyphicon glyphicon-picture"></span> All Media</a>
                <br>
                <a href="MultimediaServlet?allMedia=false"><span
                        class="glyphicon glyphicon-picture"></span> My Media</a>

            </c:when>
            <c:otherwise>
                <h1>Smart Cookie</h1>
                <p>
                <p class="banner">Smart Cookie is a blog where you can upload your favourite recipes</p></p>
            </c:otherwise>
        </c:choose>

    </div>

    <!-- thumbnails added here -->

    <!--displaying images-->
    <br>
    <br>
    <h2>Image</h2>
    <c:if test="${numjpg >= 0}">
        <div class="row display-flex">
            <c:forEach var="i" begin="0" end="${numjpg}">
                <c:set var="image" value="${requestScope['jpg'.concat(i)]}"/>
                <div class="col-sm-6 col-md-4">
                    <div class="thumbnail">
                        <img src="${image}">
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${numjpeg >= 0}">
        <c:forEach var="i" begin="0" end="${numjpeg}">
            <c:set var="image" value="${requestScope['jpeg'.concat(i)]}"/>
            <div class="col-sm-6 col-md-4">
                <div class="thumbnail">
                    <img src="${image}">
                </div>
            </div>
        </c:forEach>
    </c:if>

    <c:if test="${numpng >= 0}">
        <c:forEach var="i" begin="0" end="${numpng}">
            <div class="col-sm-6 col-md-4">
                <div class="thumbnail">
                    <c:set var="image" value="${requestScope['png'.concat(i)]}"/>
                    <img src="${image}">
                </div>
            </div>
        </c:forEach>
    </c:if>

    <!--displaying audios-->
    <br>
    <br>
    <h2>Audio</h2>
    <c:if test="${nummp3 >= 0}">
        <div class="row display-flex">
            <c:forEach var="i" begin="0" end="${nummp3}">
                <c:set var="audio" value="${requestScope['mp3'.concat(i)]}"/>
                <div class='col-sm-12 col-md-6 col-lg-4 imageDisplay'>
                    <div class="thumbnail">
                        <audio controls>
                            <source src="${audio}" type="audio/mpeg">
                            Your browser does not support the audio
                        </audio>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${numwav >= 0}">
        <div class="row display-flex">
            <c:forEach var="i" begin="0" end="${numwav}">
                <c:set var="audio" value="${requestScope['wav'.concat(i)]}"/>
                <div class='col-sm-12 col-md-6 col-lg-4'>
                    <div class="thumbnail">
                    <audio controls>
                        <source src="${audio}" type="audio/mpeg">
                        Your browser does not support the audio
                    </audio>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <!--displaying videos-->
    <br>
    <br>
    <h2>Video</h2>
    <c:if test="${nummp4 >= 0}">
        <div class="row display-flex">
            <c:forEach var="i" begin="0" end="${nummp4}">
                <c:set var="video" value="${requestScope['mp4'.concat(i)]}"/>
                <div class='col-sm-12 col-md-6 col-lg-4'>
                    <div class="thumbnail">
                    <video width="320" height="240" controls>
                        <source src="${video}" type="video/mp4">
                        Your browser does not support the video
                    </video>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${numwebm >= 0}">
        <div class="row display-flex">
            <c:forEach var="i" begin="0" end="${numwebm}">
                <c:set var="video" value="${requestScope['webm'.concat(i)]}"/>
                <div class='col-sm-12 col-md-6 col-lg-4'>
                    <div class="thumbnail">
                    <video width="320" height="240" controls>
                        <source src="${video}" type="video/mp4">
                        Your browser does not support the video
                    </video>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

</div>


<!--Modal starts here-->
<!--<div class="container">-->
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
                                           placeholder="Enter your username" name="username">
                                </div>
                                <div class="form-group">
                                    <label for="userPassword">Password:</label>
                                    <input type="password" class="form-control" id="userPassword"
                                           placeholder="Enter password" name="password">
                                </div>
                                <div class="checkbox">
                                    <label><input type="checkbox" name="remember"> Remember me</label>
                                </div>
                                <button type="submit" class="btn btn-default">Login</button>
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
                                           placeholder="New password" name="password" required>
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
                        <form action="ArticleServlet" method="post">
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
                                <textarea class="form-control" name="articleContent" id="articleContent" cols="40"
                                          rows="10"></textarea>
                            </div>

                            <div class="form-group">
                                <label for="dtp_input1" class="col-md-3 control-label">Date and Time to post
                                    article</label>
                                <div class="input-group date form_datetime col-md-5" data-date="1979-09-16T05:25:07Z"
                                     data-date-format="dd MM yyyy - HH:ii p" data-link-field="dtp_input1">
                                    <input class="form-control" size="20" type="text" value="" required>
                                    <span class="input-group-addon"><span
                                            class="glyphicon glyphicon-remove"></span></span>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                                    <input type="hidden" name="dateandtime" id="dtp_input1" value="" required/><br/>
                                </div>


                                <script type="text/javascript">
                                    $(".form_datetime").datetimepicker({
                                        format: "dd MM yyyy - hh:ii",
                                        autoclose: true,
                                        todayBtn: true,
                                        startDate: "2013-02-14 10:00",
                                        minuteStep: 10
                                    });
                                </script>
                            </div>

                            <div class="form-group">
                                <label for="fileToUpload" class="col-md-4 control-label">Choose media to upload with article</label>
                                <input type="file" class="col-md-5" name="fileToUpload" id="fileToUpload" required multiple>
                            </div>
                            <br>
                            <br>
                                <button type="submit" class="btn btn-default">Submit Article</button>
                        </form>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>

<div id="fading-div"></div>
</div>

<script src="modalAction.js" type="text/javascript"></script>
<script type="text/javascript" src="bootstrap-datetimepicker.js" charset="UTF-8"></script>
</body>
</html>
