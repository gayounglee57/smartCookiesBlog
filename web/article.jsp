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


<%--Recaptcha--%>
  <script src='https://www.google.com/recaptcha/api.js'></script>


  <!-- Bootstrap Date-Picker Plugin -->
  <script type="text/javascript" src="bootstrap-datetimepicker.js" charset="UTF-8"></script>
  <link href="bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">

  <%--Form validation for Youtube links--%>
  <script type="text/javascript" src="//cdn.jsdelivr.net/jquery.bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>

  <%--Formatting Text in writing article--%>
  <script src="https://cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>



  <!--custom css-->
  <link rel="stylesheet" href="index.css" type="text/css">

</head>
<body>

<div class="container">
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

      <c:choose>
        <c:when test="${sessionScope.name != null}">
          <li id="avatarButton"><a href="MultimediaServlet?allMedia=true"><span
                  class="glyphicon glyphicon-picture"></span> Gallery</a></li>

          <li id="avatarButton"><a href="ProfilePageServlet?image=cookie_outline.png&userArticles=true" data-toggle="modal" data-target=""><span
                  class="glyphicon glyphicon-user"></span> Profile</a></li>

          <li id="logoutButton"><a href="LoginServlet?logout=true" data-toggle="modal" data-target=""><span
          class="glyphicon glyphicon-log-out"></span> Logout</a></li>

      </c:when>
        <c:otherwise>
          <li id="avatarButton"><a href="MultimediaServlet?allMedia=true"><span
                  class="glyphicon glyphicon-picture"></span> Gallery</a></li>

          <li id="loginButton"><a href="#" data-toggle="modal" data-target=".login-register-form"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
          <li id="signupButton"><a href="#" data-toggle="modal" data-target=".login-register-form"><span class="glyphicon glyphicon-pencil"></span> Register</a></li>
      </c:otherwise>
      </c:choose>

    </ul>
  </div>
  </div>
</nav>

