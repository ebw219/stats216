///<reference path="../app.ts"/>
///<reference path="Constants.ts"/>

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
        private static init(id) {
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
                ViewMsg.show(id);
                console.log("viewmsg show called");
                // $("#" + ViewMsg.NAME + "-Close").click(ViewMsg.hide);
                // console.log("hideform called");
                ViewMsg.isInit = true;
            }
        }
    
        /**
         * Refresh() doesn't really have much meaning, but just like in sNavbar, we
         * have a refresh() method so that we don't have front-end code calling
         * init().
         */
        public static refresh(id) {
            console.log("ViewMsg refresh");
            ViewMsg.isInit = false;
            ViewMsg.init(id);
        }
    
    //     /**
    //      * Hide the ViewMsg.  Be sure to clear its fields first
    //      */
        static hide() {
            console.log("entered ViewMsg hide");
            // ViewMsg.isInit=true;
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
        public static show(id) {
            console.log("clicked, entering show");
            // $("#" + ViewMsg.NAME + "-title").val("");
            // $("#" + ViewMsg.NAME + "-message").val("");
            // $("#" + ViewMsg.NAME + "-linkload").val("");
            // $("#" + ViewMsg.NAME + "-pdfload").val("");        
            // //$("#" + ViewMsg.NAME + "-title").show();
            // //ViewMsg.init();
            // console.log("please modal show work");
            // // $("#" + ViewMsg.NAME + "-message").show();
            console.log("about to call getMsg");
            ViewMsg.getMsg(id);
            console.log("about to call show");            
            $('#' + ViewMsg.NAME).modal('show');
        }

        /**
         * getMsg is the code we run to get all the contents of the msg using the msg id
         */
        private static getMsg(id) {
            console.log("entered getMsg");
            // as in clickDelete, we need the ID of the row
            //let id = $(this).data("value");
            console.log("id of the row?: " + id);
            var md = <number>id;
            console.log("casted id of row: " + md);
            $.ajax({
                type: "GET",
                url: Constants.APP_URL + "/messages/" + md,
                dataType: "json",
                // success: editEntryForm.init
                success: ViewMsg.update
            });
            console.log("sent ajax call success, calling ViewMsg.update");
        }

        /**
         * update() is the private method used by refresh() to update the
         * ElementList
         */
        private static update(data: any) {
            console.log("msgtitle: " + data["mData"].mTitle);
            console.log("msgbody: " + data["mData"].mBody);
            console.log("entered ViewMsg function update");
            // Remove the table of data, if it exists
            $("#" + ViewMsg.NAME).remove();
            // Use a template to re-generate the table, and then insert it
            $("body").append(Handlebars.templates[ViewMsg.NAME + ".hb"](data));
            console.log("append");
            // $("#" + ViewMsg.NAME + "-Close").click(ViewMsg.hide);
            // //why doesn't it actually call hide??????????
            // console.log("hideform called");
        }

        private static link(id: string): string {
            return "<td><a href='"
                + id + "type='button' class='btn btn-link' id='ViewMsg-linkload'>" + id + "'</a></td>";                
        }

    }





//    $("#" + Navbar.NAME + "-add").click(NewEntryForm.refresh);
    //change the thing to a button not a link?? open a modal instead of a new page