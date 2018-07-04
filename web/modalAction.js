/**
 * Created by glee156 on 23/01/2018.
 */

if ($("#profile").length){
    status = "true";
} else {
    status = "false";
}

//tab highlighting in modal
$('#loginButton').click(function () {

    $('#signupTab').removeClass("active");
    $('#registration-form').removeClass("tab-pane fade in active").addClass("tab-pane fade");
    $('#loginTab').addClass("active");
    $('#login-form').addClass("tab-pane fade in active");
});

$('#signupButton').click(function () {
    $('#loginTab').removeClass("active tab-pane");
    $('#login-form').removeClass("tab-pane fade in active").addClass("tab-pane fade");
    $('#signupTab').addClass("active");
    $('#registration-form').addClass("tab-pane fade in active");
});

//alerts fading up and away
$(".alert").fadeTo(2000, 500).slideUp(500, function(){
    $(".alert").slideUp(500);
});

