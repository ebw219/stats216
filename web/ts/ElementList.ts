/**
 * The ElementList Singleton provides a way of displaying all of the data 
 * stored on the server as an HTML table.
 */
class ElementList {
    /**
     * The name of the DOM entry associated with ElementList
     */
    private static readonly NAME = "ElementList";

    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * Initialize the ElementList singleton by creating its element in the DOM.
     * This needs to be called from any public static method, to ensure that the 
     * Singleton is initialized before use.
     */
    private static init() {
        if (!ElementList.isInit) {
            ElementList.isInit = true;
        }
    }

    /**
 * refresh() is the public method for updating the ElementList
 */
public static refresh() {
    // Make sure the singleton is initialized
    ElementList.init();
    // Issue a GET, and then pass the result to update()
    $.ajax({
        type: "GET",
        url: "/messages",
        dataType: "json",
        success: ElementList.update
    });
}

/**
 * update() is the private method used by refresh() to update the 
 * ElementList
 */
private static update(data: any) {
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

}

/**
 * buttons() creates 'edit' and 'upvote' 'downvote' and 'delete' buttons for an id, and
 * puts them in
 * a TD
 */
private static buttons(id: string): string {
    return "<td><button class='" + ElementList.NAME +
        "-editbtn' data-value='" + id + "'>Edit</button></td>" + 
	"<td><button class='" + ElementList.NAME + 
	"-upvotebtn' data-value='" + id + "'>Up</button></td>" +
	"<td><button class='" + ElementList.NAME + 
	"-downvotebtn' data-value='" + id + "'>Down</button></td>" +
        "<td><button class='" + ElementList.NAME +
        "-delbtn' data-value='" + id + "'>Delete</button></td>";
}

/**
 * clickDelete is the code we run in response to a click of a delete button
 */
private static clickDelete() {
    // for now, just print the ID that goes along with the data in the row
    // whose "delete" button was clicked
    let id = $(this).data("value");
    $.ajax({
        type: "DELETE",
        url: "/messages/" + id,
        dataType: "json",
        // TODO: we should really have a function that looks at the return
        //       value and possibly prints an error message.
        success: ElementList.refresh
    });
}

/**
 * clickEdit is the code we run in response to a click of a delete button
 */
private static clickEdit() {
    // as in clickDelete, we need the ID of the row
    let id = $(this).data("value");
    $.ajax({
        type: "GET",
        url: "/messages/" + id,
        dataType: "json",
       // success: editEntryForm.init
       success: ElementList.refresh
    });
}

/**
 * clickUp is the code we run in response to a click of a up vote button
 */	
private static clickUp() {
     // as in clickDelete, we need the ID of the row
     let id = $(this).data("value");
     $.ajax({
	type: "GET",
	url: "/messages/" + id,
	dataType: "json",
	success: ElementList.refresh
	});	
}

/**
 * clickDown is the code we run in response to a click of a down vote button
 */
private static clickDown() {
     // as in clickDelete, we need the ID of the row
     let id = $(this).data("value");
     $.ajax({
        type: "GET",
        url: "/messages/" + id,
        dataType: "json",
        success: ElementList.refresh
        });
}

}