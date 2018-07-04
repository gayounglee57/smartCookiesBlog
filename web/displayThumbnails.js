/**
 * Created by glee156 on 23/01/2018.
 */
//Variable currently unused, but saved for future page navigation
var blockId
var search;

var calledFromProfilePage;

function ajaxCall(pageReload, noToLoad, status, search) {
//This handles the AJAX calls to load articles

    $.ajax({

        url: "ArticleServlet",
        type: "GET",
        data: {
            "isPageReloaded": pageReload,
            "articlesToLoad": noToLoad,
            "userArticles": calledFromProfilePage,
            "sortMethod": $("#sortingForm :selected").val(),
            "searchString": search
        },

        error: function (response) {
            console.log("error");
        },

        success: function (responseText) {
            // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
            var jsonObject = JSON.parse(responseText);

            //This block stops trying to load new articles when the number of articles returned is less than asked for
            if (Object.keys(jsonObject).length == 0) {
                $("#loading").hide();
                return;
            }
            //This block stops trying to load new articles when the number of articles returned is less than asked for
            else if (Object.keys(jsonObject).length < noToLoad) {
                $(window).off("scroll");
            }

            for (var i = 0; i < Object.keys(jsonObject).length; i++) {
                var description = jsonObject[i].description;
                var title = jsonObject[i].title;
                var id = jsonObject[i].id;
                var user = jsonObject[i].userLoggedIn;
                var author = jsonObject[i].author;
                var image = jsonObject[i].image;
               //checking which is an image out of a string of multimedia paths
                var imagePath;
                if(image != null) {
                    var imageArray = image.split(",");
                    for (var j = 0; j < imageArray.length; j++) {
                        imagePath = imageArray[j] + "";
                        if (imagePath.indexOf(".jpg") !== -1 || imagePath.indexOf(".jpeg") !== -1 || imagePath.indexOf(".png") !== -1) {
                            break;
                        }
                        imagePath = "images/cookie.jpg";
                    }
                }
                //default image on thumbnail for articles without images uploaded
                else{
                    imagePath = "images/cookie.jpg";
                }

                var article_panel = "<div id='block" + blockId + "' class=\"col-sm-6 col-md-4\">\n" +
                    "      <div class=\"thumbnail\">\n" +
                    "        <a href = \"ArticleServlet?articleid=" + id + "&userLoggedIn=" + user + "&author=" + author + "\"><img src=\"" + imagePath + "\" alt=\"...\"></a>\n" +
                    "        <div class=\"caption\">\n" +
                    "          <a href = \"ArticleServlet?articleid=" + id + "&userLoggedIn=" + user + "&author=" + author + "\"><h3>" + title + "&nbsp;</h3></a>\n" +
                    "         <a href = \"ArticleServlet?articleid=" + id + "&userLoggedIn=" + user + "&author=" + author + "\"><p>" + description + "&nbsp;</p></a>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>";

                var panel = $(article_panel).hide();
                $("#ajaxThumbnail").append(panel);
                $(panel).fadeIn(500);
                $("#loading").hide();
            }
            blockId++;

        }
    });

}


$(document).ready(function () {

    //Checks if profile id exists
    if ($("#profile").length) {
        console.log("call from profile page");
        calledFromProfilePage = "true";
        $(window).off("scroll");

    } else {
        calledFromProfilePage = "false";
    }



    ajaxCall("reload", 6, calledFromProfilePage, search);


    setTimeout(function () {
        $(window).on("scroll", infiniteScroll);
    },600);

    $("#sorting").on("change", $("#ajaxThumbnail"), function () {
        $("#ajaxThumbnail").empty();

        setTimeout(function () {
            $(window).on("scroll", infiniteScroll);
        },600);

        ajaxCall("reload", 6, false, search)
    });


    $("#search").on("click", $("#ajaxThumbnail"), doSearch);
    $("#searchText").keypress(function (e) {
        if (e.which == 13) {
            doSearch();
        }
    });

    function doSearch() {
        $("#ajaxThumbnail").empty();
        //Loading image
        $("#ajaxThumbnail").append('<div id="loading"><p>Searching Recipes <img src="images/giphy.gif" /></p></div>')
        search = $("#searchText").val();
        ajaxCall("reload", 6, false, search);
        $(window).off("scroll");
    }

    //Load new articles on scrolling
    function infiniteScroll() {
        if (calledFromProfilePage == "false") {

            if ($(window).scrollTop() + $(window).height() >= $(document).height() - 100) {
                ajaxCall(null, 6, calledFromProfilePage, search);
            }
        }
    }



});
