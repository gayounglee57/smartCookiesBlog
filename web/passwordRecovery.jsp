<%--
  Created by IntelliJ IDEA.
  User: mmiz318
  Date: 1/02/2018
  Time: 3:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Admin Interface</title>
    <link href="https://fonts.googleapis.com/css?family=Alegreya+Sans" rel="stylesheet">

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">


    <%--Icon--%>
    <link rel="icon" href="images/cookies.ico">


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <!--font-->

    <!--custom css-->
    <link rel="stylesheet" href="index.css" type="text/css">

    <title>Password Recovery</title>
</head>
<body>
<div>
<nav class="navbar navbar-fixed-top" style="z-index: 1">
    <div class="container-fluid" style="z-index: 1">
        <div class="navbar-header">
            <a class="navbar-brand" id="home" href="index.jsp">SmartCookie</a>
        </div>
        <ul class="nav navbar-nav">
        </ul>
    </div>
</nav>
</div>
<div class="container">
<div id="banner" class="banner jumbotron">

    <h1>Smart Cookie</h1>
    <p>
    <p class="banner">Smart Cookies is a blog where you can upload your favourite recipes</p></p>

</div>


<div class="row">

<div class="col-xs-12 col-sm-12 col-md-4 col-lg-4" id="passwordRecovery">
    <form action="RegisterServlet" method="post">
        <label for="email">Please enter your email address:</label><br />
        <input type="email" id="email" name="resetPass">
        <div class="g-recaptcha" data-sitekey="6LdQB0MUAAAAAPPsP-ffo-oB4NGWmCDKqVfD7bRQ"></div>
        <button type="submit" class="btn btn-default">Reset Password</button>
    </form>

</div>
</div>

</div>
</body>
</html>