<div class="container">

  <div class="row">

  <div id="banner" class="jumbotron banner">
    <div class="row">
      <div class="col-sm-10" id="articleDisplay">
        <h1>${title}</h1>
        <p>${description}</p>
        <br />
        <p><small><i>Created ${date} by ${user}</i></small></p>
        <div id="hiddenIdDiv" name="hiddenIdDiv" value="${id}" style="visibility: hidden">${id}</div>
      </div>
      <div class="col-sm-2">
        <%--<img class="img-responsive pull-right" src="images/cookie.jpg">--%>
          <img class="img-responsive pull-right" src=${avatar}>
      </div>

      <!--show if author == user logged in -------->
      <c:if test="${user == sessionScope.username}">

        <div class="container col-sm-2">
          <a href="#" data-toggle="modal" data-target=".write-article"><span class="glyphicon glyphicon-pencil"></span> Edit Text</a>
          <br>
          <a href="ArticleServlet?delete=${id}" onclick="return deleteArticleFunction()"><span class="glyphicon glyphicon-trash"></span> Delete Article</a>
          <br>
          <a href="#" data-toggle="modal" data-target=".write-photo"><span class="glyphicon glyphicon-plus"></span> Upload Multimedia</a>
          <br>
          <a href="#" data-toggle="modal" data-target=".delete-photo"><span class="glyphicon glyphicon-trash"></span> Delete Multimedia</a>
          <br>
          <a href="#" data-toggle="modal" data-target=".write-youtube"><span class="glyphicon glyphicon-plus"></span> Embed Youtube video</a>
          <br>
        </div>


        <!-- start of write article modal -->

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
                        <input type="text" class="form-control" id="title" value="${title}" name="title">
                      </div>
                      <div class="form-group">
                        <label for="recipedescrip">Description</label>
                        <textarea class="form-control" name="recipedescrip" id="recipedescrip" cols="40" rows="2">${description}</textarea>
                      </div>
                      <%--<div class="form-group">--%>
                        <%--<label for="articleContent">Content</label>--%>
                        <%--<textarea class="form-control" name="articleContent" id="articleContent" cols="40" rows="10"></textarea>--%>
                      <%--</div>--%>

                      <div class="form-group">
                        <label for="articleContent">Content</label>
                        <textarea  name="articleContent" id="articleContent" cols="40"
                                   rows="10">${content}</textarea>
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



                      <input type="hidden" class="form-control" name="writingArticle" value="true">
                      <input type="hidden" class="form-control" name="articleId" value="${id}">
                      <button type="submit" class="btn btn-default">Save Changes</button>
                      <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    </form>
                  </div>

                </div>
              </div>

            </div>
          </div>
        </div>

        <!-- end of write article modal -->



        <!-- add photo modal -->
        <div class="modal fade write-photo" role="dialog">
          <div class="modal-dialog modal-lg">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                <span class="glyphicon glyphicon-remove"></span>
              </button>
            </div>
            <div class="modal-content">

              <div class="modal-body">
                <div class="tab-content">
                  <div id="new-photo">
                    <!-- form for adding images to articles-->
                    <form action="ArticleServlet" method="post" enctype="multipart/form-data">
                      <div class="form-group">
                        <label for="fileUploadButton">Upload multimedia</label>
                        <input type="hidden" class="form-control" name="writingArticle" value="true">
                        <input id="fileUploadButton" type="file" name="fileToUpload" required multiple>
                        <input type="hidden" class="form-control" name="articleid" value="${id}">
                        <input type="hidden" value="${sessionScope.username}" name="userLoggedIn">
                        <input type="hidden" value="${user}" name="author">
                        <input type="submit" value="Save">
                      </div>
                    </form>
                  </div>

                </div>
              </div>

            </div>
          </div>
        </div>
        <!-- end of add photo modal-->


        <!-- delete photo modal -->
        <div class="modal fade delete-photo" role="dialog">
          <div class="modal-dialog modal-lg">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                <span class="glyphicon glyphicon-remove"></span>
              </button>
            </div>
            <div class="modal-content">

              <div class="modal-body">
                <div class="tab-content">
                  <div id="delete-photo">
                    <!-- form for deleting images from articles-->
                    <form action="MultimediaServlet" method="get">
                      <div class="form-group">
                        <c:if test="${numOfImages >= 0}">
                          <label>Delete multimedia</label>
                          <br>
                          <c:forEach var = "i" begin = "0" end = "${numOfImages}">
                            <c:set var="image" value="${requestScope['image'.concat(i)]}" />
                            <input type="checkbox" name="${i}" value="${image}"> Delete ${image}<br>
                          </c:forEach>
                        </c:if>
                        <!-- hidden input field with username of person logged in to compare with comment author names for delete/edit buttons-->
                        <input type="hidden" class="form-control" name="articleid" value="${id}">
                        <input type="hidden" value="${sessionScope.username}" name="userLoggedIn">
                        <input type="hidden" value="${user}" name="author">
                        <input type="submit" value="Delete" name="deletePhotoButton">
                      </div>
                    </form>
                  </div>

                </div>
              </div>

            </div>
          </div>
        </div>
        <!-- end of delete photo modal-->

        <!-- youtube modal -->
        <div class="modal fade write-youtube" role="dialog">
          <div class="modal-dialog modal-lg">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">
                <span class="glyphicon glyphicon-remove"></span>
              </button>
            </div>
            <div class="modal-content">

              <div class="modal-body">
                <div class="tab-content">
                  <div id="youtube">
                    <form action="MultimediaServlet" method="get" id="youtube-form" class="form-horizontal"
                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">

                      <div class="form-group">
                        <div class="col-sm-8">
                          <label>Enter a Youtube link</label>
                          <div>
                            <input type="text" class="form-control" name="youtube-url"
                                   pattern="www\.youtube\.com(?!\/embed)"
                                   data-bv-regexp-message="Please enter a url from youtube" required/>
                          </div>
                        </div>
                      </div>

                            <!-- hidden input field with username of person logged in to compare with comment author names for delete/edit buttons-->
                          <input type="hidden" class="form-control" name="youtubeLink" value="true">
                          <input type="hidden" class="form-control" name="articleid" value="${id}">
                          <input type="hidden" value="${sessionScope.username}" name="userLoggedIn">
                          <input type="hidden" value="${user}" name="author">

                          <input type="submit" value="Submit" name="youtubeButton">


                    </form>
                  </div>

                </div>
              </div>

            </div>
          </div>
        </div>
        <!-- end of youtube modal-->


        </c:if>
      <!--user view edit modal ends here ----------------->


      <!-- edit comment modal starts here ---------------->
      <div id="commentModal"></div>
      <!-- edit comment modal ends here-------------->

    </div>
  </div>



  <!--start of comment section------------------->
    <div class="container">

      <div class="row">
      <div class="panel panel-default">
        <div class="panel-body" id="ct">
         <c:out value="${content}" escapeXml="false"/>
        </div>
        <div id="audioDisplay"></div>
        <div id="videoDisplay"></div>
        <div id="youtubeDisplay"></div>
      </div>
      <br>
      </div>

      <div class="row">
      <h4>Comments</h4>

      <c:choose>
        <c:when test="${sessionScope.name != null}">
          <form id="commentForm" name="myForm" action="CommentServlet" method="GET">
            <textarea class="form-control" name="comment" id="initialComment" cols="10" rows="2"
                      placeholder="Write something, ${sessionScope.name}!"></textarea>
            <!-- hidden input field with username of person logged in to compare with comment author names for delete/edit buttons-->
            <input type="hidden" value="${sessionScope.username}" name="userLoggedIn">
            <div id="addComment" style="cursor: pointer"><a><span class="glyphicon glyphicon-plus"></span> Add Comment</a></div>
          </form>
        </c:when>
      </c:choose>
    <div id="commentsSection"></div>
    </div>
    </div>

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
            <li id="loginTab"><a data-toggle="tab" href="#login-form"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
            <li id="signupTab"><a data-toggle="tab" href="#registration-form"><span class="glyphicon glyphicon-pencil"></span> Register</a></li>
          </ul>
        </div>
        <div class="modal-body">
          <div class="tab-content">
            <div id="login-form">
              <form action="LoginServlet" method="post" class="redirect" value="" id="loginForm">
                <div class="form-group">
                  <label for="userID">Username/Email:</label>
                  <input type="text" class="form-control" id="userID" placeholder="Enter your username" name="username" required>
                </div>
                <div class="form-group">
                  <label for="userPassword">Password:</label>
                  <input type="password" class="form-control" id="userPassword" placeholder="Enter password" name="password" required>
                </div>
                <div class="checkbox">
                  <label><input type="checkbox" name="remember"> Remember me</label>
                </div>
                <a href="passwordRecovery.jsp">Forgot your password? Click here</a><br />
                <button type="submit" class="btn btn-default">Login</button>
              </form>
            </div>
            <div id="registration-form">
              <form action="RegisterServlet" method="post" class="redirect">
                <div class="form-group">
                  <label for="username">Your Username:</label>
                  <input type="text" class="form-control" id="username" placeholder="Enter your username" name="username">
                </div>
                <div class="form-group">
                  <label for="firstname">Your First Name:</label>
                  <input type="text" class="form-control" id="firstname" placeholder="Enter your first name" name="firstname">
                </div>
                <div class="form-group">
                  <label for="lastname">Your Last Name:</label>
                  <input type="text" class="form-control" id="lastname" placeholder="Enter your last name" name="lastname">
                </div>
                <div class="form-group">
                  <label for="birthday">Your Birthday:</label>
                  <input type="date" class="form-control" id="birthday" placeholder="Enter your birthday" name="birthday">
                </div>
                <div class="form-group">
                  <label for="country">Your Country:</label>
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
                <div class="g-recaptcha" data-sitekey="6LdQB0MUAAAAAPPsP-ffo-oB4NGWmCDKqVfD7bRQ"></div>
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
<%--ajax call when we scroll + modal tab handling on login/register modal--%>
<script src="modalAction.js" type="text/javascript" ></script>
<%--loads comments for article page--%>
<script src="commentsAjax.js" type="text/javascript" ></script>
<%--loads multimedia for article page and checks youtube url--%>
<script src="imagesAjax.js" type="text/javascript" ></script>
<%--warning alert before deleting article--%>
<script src="articleEditing.js" type="text/javascript" ></script>
<%--Date and time picker--%>
<script type="text/javascript" src="bootstrap-datetimepicker.js" charset="UTF-8"></script>
</body>
</html>