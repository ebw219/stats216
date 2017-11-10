/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {

    /**
     * The name of the DOM entry associated with NewEntryForm
     */
    static readonly NAME = "NewEntryForm";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the NewEntryForm by creating its element in the DOM and 
     * configuring its buttons.  This needs to be called from any public static 
     * method, to ensure that the Singleton is initialized before use
     */
    private static init() {
        console.log("entered newentryform init");
        //NewEntryForm.isInit = false;
        console.log("isInit: " + NewEntryForm.isInit);
        if (!NewEntryForm.isInit) {
            console.log("entered newentryform init if statement");
            $("body").append(Handlebars.templates[NewEntryForm.NAME + ".hb"]());
            // $("#" + NewEntryForm.NAME + "-OK").click(NewEntryForm.show());
            $("#" + NewEntryForm.NAME + "-OK").click(NewEntryForm.submitForm);
            console.log("submitForm called");
            $("#" + NewEntryForm.NAME + "-Close").click(NewEntryForm.hide);
            console.log("hideform called");
            NewEntryForm.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning, but just like in sNavbar, we
     * have a refresh() method so that we don't have front-end code calling
     * init().
     */
    public static refresh() {
        console.log("newentryform refresh");
        NewEntryForm.init();
    }

    /**
     * Hide the NewEntryForm.  Be sure to clear its fields first
     */
    static hide() {
        console.log("entered newentryform hide");
        $("#" + NewEntryForm.NAME + "-title").val("");
        $("#" + NewEntryForm.NAME + "-message").val("");
        $("#" + NewEntryForm.NAME).modal("hide");
    }
/**
     * Show the NewEntryForm.  Be sure to clear its fields, because there are
     * ways of making a Bootstrap modal disapper without clicking Close, and
     * we haven't set up the hooks to clear the fields on the events associated
     * with those ways of making the modal disappear.
     */
    public static show() {
        console.log("clicked, entering show");
        $("#" + NewEntryForm.NAME + "-title").val("");
        console.log("title: " + $("#" + NewEntryForm.NAME + "-title").val(""));
        $("#" + NewEntryForm.NAME + "-message").val("");
        //$("#" + NewEntryForm.NAME + "-title").show();
        //NewEntryForm.init();
        console.log("please modal show work");
        // $("#" + NewEntryForm.NAME + "-message").show();
        $('#' + NewEntryForm.NAME).modal('show');
    }


    /**
     * Send data to submit the form only if the fields are both valid.  
     * Immediately hide the form when we send data, so that the user knows that 
     * their click was received.
     */
    private static submitForm() {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        console.log("entered newentryform submitform");
        let title = "" + $("#" + NewEntryForm.NAME + "-title").val();
        console.log("title1");
        let msg = "" + $("#" + NewEntryForm.NAME + "-message").val();
        console.log("message1");
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        //NewEntryForm.hide();
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: Constants.APP_URL + "/messages",
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mMessage: msg }),
            success: NewEntryForm.onSubmitResponse
        });
    }

    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a 
     * result.
     * 
     * @param data The object returned by the server
     */
    static onSubmitResponse(data: any) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        console.log("entered onsubmitresponse");
        if (data.mStatus === "ok") {
            console.log("refresh elementlist");
            ElementList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    }
}