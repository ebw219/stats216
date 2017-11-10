/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/LoginForm.ts"/>
/// <reference path="ts/LoginOAuth.ts"/>

// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;

// Prevent compiler errors when using Handlebars
var Handlebars: any;

// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation
var editEntryForm: EditEntryForm;

// Run some configuration code when the web page loads
$(document).ready(function () {
    // LoginOAuth.init();
    Navbar.refresh();
    //NewEntryForm.refresh();
    $("#Navbar-add").click(NewEntryForm.refresh());
    console.log("hopefully newentryform refresh only called on navbar-add click");
    ElementList.refresh();
    console.log("app.ts refreshes done");    

    // // Create the object that controls the "Edit Entry" form
    // editEntryForm = new EditEntryForm();
    // // set up initial UI state
    // console.log("new editentryform");
    //$("#editElement").hide();
});