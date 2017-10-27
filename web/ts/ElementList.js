///<reference path="../app.ts"/>
///<reference path="Constants.ts"/>
/**
 * The ElementList Singleton provides a way of displaying all of the data
 * stored on the server as an HTML table.
 */
var ElementList = /** @class */ (function () {
    function ElementList() {
    }
    /**
     * Initialize the ElementList singleton by creating its element in the DOM.
     * This needs to be called from any public static method, to ensure the
     * Singleton is initialized before use
     */
    ElementList.init = function () {
        if (!ElementList.isInit) {
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
            url: Constants.APPURL + "/messages",
            dataType: "json",
            success: ElementList.update
        });
    };
    /**
     * update() is the private method used by refresh() to update the
     * ElementList
     */
    ElementList.update = function (data) {
        // Remove the table of data, if it exists
        $("#" + ElementList.NAME).remove();
        // Use a template to re-generate the table, and then insert it
        $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
        // Find all of the delete buttons, and set their behavior
        $("." + ElementList.NAME + "-delbtn").click(ElementList.clickDelete);
        // Find all of the Edit buttons, and set their behavior
        $("." + ElementList.NAME + "-editbtn").click(ElementList.clickEdit);
        // Find all of the UpVote buttons and set their behavior
        $("." + ElementList.NAME + "-upvotebtn").click(ElementList.clickUp);
        // Find all of the DownVote buttons and set their behavior
        $("." + ElementList.NAME + "-downvotebtn").click(ElementList.clickDown);
    };
    /**
     * buttons() creates 'edit' and 'upvote' 'downvote' and 'delete' buttons for an id, and
     * puts them in
     * a TD
     */
    ElementList.buttons = function (id) {
        return "<td><button class='" + ElementList.NAME +
            "-editbtn' data-value='" + id + "'>Edit</button></td>" +
            "<td><button class='" + ElementList.NAME +
            "-upvotebtn' data-value='" + id + "'>Up</button></td>" +
            "<td><button class='" + ElementList.NAME +
            "-downvotebtn' data-value='" + id + "'>Down</button></td>" +
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
            url: Constants.APPURL + "/messages/" + id,
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
            url: Constants.APPURL + "/messages/" + id,
            dataType: "json",
            // success: editEntryForm.init
            success: ElementList.refresh
        });
    };
    /**
     * clickUp is the code we run in response to a click of a up vote button
     */
    ElementList.clickUp = function () {
        // as in clickDelete, we need the ID of the row
        var id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: Constants.APPURL + "/messages/" + id,
            dataType: "json",
            success: ElementList.refresh
        });
    };
    /**
     * clickDown is the code we run in response to a click of a down vote button
     */
    ElementList.clickDown = function () {
        // as in clickDelete, we need the ID of the row
        var id = $(this).data("value");
        $.ajax({
            type: "GET",
            url: Constants.APPURL + "/messages/" + id,
            dataType: "json",
            success: ElementList.refresh
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
