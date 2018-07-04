var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};
console.log(window.location.href);
var articleid = getUrlParameter('articleid');
var user = getUrlParameter('userLoggedIn');
console.log("this user is " + user);
var articleAuthor = getUrlParameter('author');
var parentid = 0;

console.log("content of article is " + $("#ct").html());

// Handles login redirect
var line = '<input type="hidden" value="' + articleid + '" name="redirectId">';
$("#loginForm").append(line);


//Loading image
$("#commentsSection").append('<div id="loading"><p>Loading Comments <img src="images/giphy.gif" /></p></div>')
commentsCall();

$(document).ready(function () {
    $("#addComment").on("click", function () {

        var commentcontent = $("#initialComment").val();


        if (commentcontent == "") {
            return;
        }
        if (commentcontent == null) {
            return;
        }

        $.ajax({
            url: "CommentServlet",
            type: "GET",
            data: {
                "articleid": articleid,
                "username": user,
                "Author": articleAuthor,
                "parentid": parentid,
                "comment": commentcontent
            },

            error: function () {
                console.log("error add new comment");
            },
            success: function (response) {


                console.log("successfully written comment");

                var jsonObject = JSON.parse(response);
                user = jsonObject["User"];
                var commentJson = {
                    "0": {
                        "Avatar": jsonObject["Avatar"],
                        "Username": jsonObject["User"],
                        "Date": jsonObject["Date"],
                        "CommentId": jsonObject["CommentId"],
                        "Comment": commentcontent
                    }
                };

                //create JSONObject here

                $(appendComment(commentJson, "0",user)).appendTo("#commentsSection").hide().fadeIn(300);

                // $("#commentsSection").append(appendComment(commentJson, "0"));

                $("#initialComment").val("");
                addDeleteListener(jsonObject["CommentId"]);
                addEditListener(jsonObject["CommentId"]);
                addAddListener(jsonObject["CommentId"]);

            }
        });

    });
});

//This handles the inital retrieval of comments for the article and dispatch to the appendCOmment function
function commentsCall() {

    $.ajax({
        url: "CommentServlet",
        type: "GET",
        data: {"articleloading": "true", "articleid": articleid},

        error: function () {
            console.log("error loading comments");
        },
        success: function (response) {

            //Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...


            var jsonObject = JSON.parse(response);
            user = jsonObject["userLoggedIn"];
            articleAuthor = jsonObject["Author"];

            if (jsonObject["nocomments"] == true) {
                $("#loading").fadeOut(200);
                return;
            }

            var noOfcomments = 0;
            var allComments = "";
            //Lengths of loop is minus 2 to account for author and user parametersƒre
            for (var i = 0; i < Object.keys(jsonObject).length-2; i++) {
                var commentId = jsonObject[i]["CommentId"];
                //If this is a top level comment
                if (jsonObject[i]["Parent"] == 0) {

                    $("#commentsSection").append(appendComment(jsonObject, i,user,articleAuthor));
                    addDeleteListener(commentId);
                    addEditListener(commentId);
                    addAddListener(commentId);
                }

                //Handles sub-comments
                else {
                    var nested = appendComment(jsonObject, i,user,articleAuthor);

                    $(nested).appendTo($("#commentblock" + jsonObject[i]["Parent"]));
                    addDeleteListener(commentId);
                    addEditListener(commentId);
                    addAddListener(commentId);
                }

            }
            $("#loading").fadeOut(200);

        }

    });
}

