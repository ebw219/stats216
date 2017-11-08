// Prevent compiler errors when using jQuery.  "$" will be given a type of
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $;
// Prevent compiler errors when using Handlebars
var Handlebars;
var LoginOAuth = /** @class */ (function () {
    function LoginOAuth() {
    }
    LoginOAuth.init = function () {
        $("body").append(Handlebars.templates[LoginOAuth.NAME + ".hb"]());
        $("#" + LoginOAuth.NAME + "-login").show();
        $("#" + LoginOAuth.NAME + "-login").click(LoginOAuth.clickLogin);
    };
    LoginOAuth.clickLogin = function () {
        function handleClientLoad() {
            // Loads the client library and the auth2 library together for efficiency.
            // Loading the auth2 library is optional here since `gapi.client.init` function will load
            // it if not already loaded. Loading it upfront can save one network request.
            gapi.load('client:auth2', initClient);
        }
        function initClient() {
            // Initialize the client with API key and People API, and initialize OAuth with an
            // OAuth 2.0 client ID and scopes (space delimited string) to request access.
            gapi.client.init({
                apiKey: Constants.APIKEY,
                discoveryDocs: ["https://people.googleapis.com/$discovery/rest?version=v1"],
                clientId: Constants.CLIENTID,
                scope: 'profile'
            });
            // }).then(function () {
            //     // Listen for sign-in state changes.
            //     gapi.auth2.getAuthInstance().isSignedIn.listen(updateSigninStatus);
            //
            //     // Handle the initial sign-in state.
            //     updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get());
            // });
        }
        // function updateSigninStatus(isSignedIn) {
        //     // When signin status changes, this function is called.
        //     // If the signin status is changed to signedIn, we make an API call.
        //     if (isSignedIn) {
        //         makeApiCall();
        //     }
        // }
        //
        // function handleSignInClick(event) {
        //     // Ideally the button should only show up after gapi.client.init finishes, so that this
        //     // handler won't be called before OAuth is initialized.
        //     gapi.auth2.getAuthInstance().signIn();
        // }
        //
        // function handleSignOutClick(event) {
        //     gapi.auth2.getAuthInstance().signOut();
        // }
        //
        // function makeApiCall() {
        //     // Make an API call to the People API, and print the user's given name.
        //     gapi.client.people.people.get({
        //         'resourceName': 'people/me',
        //         'requestMask.includeField': 'person.names'
        //     }).then(function(response) {
        //         console.log('Hello, ' + response.result.names[0].givenName);
        //     }, function(reason) {
        //         console.log('Error: ' + reason.result.error.message);
        //     });
        // }
    };
    LoginOAuth.NAME = "LoginOAuth";
    return LoginOAuth;
}());
