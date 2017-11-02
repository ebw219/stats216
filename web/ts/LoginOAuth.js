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
        // $("#" + LoginOAuth.NAME + "-login").show();
        $("." + LoginOAuth.NAME + "-login").click(LoginOAuth.clickLogin);
    };
    LoginOAuth.clickLogin = function () {
        $.ajax({
            type: "GET",
            url: "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=325681108859-g2tq47a7h6pvnfo159h0aoaluto67kqv.apps.googleusercontent.com&redirect_uri=https://www.getpostman.com/oauth2/callback&scope=https://www.googleapis.com/auth/analytics.readonly+https://www.googleapis.com/auth/userinfo.email&state=abc123",
            dataType: "json",
        });
    };
    LoginOAuth.NAME = "LoginOAuth";
    return LoginOAuth;
}());
