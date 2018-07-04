<%--
  Created by IntelliJ IDEA.
  User: glee156
  Date: 23/01/2018
  Time: 1:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>

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

    <link rel="stylesheet" href="index.css" type="text/css">
    <link rel="stylesheet" href="profilePage.css" type="text/css">

    <title>Your Profile</title>
    <meta http-equiv="Cache-Control" content="no-cache"/>
</head>

<body>

<c:choose>

    <c:when test="${sessionScope.name == null}">
        <c:redirect url="index.jsp"/>
    </c:when>
</c:choose>

<div class="container" id="profile">
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
                    <c:when test="${deleted != null}">
                        <div class="alert alert-info" role="alert">
                            Article successfully deleted
                        </div>
                    </c:when>
                    <c:when test="${reset != null}">
                        <div class="alert alert-info" role="alert">
                            Modify your password here
                        </div>
                    </c:when>
                </c:choose>
            </div>
        </div>
        <div class="col-xs-4"></div>
    </div>
</div>


<nav class="navbar navbar-fixed-top">
    <div class="container-fluid">
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

                <li id="avatarButton"><a href="MultimediaServlet?allMedia=true"><span
                        class="glyphicon glyphicon-picture"></span> Gallery</a></li>

                <li id="logoutButton"><a href="LoginServlet?logout=true" data-toggle="modal" data-target=""><span
                        class="glyphicon glyphicon-log-out"></span> Logout</a></li>
            </ul>
        </div>
    </div>
</nav>


<div class="container">

    <div id="banner" class="jumbotron banner">

        <h1>Hi there ${sessionScope.name}</h1><br/>
        <a href="#" data-toggle="modal" data-target=".write-article"><span class="glyphicon glyphicon-plus"></span>
            Create Article</a>
    </div>


    <div class="row">
        <div class="col-xs-12 col-sm-6 col-md-3">
            <!-- start of avatar section (far left)------------------------------------>
            <div class="container avatar col-xs-12 col-sm-6 col-sm-3">
                <form id="avatarForm" name="myForm" action="ProfilePageServlet" method="POST"
                      enctype="multipart/form-data">
                    <fieldset>

                        <div id="personalAvatar"></div>

                        <!--start of pre-defined avatars for user to choose from -->
                        <div class="row">
                            <div width="50">
                                <div class="thumbnail1">
                                    <a href="ProfilePageServlet?image=cookie_chips.png"><img
                                            src="images\cookie_chips.png"
                                            alt="cookie_chips" width="50"></a>
                                </div>
                            </div>

                            <div width="50">
                                <div class="thumbnail1">
                                    <a href="ProfilePageServlet?image=cookie_double.png"><img
                                            src="images\cookie_double.png"
                                            alt="cookies_double"
                                            width="50"></a>
                                </div>
                            </div>

                            <div width="50">
                                <div class="thumbnail1">
                                    <a href="ProfilePageServlet?image=bright_cookie.png"><img
                                            src="images\bright_cookie.png"
                                            alt="cookies_double"
                                            width="50"></a>
                                </div>
                            </div>

                            <div width="50">
                                <div class="thumbnail1">
                                    <a href="ProfilePageServlet?image=brave_cookie.png"><img
                                            src="images\brave_cookie.png"
                                            alt="cookies_double"
                                            width="50"></a>
                                </div>
                            </div>
                        </div>

                        <!--end of pre-defined avatars for user to choose from -->

                        <label for="fileToUpload">Click above or choose your own avatar file to upload</label>
                        <input id="fileUploadButton" type="file" name="fileToUpload" required>
                        <input type="submit" value="Save" formmethod="POST">
                    </fieldset>
                </form>
            </div>
        </div>
        <!--end of avatar section------------------------------------>
        <div class="col-xs-12 col-sm-6">
            <!--start of user info section------------------------------->
            <div class="container info col-xs-12 col-sm-9">
                <form id='userform_id' name='userform' method='post' action='RegisterServlet'>
                    <fieldset>
                        <legend>Your Profile Information:</legend>
                        <div id="profileInfo"></div>
                        <input type="hidden" name="profilePage" value="true">
                        <input type='submit' name='submit_button' id='submit_id' value='Save'/>

                    </fieldset>
                </form>
            </div>
            <div class="container info col-xs-12 col-sm-9">
                <form id='deleteButton' action='RegisterServlet' method="get" onclick="return deleteFunction()">
                    <input type='submit' name='delete_button' id='delete_id' value='Delete account'>
                </form>
            </div>
        </div>
        <!--end of user info section------------------------------->

    </div>


    <div class="row display-flex">
        <h2>My Articles</h2>
        <div id="ajaxThumbnail"></div>
    </div>

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
                                    <label for="recipedescrip">Description</label>
                                    <textarea class="form-control" name="recipedescrip" id="recipedescrip" cols="40"
                                              rows="2" required></textarea>
                                </div>
                                <div class="form-group">
                                    <label for="articleContent">Content</label>
                                    <textarea name="articleContent" id="articleContent" cols="40"
                                              rows="10"></textarea>
                                </div>

                                <script>
                                    CKEDITOR.replace('articleContent');
                                </script>

                                <div class="form-group">
                                    <div class="input-append date form_datetime" data-date="2013-02-21T15:25:00Z">
                                        <label for="dtp_input1" class="col-md-3 control-label">Date and Time to post
                                            article</label>
                                        <input size="26" id="dtp_input1" name="dateandtime" value="" type="text"
                                               placeholder="Will post immediately if not set" readonly>
                                        <span class="add-on"><i class="icon-remove"><span
                                                class="glyphicon glyphicon-remove"></span></i></span>
                                        <span class="add-on"><i class="icon-calendar"><span
                                                class="glyphicon glyphicon-th"></span></i></span>


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
                                    <label for="fileToUpload" class="col-md-4 control-label">Choose media to upload with
                                        article</label>
                                    <input type="file" class="col-md-5" name="fileToUpload" id="fileToUpload" required
                                           multiple>
                                </div>
                                <br>
                                <br>


                                <!--hidden parameter to identify creating article on profile page-->
                                <input type="hidden" name="writingFromProfilePage" value="true"/>
                                <input type="hidden" class="form-control" name="writingArticle" value="true">
                                <button type="submit" class="btn btn-default">Submit Article</button>
                            </form>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>

</div>
<!--end of container------------------------------->

<script src="profilePageAjax.js" type="text/javascript"></script>
<script src="displayThumbnails.js" type="text/javascript"></script>
<script type="text/javascript" src="bootstrap-datetimepicker.js" charset="UTF-8"></script>
</body>
</html>
