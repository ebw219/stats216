(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['EditEntryForm.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['NewEntryForm.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "";
},"useData":true});
})();

(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['LoginOAuth.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div class=\"panel panel-default\" id=\"Login\">\n    <div class=\"panel-heading\">\n        <h3 class=\"panel-title\">Login to The Buzz</h3>\n    </div>\n    <td>\n        <a href=\"https://accounts.google.com/o/oauth2/auth?\n    redirect_uri=https://www.getpostman.com/oauth2/callback/&\n    response_type=code&\n    client_id=325681108859-g2tq47a7h6pvnfo159h0aoaluto67kqv.apps.googleusercontent.com&\n    scope=https://www.googleapis.com/auth/analytics.readonly+https://www.googleapis.com/auth/userinfo.email&\n    approval_prompt=force&\n    access_type=offline\">\n            <button id=\"LoginOAuth-login\">Login</button>\n        </a>\n    </td>\n</div>";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ElementList.hb'] = template({"1":function(container,depth0,helpers,partials,data) {
    var alias1=container.lambda, alias2=container.escapeExpression;

  return "         <tr>\n                <td>"
    + alias2(alias1((depth0 != null ? depth0.mTitle : depth0), depth0))
    + "</td>\n              	<td><button class=\"ElementList-editbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.mId : depth0), depth0))
    + "\">Edit</button></td>\n              	<td><button class=\"ElementList-delbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.mId : depth0), depth0))
    + "\">Delete</button></td>\n              	<td><button class=\"ElementList-upvotebtn\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.mId : depth0), depth0))
    + "\">Up Vote</button></td>\n	      	<td><button class=\"ElementList-downvotebtn\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.mId : depth0), depth0))
    + "\">Down Vote</button></td>\n 		<td><button class=\"ElementList-addcmntbtn\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.mId : depth0), depth0))
    + "\">Add Comment</button></td>\n 		<td><div id=\"votes\" data-value=\""
    + alias2(alias1((depth0 != null ? depth0.mId : depth0), depth0))
    + "\">Votes</div></td>\n	</tr>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"panel panel-default\" id=\"ElementList\">\n     <div class=\"panel-heading\">\n     	  <h3 class=\"panel-title\">All Messages</h3>\n	  </div>\n    <table class=\"table\">\n        <tbody>\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : (container.nullContext || {}),(depth0 != null ? depth0.mData : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "            </tbody>\n    	</table>\n    </div>\n";
},"useData":true});
})();
(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['LoginForm.hb'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<!--\n<div id=\"LoginForm\" class=\"modal fade\" role=\"dialog\">\n    <div class=\"modal-dialog\">\n        <div class=\"modal-content\">\n            <div class=\"modal-header\">\n                <h4 class=\"modal-title\">Log In</h4>\n            </div>\n            <div class=\"modal-body\">\n                <label for=\"LogIn-title\">Title</label>\n                <input class=\"form-control\" type=\"text\" id=\"LogIn-title\" />\n                <label for=\"LogIn-message\">Message</label>\n                <textarea class=\"form-control\" id=\"LogIn-message\"></textarea>\n            </div>\n            <div class=\"modal-footer\">\n                <button type=\"button\" class=\"btn btn-default\" id=\"LogInForm-OK\">OK</button>\n                <button type=\"button\" class=\"btn btn-default\" id=\"LogInForm-Close\">Close</button>\n            </div>\n        </div>\n    </div>\n</div>\n-->\n\n<div class=\"panel panel-default\" id=\"Login\">\n    <div class=\"panel-heading\">\n        <h3 class=\"panel-title\">Login to The Buzz</h3>\n    </div>\n    <label>User</label>\n    <input class=\"form-control\" type=\"text\" id=\"LoginForm-username\"/>\n    <label>Pass</label>\n    <input class=\"form-control\" type=\"text\" id=\"LoginForm-userPass\"/>\n    <td><button class=\"LoginForm-login\">Login</button></td>\n    <td><button class=\"LoginForm-signup\">Sign Up</button></td>\n</div>";
},"useData":true});
})();
