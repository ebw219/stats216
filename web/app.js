/// <reference path="ts/EditEntryForm.ts"/>
/// <reference path="ts/NewEntryForm.ts"/>
/// <reference path="ts/ElementList.ts"/>
/// <reference path="ts/Navbar.ts"/>
/// <reference path="ts/LoginForm.ts"/>
/// <reference path="ts/LoginOAuth.ts"/>
// Prevent compiler errors when using jQuery.  "$" will be given a type of 
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $;
// Prevent compiler errors when using Handlebars
var Handlebars;
// a global for the EditEntryForm of the program.  See newEntryForm for 
// explanation
var editEntryForm;
// Run some configuration code when the web page loads
$(document).ready(function () {
    Navbar.refresh();
    ElementList.refresh();
    LoginOAuth.init();
});
