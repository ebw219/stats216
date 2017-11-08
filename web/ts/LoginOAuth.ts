// Prevent compiler errors when using jQuery.  "$" will be given a type of
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.

var $: any;

// Prevent compiler errors when using Handlebars
var Handlebars: any;

class LoginOAuth {

    private static readonly NAME = "LoginOAuth";

    public static init() {
        $("body").append(Handlebars.templates[LoginOAuth.NAME + ".hb"]());
        // $("#" + LoginOAuth.NAME + "-login").show();
        $("#" + LoginOAuth.NAME + "-login").click(LoginOAuth.clickLogin);

    }

    private static hide(){
        $("#" + "Login").hide();
    }

    public static clickLogin() {
        var auth2;
        console.log("clicked login");
        //Initializing the GoogleAuth Object
        function start() {
            console.log("in start");
            console.log(gapi);
            gapi.load('auth2', callback);
            function callback() {
                console.log("in callback");
                auth2 = gapi.auth2.init({
                    client_id: Constants.CLIENT_ID,
                    // Scopes to request in addition to 'profile' and 'email'
                    //scope: 'additional_scope'
                });
                auth2.signIn({
                    client_id: Constants.CLIENT_ID
                })
                    .then(signInCallback);
            }
        }

        //One-time code flow
        // auth2.grantOfflineAccess().then(signInCallback);
        start();

        // auth2.isSignedIn.listen(signInCallback);


        function signInCallback(authResult) {
            console.log("in sign-in callback");
            console.log(authResult['Zi'].access_token);
            // console.log(authResult['Zi']);
            if (authResult['Zi']) {
                // Hide the sign-in button now that the user is authorized, for example:
                // $('#signinButton').attr('style', 'display: none');
                console.log(Constants.APP_URL + "/accessToken/" + authResult['Zi'].access_token);
                // Send the code to the server
                $.ajax({
                    type: 'POST',
                    url: Constants.APP_URL + "/accessToken/" + authResult['Zi'].access_token,
                    // Always include an `X-Requested-With` header in every AJAX request,
                    // to protect against CSRF attacks.
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    contentType: 'application/octet-stream; charset=utf-8',
                    success: function(result) {
                        // Handle or verify the server response.
                        LoginOAuth.hide();
                        ElementList.refresh();
                    },
                    processData: false,
                    // data: authResult['Zi'],
                });
            } else {
                // There was an error.
            }
        }
    }

}