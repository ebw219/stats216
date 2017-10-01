"use strict";
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
            url: "/messages/" + id,
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
            mainList.refresh();
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
/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
var NewEntryForm = /** @class */ (function () {
    /**
     * To initialize the object, we say what method of NewEntryForm should be
     * run in response to each of the form's buttons being clicked.
     */
    function NewEntryForm() {
        $("#addCancel").click(this.clearForm);
        $("#addButton").click(this.submitForm);
    }
    /**
     * Clear the form's input fields
     */
    NewEntryForm.prototype.clearForm = function () {
        $("#newTitle").val("");
        $("#newMessage").val("");
        // reset the UI
        $("#addElement").hide();
        $("#editElement").hide();
        $("#showElements").show();
    };
    /**
     * Check if the input fields are both valid, and if so, do an AJAX call.
     */
    NewEntryForm.prototype.submitForm = function () {
        // get the values of the two fields, force them to be strings, and check 
        // that neither is empty
        var title = "" + $("#newTitle").val();
        var msg = "" + $("#newMessage").val();
        if (title === "" || msg === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: "/messages",
            dataType: "json",
            data: JSON.stringify({ mTitle: title, mMessage: msg }),
            success: newEntryForm.onSubmitResponse
        });
    };
    /**
     * onSubmitResponse runs when the AJAX call in submitForm() returns a
     * result.
     *
     * @param data The object returned by the server
     */
    NewEntryForm.prototype.onSubmitResponse = function (data) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            newEntryForm.clearForm();
        }
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        else {
            window.alert("Unspecified error");
        }
    };
    return NewEntryForm;
}()); // end class NewEntryForm
/**
 * The ElementList Singleton provides a way of displaying all of the data
 * stored on the server as an HTML table.
 */
var ElementList = /** @class */ (function () {
    function ElementList() {
    }
    /**
     * Initialize the ElementList singleton by creating its element in the DOM.
     * This needs to be called from any public static method, to ensure that the
     * Singleton is initialized before use.
     */
    ElementList.init = function () {
        if (!ElementList.isInit) {
            $("body").append('<div id="' + ElementList.NAME +
                '"><h3>All Messages</h3><button id="' + ElementList.NAME +
                '-showFormButton">Add Message</button><div id="' +
                ElementList.NAME + '-messageList"></div></div>');
            ElementList.isInit = true;
        }
    };
    /**
 * refresh() is the public method for updating the ElementList
 */
    ElementList.refresh = function () {
        // Make sure the singleton is initialized
        ElementList.init();
        // Issue a GET, and then pass the result to update()
        $.ajax({
            type: "GET",
            url: "/messages",
            dataType: "json",
            success: ElementList.update
        });
    };
    /**
     * update() is the private method used by refresh() to update the
     * ElementList
     */
    ElementList.update = function (data) {
        // create the new table of data
        var newHTML = "<table>";
        for (var i = 0; i < data.mData.length; ++i) {
            newHTML += "<tr><td>" + data.mData[i].mTitle + "</td>" +
                ElementList.buttons(data.mData[i].mId) + "</tr>";
        }
        newHTML += "</table>";
        // replace the contents of the messageList with our table
        $("#" + ElementList.NAME + "-messageList").html(newHTML);
        // Find all of the delete buttons, and set their behavior
        $("." + ElementList.NAME + "-delbtn").click(ElementList.clickDelete);
        // Find all of the Edit buttons, and set their behavior
        $("." + ElementList.NAME + "-editbtn").click(ElementList.clickEdit);
        $("#" + ElementList.NAME + "-messageList").append(Handlebars.templates[ElementList.NAME + ".hb"]({ text1: "Hello", arr1: [1, 2, 3] }));
    };
    /**
     * buttons() creates 'edit' and 'delete' buttons for an id, and puts them in
     * a TD
     */
    ElementList.buttons = function (id) {
        return "<td><button class='" + ElementList.NAME +
            "-editbtn' data-value='" + id + "'>Edit</button></td>" +
            "<td><button class='" + ElementList.NAME +
            "-delbtn' data-value='" + id + "'>Delete</button></td>";
    };
    /**
     * clickDelete is the code we run in response to a click of a delete button
     */
    ElementList.clickDelete = function () {
        // for now, just print the ID that goes along with the data in the row
        // whose "delete" button was clicked
        var id = $(this).data("value");
        $.ajax({
            type: "DELETE",
            url: "/messages/" + id,
            dataType: "json",
            // TODO: we should really have a function that looks at the return
            //       value and possibly prints an error message.
            success: ElementList.refresh
        });
    };
    /**
     * clickEdit is the code we run in response to a click of a delete button
     */
    ElementList.clickEdit = function () {
        // as in clickDelete, we need the ID of the row
        var id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: "/messages/" + id,
            dataType: "json",
            success: editEntryForm.init
        });
    };
    /**
     * The name of the DOM entry associated with ElementList
     */
    ElementList.NAME = "ElementList";
    /**
     * Track if the Singleton has been initialized
     */
    ElementList.isInit = false;
    return ElementList;
}());
/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $;
// Prevent compiler errors when using Handlebars
var Handlebars;
// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm;
// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation
var editEntryForm;
// Run some configuration code when the web page loads
$(document).ready(function () {
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();
    // set up initial UI state
    $("#editElement").hide();
    $("#addElement").hide();
    $("#showElements").show();
    // set up the "Add Message" button
    $("#showFormButton").click(function () {
        $("#addElement").show();
        $("#showElements").hide();
    });
    // Populate the Element List Singleton with data from the server
    ElementList.refresh();
});
