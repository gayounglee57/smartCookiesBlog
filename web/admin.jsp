<%--
  Created by IntelliJ IDEA.
  User: mmIZ318
  Date: 30/01/2018
  Time: 1:12 PM
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


    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script src="https://www.google.com/recaptcha/api.js" async defer></script>
    <!--font-->

    <!--custom css-->
    <link rel="stylesheet" href="index.css" type="text/css">
</head>
<div>
<nav class="navbar">
    <div class="container-fluid">

        <ul class="nav navbar-nav">
        </ul>

        <ul class="nav navbar-nav navbar-right">
            <li id="logoutButton"><a href="LoginServlet?logout=true"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li></ul>
    </div>
</nav>
</div>

<div class="container">

        <div id = "banner" class="jumbotron banner">
            <h1>Hi there ${sessionScope.name}</h1><br/>
            <p><p class="banner">Admin Interface</p></p>
        </div>

<div class="row">
    <div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
        <h1>Edit Users</h1>
        <div id="adminUsers" class="list-group">
            <a  data-toggle="modal" data-target=".register-form" class="list-group-item active">
                <span class="glyphicon glyphicon-plus"></span> Add New User
            </a>

        </div>
    </div>
    <div class="col-xs-12 col-sm-12 col-md-4 col-lg-4">
        <h1>Edit Articles</h1>
        <div id="adminArticles" class="list-group">

        </div>
    </div>
    <div class="col-xs-12 col-sm-12 col-md-2 col-lg-2">
        <h1>Details</h1>
        <div id="details"></div>
        <table id="showComments">

        </table>

    </div>

</div>
<!--New User Modal -->
    <div class="row">
        <div class="modal fade register-form" role="dialog">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">
                            <span class="glyphicon glyphicon-remove"></span>
                        </button>
                    </div>
                    <div class="modal-body">
                    <div class="tab-content">

                    <div id="registration-form">
                    <form action="RegisterServlet" method="post">
                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" class="form-control" id="username" placeholder="Enter your username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="firstname">First Name:</label>
                        <input type="text" class="form-control" id="firstname" placeholder="Enter your first name" name="firstname">
                    </div>
                    <div class="form-group">
                        <label for="lastname">Last Name:</label>
                        <input type="text" class="form-control" id="lastname" placeholder="Enter your last name" name="lastname">
                    </div>
                    <div class="form-group">
                        <label for="birthday">Birthday:</label>
                        <input type="date" class="form-control" id="birthday" placeholder="Enter your birthday" name="birthday">
                    </div>
                    <div class="form-group">
                        <label for="country">Country:</label>
                        <input type="text" class="form-control" id="country" placeholder="Enter your country" name="country">
                    </div>
                    <div class="form-group">
                        <label for="email">Email:</label>
                        <input type="email" class="form-control" id="email" placeholder="Enter your email" name="email">
                    </div>
                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="password" class="form-control" id="password" placeholder="New password" name="password" pattern=".{4,}" title="Please enter at least 4 characters" required>
                    </div>
                    <div class="form-group">
                        <label for="description">Description:</label>
                        <textarea class="form-control" name="description" id="description" cols="40" rows="5"></textarea>
                    </div>
                        <label for="makeAdmin">Make this user an admin:</label>
                        <input type="checkbox" name="makeAdmin" id="makeAdmin">
                    <button type="submit" class="btn btn-default">Register</button>
                </form>
            </div>
        </div>
        </div>
    </div>
            </div>
        </div>
    <!--Edit Users Modal Ends -->
    </div>
</div>
<script src="adminAJAX.js" type="text/javascript" ></script>
<script src="profilePageAjax.js" type="text/javascript" ></script>
<script src="articleEditing.js" type="text/javascript"></script>
</body>
</html>
