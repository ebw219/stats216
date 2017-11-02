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
