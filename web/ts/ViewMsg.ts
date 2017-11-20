///<reference path="../app.ts"/>
///<reference path="Constants.ts"/>
// /**
//  * The ViewMsg Singleton provides a way of displaying all of the data
//  * stored on the server as an HTML table.
//  */
// class ViewMsg {
//     /**
//      * The name of the DOM entry associated with ViewMsg
//      */
//     private static readonly NAME = "ViewMsg";

//     /**
//      * Track if the Singleton has been initialized
//      */
//     private static isInit = false;

//     /**
//      * Initialize the ViewMsg singleton by creating its element in the DOM.
//      * This needs to be called from any public static method, to ensure the
//      * Singleton is initialized before use
//      */
//     private static init() {
//         if (!ViewMsg.isInit) {
//             ViewMsg.isInit = true;
//         }
//     }

//     /**
//      * refresh() is the public method for updating the ViewMsg
//      */
//     public static refresh() {
//         // Make sure the singleton is initialized
//         ViewMsg.init();
//         let mId = $(this).data("value");

//         // Issue a GET, and then pass the result to update()
//         $.ajax({
//             type: "GET",
//             url: Constants.APP_URL + "/messages/" + mId,
//             dataType: "json",
//             success: ViewMsg.update
//         });
//     }

//     /**
//      * update() is the private method used by refresh() to update the
//      * ViewMsg
//      */
//     private static update(data: any) {
//         let mId = $(this).data("value");
//         console.log("title: " + data["mData"][mId].mTitle);
//         console.log("body: " + data["mData"][mId].mBody);
//         // Remove the table of data, if it exists
//         // $("#" + ElementList.NAME).remove();
//         // // Use a template to re-generate the table, and then insert it
//         // $("body").append(Handlebars.templates[ElementList.NAME + ".hb"](data));
//         // console.log("append");
//         // //click on message
//         // $("#" + ElementList.NAME + "-viewmsg").click(ElementList.clickMsg);
//         // // Find all of the delete buttons, and set their behavior
//         // $("." + ElementList.NAME + "-delbtn").click(ElementList.clickDelete);
//         // // Find all of the Edit buttons, and set their behavior
//         // $("." + ElementList.NAME + "-editbtn").click(ElementList.clickEdit);
//         // // Find all of the UpVote buttons and set their behavior
//         // $("." + ElementList.NAME + "-upvotebtn").click(ElementList.clickUp);
//         // // Find all of the DownVote buttons and set their behavior
//         // $("." + ElementList.NAME + "-downvotebtn").click(ElementList.clickDown);

//     }

//     /**
//      * clickMsg is the code we run in response to a click on a message title
//      */
//     // private static clickMsg() {
//     //     console.log("msg clicked, called method clickMsg()");
//     // }

//     /**
//      * clickDelete is the code we run in response to a click of a delete button
//      */
//     // private static clickDelete() {
//     //     // for now, just print the ID that goes along with the data in the row
//     //     // whose "delete" button was clicked
//     //     let id = $(this).data("value");
//     //     $.ajax({
//     //         type: "DELETE",
//     //         url: Constants.APP_URL + "/messages/" + id,
//     //         dataType: "json",
//     //         // TODO: we should really have a function that looks at the return
//     //         //       value and possibly prints an error message.
//     //         success: ElementList.refresh
//     //     });
//     // }

// }













/**
 * ViewMsg encapsulates all of the code for the form for adding an entry
 */
class ViewMsg {
    
        /**
         * The name of the DOM entry associated with ViewMsg
         */
        static readonly NAME = "ViewMsg";
    
        /**
         * Track if the Singleton has been initialized
         */
        private static isInit = false;
    
        /**
         * Initialize the ViewMsg by creating its element in the DOM and 
         * configuring its buttons.  This needs to be called from any public static 
         * method, to ensure that the Singleton is initialized before use
         */
        private static init() {
            console.log("entered ViewMsg init");
    //         //ViewMsg.isInit = false;
            console.log("isInit: " + ViewMsg.isInit);
            if (!ViewMsg.isInit) {
                console.log("entered ViewMsg init if statement");
                $("body").append(Handlebars.templates[ViewMsg.NAME + ".hb"]());
                //$("body").append(Handlebars.templates[ViewMsg.NAME + ".hb"]());                
                //$("#Navbar-add").click(ViewMsg.show());            
                //$("#" + ViewMsg.NAME + "-OK").click(ViewMsg.show());
                //new entry form shows up when the page loads
                ViewMsg.show();
                console.log("viewmsg show called");
                $("#" + ViewMsg.NAME + "-Close").click(ViewMsg.hide);
                console.log("hideform called");
                ViewMsg.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh() {
            console.log("ViewMsg refresh");
            ViewMsg.isInit = false;
            ViewMsg.init();
        }
    
    //     /**
    //      * Hide the ViewMsg.  Be sure to clear its fields first
    //      */
        static hide() {
            console.log("entered ViewMsg hide");
            // $("#" + ViewMsg.NAME + "-title").val("");
            // $("#" + ViewMsg.NAME + "-message").val("");
            // $("#" + ViewMsg.NAME + "-linkload").val("");
            // $("#" + ViewMsg.NAME + "-pdfload").val("");        
            $("#" + ViewMsg.NAME).modal("hide");
        }
    // /**
    //      * Show the ViewMsg.  Be sure to clear its fields, because there are
    //      * ways of making a Bootstrap modal disapper without clicking Close, and
    //      * we haven't set up the hooks to clear the fields on the events associated
    //      * with those ways of making the modal disappear.
    //      */
        public static show() {
            console.log("clicked, entering show");
            // $("#" + ViewMsg.NAME + "-title").val("");
            // $("#" + ViewMsg.NAME + "-message").val("");
            // $("#" + ViewMsg.NAME + "-linkload").val("");
            // $("#" + ViewMsg.NAME + "-pdfload").val("");        
            // //$("#" + ViewMsg.NAME + "-title").show();
            // //ViewMsg.init();
            // console.log("please modal show work");
            // // $("#" + ViewMsg.NAME + "-message").show();
            $('#' + ViewMsg.NAME).modal('show');
        }
    }





//    $("#" + Navbar.NAME + "-add").click(NewEntryForm.refresh);
    //change the thing to a button not a link?? open a modal instead of a new page