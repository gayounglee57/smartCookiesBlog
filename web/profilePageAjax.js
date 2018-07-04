/**
 * Created by mmiz318 on 25/01/2018.
 */
//js for alerts on profile page
$(".alert").fadeTo(2000, 500).slideUp(500, function(){
    $(".alert").slideUp(500);
});

function profileInfoCall() {
//This handles the AJAX calls to load profile info
    $.ajax({
        url: "ProfilePageServlet",
        type: "GET",
        error: function (){
            console.log("error");
        },
        success: function (response) {
            //Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
            var jsonObject = JSON.parse(response);

            var profileInfo_panel = "<label for='username'>Username: </label><br>" +
                "<input id='username' name='username' type='text' size='40' value='" + jsonObject["Username"] + "' disabled ><br>" +
                "<label for='fname'>First Name: </label><br>" +
                "<input id='fname' name='firstname' type='text' size='40' value='" + jsonObject["First Name"] + "'><br>" +
                "<label for='lname'>Last Name: </label><br>" +
                "<input id='lname' name='lastname' type='text' size='40' value='" + jsonObject["Last Name"] + "'><br>" +
                "<label for='bday'>Birthday: </label><br>" +
                "<input id='bday' name='birthday' type='date' size='40' value='" + jsonObject["D.O.B"] + "'><br>" +
                "<label for='country'>Country: </label><br>" +
                "<input id='country' name='country' type='text' size='40' value='" + jsonObject["Country"] + "'><br>" +
                "<label for='email'>Email: </label><br>" +
                "<input id='email' name='email' type='email' size='40' value='" + jsonObject["Email"] + "'><br>" +
                "<label for='password'>Password: </label><br>" +
                "<input id='password' name='password' type='password' pattern='{4,}' title='Please enter at least 4 characters' size='40' value='" + jsonObject["Password"] + "' required><br>" +
                "<label for='description'>Description: </label><br>" +
                "<textarea name='description' id='description' cols='40' rows='5'>" + jsonObject["Description"] + "</textarea>";

            //image tag of avatar
            var avatarPath = jsonObject["Avatar"];
            if(avatarPath == null){
                avatarPath = 'images/cookie_outline.png';
            }
            var personalAvatar = "<img src='" +  avatarPath + "'>";

            $("#profileInfo").append(profileInfo_panel);
            $('#personalAvatar').append(personalAvatar);

        }

    });
}

profileInfoCall();

function deleteFunction() {
    return confirm("Are you sure you want to permanently delete your Smart Cookie Account?");
}
