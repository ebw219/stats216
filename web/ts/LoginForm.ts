/**
 * LoginForm encapsulates all of the code for the form for adding an entry
 */

// Prevent compiler errors when using jQuery.  "$" will be given a type of
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;

// Prevent compiler errors when using Handlebars
var Handlebars: any;

class LoginForm {

    /**
     * The name of the DOM entry associated with LoginForm
     */
    private static readonly NAME = "LoginForm";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;
    /**
     * Initialize the LoginForm by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        Navbar.refresh();

        $("#" + LoginForm.NAME).remove();

        LoginForm.show();
        if (!LoginForm.isInit) {
            $("body").append(Handlebars.templates[LoginForm.NAME + ".hb"]({}));
            $("#" + LoginForm.NAME + "-OK").click(LoginForm.submitForm());
            $("#" + LoginForm.NAME + "-Close").click(LoginForm.hide());
            LoginForm.isInit = true;
        }
    }
    /**
     * Refresh() doesn't really have much meaning, but just like in Navbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        LoginForm.init();
    }

    /**
     * Hide the LoginForm.  Be sure to clear its fields first
     */
    private static hide() {
        $("#" + LoginForm.NAME + "-title").val("");
        $("#" + LoginForm.NAME + "-message").val("");
        $("#" + LoginForm.NAME).modal("hide");
    }
    /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        $("#" + LoginForm.NAME + "-title").val("");
        $("#" + LoginForm.NAME + "-message").val("");
        $("#" + LoginForm.NAME).modal("show");
    }

    /**
     * Send data to submit the form only if the fields are both valid.
     * Immediately hide the form when we send data, so that the user knows that
     * their click was received.
     */
    private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        let title = "" + $("#" + NewEntryForm.NAME + "-title").val();
        let msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        NewEntryForm.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: Constants.APPURL + "/messages",
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mMessage: msg }),
            success: NewEntryForm.onSubmitResponse
        });
    }
}