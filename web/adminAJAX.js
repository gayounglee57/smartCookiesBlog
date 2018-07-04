/**
 * Created by mmIZ318 on 30/01/2018.
 */
ajaxArticlesCall(null, 6);
ajaxUsersCall();

var id;
var className;

//modal tab highlighting
$("#adminArticles").on("click", ".article", function(){
    $('#details').empty();
    $('#showComments').empty();
    id = $(this).attr('id');
    className = $(this).attr('class');
    ajaxArticleDetailsCall();
});
$("#adminUsers").on("click", ".user", function(){
    $('#details').empty();
    $('#showComments').empty();
    id = $(this).attr('id');
    className = $(this).attr('class');
    ajaxUserDetailsCall();
});


function ajaxArticlesCall(pageReload,noToLoad) {
//This handles the AJAX calls to load articles

    $.ajax({

        url: "ArticleServlet",
        type: "GET",
        data: {isPageReloaded: pageReload, articlesToLoad: noToLoad, "userArticles": status},
        success: function (responseText) {
            var jsonObject = JSON.parse(responseText);

            for (var i = 0; i < Object.keys(jsonObject).length; i++) {
                var description = jsonObject[i].description;
                var title = jsonObject[i].title;
                var id = jsonObject[i].id;
                var user = jsonObject[i].userLoggedIn;
                var author = jsonObject[i].author;
                var article_panel =
                    "<button type='button' class='list-group-item article' id='" + id + "'>\"" + title + "\" by " + author + "</a>";

                $("#adminArticles").append(article_panel);

            }
        }
    });

}
    function ajaxUsersCall() {

        $.ajax({
            url: "AdminServlet",
            type: "POST",
            success: function (responseText) {
                var jsonObject = JSON.parse(responseText);

                for (var i = 0; i < Object.keys(jsonObject).length; i++) {
                    var uname = jsonObject[i];

                    var user_panel =
                        "<button type='button' class='list-group-item user' id='" + uname +"'>" + uname + "</button>";
                    $("#adminUsers").append(user_panel);
                }
            }
        });
    }




    function ajaxArticleDetailsCall() {
        console.log("AJAX");
        $.ajax({
            url: "AdminServlet",
            type: "GET",
            data: {"id": id, "className": className},
            success: function (responseText) {

                var jsonObject = JSON.parse(responseText);

                var title = jsonObject["title"];
                var description = jsonObject["description"];
                var content = jsonObject["content"];
                var author = jsonObject["author"];

                var article_panel =
                    "<a href='ArticleServlet?hide=false&id=" + jsonObject["articleID"] + "'>Show</a>" + "<br>" +
                    "<a href='ArticleServlet?hide=true&id=" + jsonObject["articleID"] + "'>Hide</a>" +
                    "<h3>Title: " + title + "</h3>" +
                        "<p>Description: " + description + "</p>" +
                        "<p>Content: " + content + "</p>";

                $("#details").append(article_panel);
                $("#showComments").append("<h4>Article Comments:</h4>");
                for (var i = 0; i < Object.keys(jsonObject).length - 4; i++){
                    var comment = jsonObject[i];
                    var commentContent = comment["commentContent"];
                    var commentAuthor = comment["commentAuthor"];
                    var comment_panel =
                        "<tr><td>" + commentContent + " - <i>" + commentAuthor + "</i></td>" +
                            "<td><a href='CommentServlet?hide=false&id=" + comment["commentID"] +"'>Show</a>&emsp;<a href='CommentServlet?hide=true&id=" + comment["commentID"] +"'>Hide</a></td></tr>";
                    $("#showComments").append(comment_panel);
                }


            }

    });

}
function ajaxUserDetailsCall() {
    console.log("AJAX");
    $.ajax({
        url: "AdminServlet",
        type: "GET",
        data: {"id": id, "className": className},
        success: function (responseText) {

            var jsonObject = JSON.parse(responseText);

            var username = jsonObject["username"];
            var fname = jsonObject["fname"];
            var lname = jsonObject["lname"];

            var article_panel =
                "<h3>Username: " + username + "</h3>" +
                "<p>Name: " + fname + " " + lname + "</p>" +
                "<a href='RegisterServlet?admin=" + username +"' onclick='deleteFunction()'>Delete User Account</a>";

            $("#details").append(article_panel);
            $("#showComments").append("<h4>Authored Articles:</h4>");
            for (var i = 0; i < Object.keys(jsonObject).length - 3; i++) {
                var article = jsonObject[i];
                var comment_panel =
                    "<tr><td>" + article["article"] + "</td>" +
                    "<td><a href='ArticleServlet?hide=false&id=" + article["articleID"] + "'>Show</a>" +
                    "<a href='ArticleServlet?hide=true&id=" + article["articleID"] + "'>Hide</a></td></tr>";
                $("#showComments").append(comment_panel);
            }
        }
    });
}
