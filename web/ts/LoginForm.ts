/**
 * LogInForm encapsulates all of the code for the form for adding an entry
 */
class LogInForm {

    /**
     * The name of the DOM entry associated with LogInForm
     */
    private static readonly NAME = "LogInForm";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;
     /**
     * Initialize the LogInForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        if (!LogInForm.isInit) {
            $("body").append(Handlebars.templates[LogInForm.NAME + ".hb"]());
            $("#" + LogInForm.NAME + "-OK").click(LogInForm.submitForm);
            $("#" + LogInForm.NAME + "-Close").click(LogInForm.hide);
            LogInForm.isInit = true;
        }
    }
     /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        LogInForm.init();
    }

     /**
     * Hide the LogInForm.  Be sure to clear its fields first
     */
    private static hide() {
        $("#" + LogInForm.NAME + "-title").val("");
        $("#" + LogInForm.NAME + "-message").val("");
        $("#" + LogInForm.NAME).modal("hide");
    }
     /**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        $("#" + LogInForm.NAME + "-title").val("");
        $("#" + LogInForm.NAME + "-message").val("");
        $("#" + LogInForm.NAME).modal("show");
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