/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var NewEntryForm = /** @class */ (function () {
    function NewEntryForm() {
    }
    /**
     * Initialize the NewEntryForm by creating its element in the DOM and
     * configuring its buttons.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use
     */
    NewEntryForm.init = function () {
        if (!NewEntryForm.isInit) {
            $("body").append(Handlebars.templates[NewEntryForm.NAME + ".hb"]());
            $("#" + NewEntryForm.NAME + "-OK").click(NewEntryForm.submitForm);
            $("#" + NewEntryForm.NAME + "-Close").click(NewEntryForm.hide);
            NewEntryForm.isInit = true;
        }
    };
    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    NewEntryForm.refresh = function () {
        NewEntryForm.init();
    };
    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    NewEntryForm.hide = function () {
        $("#" + NewEntryForm.NAME + "-title").val("");
        $("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + NewEntryForm.NAME).modal("hide");
    };
    /**
         * Show the NewEntryForm.  Be sure to clear its fields, because there are
         * ways of making a Bootstrap modal disapper without clicking Close, and
         * we haven't set up the hooks to clear the fields on the events associated
         * with those ways of making the modal disappear.
         */
    NewEntryForm.show = function () {
        $("#" + NewEntryForm.NAME + "-title").val("");
        $("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + NewEntryForm.NAME).modal("show");
    };
    /**
     * Send data to submit the form only if the fields are both valid.
     * Immediately hide the form when we send data, so that the user knows that
     * their click was received.
     */
    NewEntryForm.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var title = "" + $("#" + NewEntryForm.NAME + "-title").val();
        var msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
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
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    NewEntryForm.onSubmitResponse = function (data) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            ElementList.refresh();
        }
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        else {
            window.alert("Unspecified error");
        }
    };
    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    NewEntryForm.NAME = "NewEntryForm";
    /**
     * Track if the Singleton has been initialized
     */
    NewEntryForm.isInit = false;
    return NewEntryForm;
}());
