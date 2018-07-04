/**
 * Created by glee156 on 31/01/2018.
 */

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

var id = getUrlParameter('articleid');
if(id == null){
    id = $("#hiddenIdDiv").text();
}
var stop = true;


function ajaxCall() {
//This handles the AJAX calls to load images
    if(stop) {
        $.ajax({

            url: "ArticleServlet",
            type: "GET",
            data: {"images": "true", "articleid": id},

            error: function () {
                console.log("error");
            },

            success: function (response) {
                var jsonObject = JSON.parse(response);
                var images = jsonObject["Images"];
                if (images != null) {
                    var imagesArray = images.split(",");
                    var image = "<div class='row'>";
                    var audio = "<div>";
                    var video = "<div>";
                    console.log("size of array " + imagesArray.length);
                    for (var i = 0; i < imagesArray.length; i++) {

                        var imagePath = imagesArray[i];

                        //deal with double slashes created by toJSONString() in servlet
                        imagePath.replace("\\\\", "\\");
                        imagePath.replace(" ", "");

                        if(imagePath.indexOf(".jpg") != -1 || imagePath.indexOf(".jpeg") != -1 || imagePath.indexOf(".png") != -1) {
                            image +=
                                "<div class='col-sm-12 col-md-6 col-lg-4 imageDisplay'>\n" +
                                "<a href = '" + imagePath + "'><img src='" + imagePath + "' alt='' width='400'></a>" +
                                "</div>\n";

                        }
                        //displaying audio files
                        else if(imagePath.indexOf(".mp3") != -1){
                            audio +=
                                "<div class='col-sm-12 col-md-6 col-lg-4 imageDisplay'>\n" +
                                "<div class='thumbnail'>" +
                            "<audio controls>" +
                            '<source src="' + imagePath + '" type="audio/mpeg">' +
                                'Your browser does not support the audio' +
                            "</audio></div></div>\n";
                        }
                        else if(imagePath.indexOf(".wav") != -1){
                            audio +=
                                "<div class='col-sm-12 col-md-6 col-lg-4 imageDisplay'>\n" +
                                "<div class='thumbnail'>" +
                                "<audio controls>" +
                                '<source src="' + imagePath + '" type="audio/wav">' +
                                'Your browser does not support the audio' +
                                "</audio></div></div>\n";
                        }
                        else if(imagePath.indexOf(".ogg") != -1){
                            audio +=
                                "<div class='col-sm-12 col-md-6 col-lg-4 imageDisplay'>\n" +
                                "<div class='thumbnail'>" +
                                "<audio controls>" +
                                '<source src="' + imagePath + '" type="audio/ogg">' +
                                'Your browser does not support the audio' +
                                "</audio></div></div>\n";
                        }
                        //displaying video files
                        else if(imagePath.indexOf(".mp4") != -1){
                            video +=
                                "<div class='col-sm-12 col-md-6 col-lg-4 imageDisplay'>\n" +
                                "<div class='thumbnail'>" +
                                '<video width="320" height="240" controls>' +
                                '<source src="' + imagePath + '" type="video/mp4">' +
                                'Your browser does not support the video' +
                                '</video></div></div>\n';
                        }
                        else if(imagePath.indexOf(".webm") != -1){
                            video +=
                                "<div class='col-sm-12 col-md-6 col-lg-4 imageDisplay'>\n" +
                                "<div class='thumbnail'>" +
                                '<video width="320" height="240" controls>' +
                                '<source src="' + imagePath + '" type="video/webm">' +
                                'Your browser does not support the video' +
                                '</video></div></div>\n';
                        }

                    }

                    $("#articleDisplay").append(image);
                    $("#audioDisplay").append(audio);
                    $("#videoDisplay").append(video);
                    stop = false;
                    console.log(stop)
                }

            }

        });
    }
}

function youtubeAjaxCall() {
//This handles the AJAX calls to load images

        $.ajax({

            url: "MultimediaServlet",
            type: "GET",
            data: {"youtubeAjax": "true", "articleid": id},

            error: function () {
                console.log("error");
            },

            success: function (response) {
                var jsonObject = JSON.parse(response);

                var url = jsonObject["Youtube"];

                if(url != null) {
                    var youtube = '<div class=\'col-sm-12 col-md-6 col-lg-4 imageDisplay\'><iframe class="text-center" width="560" height="315" src="' + url + '" frameborder="0" gesture="media" allow="encrypted-media" allowfullscreen></iframe></div>';
                }

                $("#youtubeDisplay").append(youtube);
            }

        });
}

function youtubeUrlCheck() {
//This handles the AJAX call to check youtube link

    var url = $("#youtube-url").val();

    if(!url.includes("www.youtube.com")){
        console.log("please enter a link from youtube");
    }
}

ajaxCall();
youtubeAjaxCall();


//showing if the youtube url typed in contains 'www.youtube.com' and no 'iframe'
$(document).ready(function() {
    $('#youtube-form').bootstrapValidator();
});