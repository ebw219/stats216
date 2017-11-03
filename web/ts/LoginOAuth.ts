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

        // ElementList.refresh();
    }

    public static clickLogin() {
        console.log("clicked login");
        start();
        var auth2;
        //Initializing the GoogleAuth Object
        function start() {
            console.log("in start");
            // gapi.load("client:auth2", callback);
            gapi.load('auth2', callback);

            function callback() {
                console.log("in callback");
                auth2 = gapi.auth2.init({
                    client_id: Constants.CLIENT_ID,
                    // Scopes to request in addition to 'profile' and 'email'
                    //scope: 'additional_scope'
                });
            }
        }

        //One-time code flow
        // auth2.grantOfflineAccess().then(signInCallback);
        console.log(auth2.signIn({
            client_id:Constants.CLIENT_ID
        }));

        function signInCallback(authResult) {
            console.log("in sign in callback");
            if (authResult['code']) {

                // Hide the sign-in button now that the user is authorized, for example:
                // $('#signinButton').attr('style', 'display: none');

                // Send the code to the server
                $.ajax({
                    type: 'POST',
                    url: Constants.APP_URL + "/code",
                    // Always include an `X-Requested-With` header in every AJAX request,
                    // to protect against CSRF attacks.
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    contentType: 'application/octet-stream; charset=utf-8',
                    success: function(result) {
                        // Handle or verify the server response.
                    },
                    processData: false,
                    data: authResult['code']
                });
            } else {
                // There was an error.
            }
        }
    }

}