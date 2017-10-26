/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 */
var EditEntryForm = /** @class */ (function () {
    /**
     * To initialize the object, we say what method of EditEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    function EditEntryForm() {
        $("#editCancel").click(this.clearForm);
        $("#editButton").click(this.submitForm);
    }
    /**
     * init() is called from an AJAX GET, and should populate the form if and
     * only if the GET did not have an error
     */
    EditEntryForm.prototype.init = function (data) {
        if (data.mStatus === "ok") {
            $("#editTitle").val(data.mData.mTitle);
            $("#editMessage").val(data.mData.mContent);
            $("#editId").val(data.mData.mId);
            $("#editCreated").text(data.mData.mCreated);
            // show the edit form
            $("#addElement").hide();
            $("#editElement").show();
            $("#showElements").hide();
        }
        else if (data.mStatus === "error") {
            window.alert("Error: " + data.mMessage);
        }
        else {
            window.alert("An unspecified error occurred");
        }
    };
    /**
     * Clear the form's input fields
     */
    EditEntryForm.prototype.clearForm = function () {
        $("#editTitle").val("");
        $("#editMessage").val("");
        $("#editId").val("");
        $("#editCreated").text("");
    };
    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    EditEntryForm.prototype.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var title = "" + $("#editTitle").val();
        var msg = "" + $("#editMessage").val();
        // NB: we assume that the user didn't modify the value of #editId
        var id = "" + $("#editId").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "PUT",
            url: Constants.appUrl + "/messages/" + id,
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mMessage: msg }),
            success: editEntryForm.onSubmitResponse
        });
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    EditEntryForm.prototype.onSubmitResponse = function (data) {
        // If we get an "ok" message, clear the form and refresh the main 
        // listing of messages
        if (data.mStatus === "ok") {
            editEntryForm.clearForm();
            ElementList.refresh();
        }
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        else {
            window.alert("Unspecified error");
        }
    };
    return EditEntryForm;
}()); // end class EditEntryForm
