/**
 * The Navbar Singleton is the navigation bar at the top of the page.  Through 
 * its HTML, it is designed so that clicking the "brand" part will refresh the
 * page.  Apart from that, it has an "add" button, which forwards to 
 * NewEntryForm
 */
class Navbar {
    /**
     * Track if the Singleton has been initialized
     */
    private static isInit = false;

    /**
     * The name of the DOM entry associated with Navbar
     */
    private static readonly NAME = "Navbar";

    /**
     * Initialize the Navbar Singleton by creating its element in the DOM and
     * configuring its button.  This needs to be called from any public static
     * method, to ensure that the Singleton is initialized before use.
     */
    private static init() {
        console.log("entered navbar init");
        console.log("navbar.isInit: " + Navbar.isInit);
        if (!Navbar.isInit) {
            console.log("entered navbar init if statement");
            $("body").prepend(Handlebars.templates[Navbar.NAME + ".hb"]());
            $("#" + Navbar.NAME + "-add").click(NewEntryForm.refresh);
            console.log("show newentryform called");
            //click on test button
            $("#" + Navbar.NAME + "-testviewmsg").click(ViewMsg.refresh);
            console.log("testviewmsg clicked");
            Navbar.isInit = true;
        }
    }

    /**
     * Refresh() doesn't really have much meaning for the navbar, but we'd 
     * rather not have anyone call init(), so we'll have this as a stub that
     * can be called during front-end initialization to ensure the navbar
     * is configured.
     */
    public static refresh() {
        console.log("navbar refresh");
        Navbar.init();
    }
}