//This handles adding the comments to the DOM
function appendComment(jsonObject, i,user,articleAuthor) {

    var commentId = jsonObject[i]["CommentId"];
    //creating new comment display with every object in response
    var comment = '<div class="media">' +
        '<div class="media-left" id="avatarthumb' + commentId + '"><img src="' + jsonObject[i]["Avatar"] + '" class="media-object" style="width:45px"></div>' +
        '<div class="media-body" id="commentblock' + commentId + '">' +
        '<h4 class="media-heading">' + jsonObject[i]["Username"] + '&nbsp;<small><i>Posted on ' + jsonObject[i]["Date"] + '</i></small></h4>' +
        '<form name="commentForm" action="CommentServlet" method="GET">' +
        '<p>' + jsonObject[i]["Comment"] + '</p>' +
        '<input type="text" name="commentId" value="' + commentId + '" hidden>' +
        '<input type="text" name="articleid" value="' + id + '" hidden>' +
        '<input type="text" name="userLoggedIn" value="' + user + '" hidden>' +
        '</form>';


    //if the user is logged in add an "add comment" button to each comment
    if ((user != "null" && user != "") && user != null) {
        comment += '<span id = "addcommentnumber' + commentId + '" style="cursor: pointer">' +
            '<a><span class="glyphicon glyphicon-plus"></span> Add Comment&emsp;</a></span>' +
            '<div id="nestedForm' + commentId + '"><form> ' +
            '<textarea class="form-control" name="comment" id="nestedcomment' + commentId + '" cols="10" rows="2"' +
            ' placeholder="Write something, ' + user + '!"></textarea>' +
            '</form>' +
            '<div id="submitnestedcomment' + commentId + '" style="cursor: pointer"><a><span class="glyphicon glyphicon-share-alt"></span> Submit Comment</a></div></div>'
    }

    //if the COMMENT was written by the person currently logged in, add a delete and edit button.
    if (user === jsonObject[i]["Username"]) {

        comment += '<span id="editcommentnumber' + commentId + '" style="cursor: pointer">' +
            '<a><span class="glyphicon glyphicon-pencil"></span> Edit&emsp;</a></span>' +

            '<span id="deletecommentnumber' + commentId + '" style="cursor: pointer"> ' +
            '<a><span class="glyphicon glyphicon-trash"></span> Delete&emsp;</a></span>' +
            '<div id="nestedEditForm' + commentId + '">' +
            '<textarea class="form-control" name="comment" id="nestedEditcomment' + commentId + '" cols="10" rows="2"></textarea>' + '</form>' +
            '<div id="submitnestedEditcomment' + commentId + '" style="cursor: pointer"><a><span class="glyphicon glyphicon-share-alt"></span> Submit Comment</a></div></div>';


        // if the ARTICLE was written by the person currently logged in, add a delete button.

    } else if (user === articleAuthor) {

        comment += '<span id="deletecommentnumber' + commentId + '" style="cursor: pointer"> ' +
            '<a><span class="glyphicon glyphicon-trash"></span> Delete&emsp;</a></span>';
    }


    comment += '</div></div>';
    return comment;
}

//Adds delete/edit/add listeners to the buttons created in the appendComment function
function addDeleteListener(id) {

    $("#deletecommentnumber" + id).on("click", function () {
        $.ajax({
            url: "CommentServlet",
            type: "GET",
            data: {"commentId": id, "deleteComment": true},

            error: function () {
                console.log("error deleting comment");
            },
            success: function (response) {
                var jsonObject = JSON.parse(response);
                if (jsonObject["deletedcomment"] == true) {

                    $("#commentblock" + id).fadeOut("slow");
                    $("#submitnestedcomment" + id).fadeOut("slow");
                    $("#avatarthumb" + id).fadeOut("slow");
                    $("#addcommentnumber" + id).fadeOut("slow");
                }
            }
        });

    });
}

function addEditListener(id) {
    $("#nestedEditForm" + id).fadeToggle(500);
    $("#editcommentnumber" + id).on("click", function () {

        $("#nestedEditcomment" + id).val($("#commentblock" + id + " p").html())
        $("#nestedEditForm" + id).fadeToggle(500);

        $("#submitnestedEditcomment" + id).on("click", function () {
            var commentcontent = $("#nestedEditcomment" + id).val();

            if (commentcontent == "") {
                return;
            }
            if (commentcontent == null) {
                return;
            }

            $.ajax({
                url: "CommentServlet",
                type: "GET",
                data: {"saveChanges": true, "commentId": id, "comment": commentcontent},

                error: function () {
                    console.log("error add new comment");
                },
                success: function (response) {
                    $("#nestedEditForm" + id).hide();
                    $("#commentblock" + id + " p").html(commentcontent);
                    $("#submitnestedcomment" + id).hide();
                }
            });
        });

    });
}

function addAddListener(id) {
    $("#nestedForm" + id).hide();
    $("#addcommentnumber" + id).on("click", function () {
        $("#nestedForm" + id).fadeToggle(500);
        addNestedSubmit(id);
    });
}

function addNestedSubmit(id) {
    $("#submitnestedcomment" + id).on("click", function () {
        console.log("nested submit clicked");
        var commentcontent = $("#nestedcomment" + id).val();

        if (commentcontent == "") {
            return;
        }
        if (commentcontent == null) {
            return;
        }
        $.ajax({
            url: "CommentServlet",
            type: "GET",
            data: {
                "articleid": articleid,
                "Author": articleAuthor,
                "parentid": id,
                "comment": commentcontent,
                "username": user
            },

            error: function () {
                console.log("error adding new comment");
            },
            success: function (response) {
                // The following handles displaying the comment immediately
                var jsonObject = JSON.parse(response);
                $("#addComment").fadeOut(100);
                $("#nestedcomment" + id).fadeOut(100);
                var commentJson = {
                    "0": {
                        "Avatar": jsonObject["Avatar"],
                        "Username": user,
                        "Date": jsonObject["Date"],
                        "CommentId": jsonObject["CommentId"],
                        "Comment": commentcontent
                    }
                };
                //create JSONObject here
                $(appendComment(commentJson, 0,user)).appendTo("#commentblock" + id).hide().fadeIn(300);
                $("#submitnestedcomment" + id).hide();

                var commentId = jsonObject["CommentId"];

                addDeleteListener(commentId);
                addEditListener(commentId);
                addAddListener(commentId);
            }
        });
    });
}


