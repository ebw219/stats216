package edu.lehigh.cse216.lyle.backend;

// Import the Spark package, so that we can make use of the "get" function to
// create an HTTP GET route
//import .MsgDatabase;
import spark.Spark;
import java.util.ArrayList;

// Import Google's JSON library
import com.google.gson.*;
import java.util.Map;
/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {

    public static void main(String[] args) {

	// Get the port on which to listen for requests
	Spark.port(getIntFromEnv("PORT", 4567));

	// gson provides us with a way to turn JSON into objects, and objects
	// into JSON.
	//
	// NB: it must be final, so that it can be accessed from our lambdas
	//
	// NB: Gson is thread-safe.  See
	// https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
	final Gson gson = new Gson();

	//System.out.println("\nuser: " + user + "\n");

	final Database database = Database.getDatabase(getDatabaseUrl()); //changed to database instead of datastore
	final MsgDatabase msgDatabase = MsgDatabase.getMsgDatabase(getDatabaseUrl()); //for tblmessage
	final ComDatabase comDatabase = ComDatabase.getComDatabase(getDatabaseUrl()); //for tblcomment
	
	/*if (!database.tableDoesExist()) {
		System.out.println("Made it in to table doesn't exist");
		database.createTable();
	}*/ //BACKEND SHOULDN'T CREATE A TABLE
	// Set up the location for serving static files.  If the STATIC_LOCATION
	// environment variable is set, we will serve from it.  Otherwise, serve
	// from "/web"
	String static_location_override = System.getenv("STATIC_LOCATION");
	if (static_location_override == null) {
	    Spark.staticFileLocation("/web");
	} else {
	    Spark.staticFiles.externalLocation(static_location_override);
	}

    
	// Set up the location for serving static files
	Spark.staticFileLocation("/web");
	
	// Set up a route for serving the main page
	Spark.get("/", (req, res) -> {
		res.redirect("/index.html");
		return "";
	    });


	//GET route that puts username and password into the users table


	// GET route that returns all message titles and Ids.  All we do is get
	// the data, embed it in a StructuredResponse, turn it into JSON, and
	// return it.  If there's no data, we return "[]", so there's no need
	// for error handling.
	//gets from the message table
	Spark.get("/messages", (request, response) -> {
		// ensure status 200 OK, with a MIME type of JSON
		response.status(200);
		response.type("application/json");
		return gson.toJson(new StructuredResponse("ok", null, msgDatabase.selectAll())); // changed 
	    });

	// GET route that returns everything for a single row in the database.
	// The ":id" suffix in the first parameter to get() becomes
	// request.params("id"), so that we can get the requested row ID.  If
	// ":id" isn't a number, Spark will reply with a status 500 Internal
	// Server Error.  Otherwise, we have an integer, and the only possible
	// error is that it doesn't correspond to a row with data.
	//gets from the message table
	Spark.get("/messages/:message_id", (request, response) -> {
		int idx = Integer.parseInt(request.params("message_id"));
		// ensure status 200 OK, with a MIME type of JSON
		response.status(200);
		response.type("application/json");
		MsgDatabase.RowDataMsg data = msgDatabase.selectOne(idx);
		if (data == null) {
		    return gson.toJson(new StructuredResponse("error", idx + " not found", null));
		} else {
		    return gson.toJson(new StructuredResponse("ok", null, data));
		}
	    });

	// POST route for adding a new element to the database.  This will read
	// JSON from the body of the request, turn it into a SimpleRequest
	// object, extract the title and message, insert them, and return the
	// ID of the newly created row.
	//posts to the message table
	Spark.post("/messages", (request, response) -> {
		// NB: if gson.Json fails, Spark will reply with status 500 Internal
		// Server Error
		SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
		// ensure status 200 OK, with a MIME type of JSON
		// NB: even on error, we return 200, but with a JSON object that
		//     describes the error.
		response.status(200);
		response.type("application/json");
		// NB: createEntry checks for null title and message
		int newId = msgDatabase.insertRow(req.mTitle, req.mBody);
		if (newId == -1) {
		    return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
		} else {
		    return gson.toJson(new StructuredResponse("ok", "" + newId, null));
		}
		});

		//POST route for adding a new element to the comments table
		//posts to the message table
/*	Spark.post("/comments", (request, response) -> {
		// NB: if gson.Json fails, Spark will reply with status 500 Internal
		// Server Error
		SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
		// ensure status 200 OK, with a MIME type of JSON
		// NB: even on error, we return 200, but with a JSON object that
		//     describes the error.
		response.status(200);
		response.type("application/json");
		// NB: createEntry checks for null title and message
		int newId = comDatabase.insertRow(req.mCom);
		if (newId == -1) {
		    return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
		} else {
		    return gson.toJson(new StructuredResponse("ok", "" + newId, null));
		}
		}); */
		//NEED THIS, JUST  COMMENTED OUT TO COMPILE PLS DON'T FORGET TO UNCOMMENT IT
	
		/** 
	// POST route to log in
	Spark.post("/login/:user/:pass", (request, response) -> {
		//status 500 Internal Server Error if gson.Json fails
		SimpleRequest req = gson.fromJson(request.boy(), SimpleRequest.class);
		//ensure status 200 OK
		response.status(200);
		response.type("application/json");
		//check for user and salted hashed password in database
		//if match, create random value and save in local hash
		//return ok and randval
	});
	*/
 
	// // PUT route for updating a row in the Database.  This is almost
	// // exactly the same as POST
	// Spark.put("/messages/:id", (request, response) -> {
	// 	// If we can't get an ID or can't parse the JSON, Spark will send
	// 	// a status 500
	// 	int idx = Integer.parseInt(request.params("id"));
	// 	SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
	// 	// ensure status 200 OK, with a MIME type of JSON
	// 	response.status(200);
	// 	response.type("application/json");
	// 	int result = database.updateOne(idx, req.mTitle, req.mMessage);
	// 	if (result == -1) {
	// 	    return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
	// 	} else {
	// 	    return gson.toJson(new StructuredResponse("ok", null, result));
	// 	}
	//     });
	
	// DELETE route for removing a row from the Database
	//delete from message table
	Spark.delete("/messages/:message_id", (request, response) -> {
		// If we can't get an ID, Spark will send a status 500
		int idx = Integer.parseInt(request.params("message_id"));
		// ensure status 200 OK, with a MIME type of JSON
		response.status(200);
		response.type("application/json");
		// NB: we won't concern ourselves too much with the quality of the
		//     message sent on a successful delete
		int result = msgDatabase.deleteRow(idx);
		if (result == -1) {
		    return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
		} else {
		    return gson.toJson(new StructuredResponse("ok", null, null));
		}
	    });


	// PUT route for updating the upvote in the Database
	Spark.put("/messages/upVote/:id", (request, response) -> {
		// If we can't get an ID or can't parse the JSON, Spark will send
		// a status 500
		int idx = Integer.parseInt(request.params("id"));
		//int votex = Integer.parseInt(request.params("votes"));
		//SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
		// ensure status 200 OK, with a MIME type of JSON
		response.status(200);
		response.type("application/json");
		int result = database.upVote(idx);
		if (result == -1) {
			return gson.toJson(new StructuredResponse("error", "unable to update vote " + idx, null));
		} else {
			return gson.toJson(new StructuredResponse("ok", null, result));
		}
	});
	
	// PUT route for updating the downvote in the Database
	Spark.put("/messages/downVote/:id", (request, response) -> {
		// If we can't get an ID or can't parse the JSON, Spark will send
		// a status 500
		int idx = Integer.parseInt(request.params("id"));
		//int votex = Integer.parseInt(request.params("votes"));
		//SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
		// ensure status 200 OK, with a MIME type of JSON
		response.status(200);
		response.type("application/json");
		int result = database.downVote(idx);
		if (result == -1) {
			return gson.toJson(new StructuredResponse("error", "unable to update vote " + idx, null));
		} else {
			return gson.toJson(new StructuredResponse("ok", null, result));
		}
	});

    }

/**
 * Get an integer environment varible if it exists, and otherwise return the
 * default value.
 * 
 * @envar      The name of the environment variable to get.
 * @defaultVal The integer value to use as the default if envar isn't found
 * 
 * @returns The best answer we could come up with for a value for envar
 */
static int getIntFromEnv(String envar, int defaultVal) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get(envar) != null) {
        return Integer.parseInt(processBuilder.environment().get(envar));
    }
    return defaultVal;
}    

static String getDatabaseUrl() {
	ProcessBuilder processBuilder = new ProcessBuilder();
    return processBuilder.environment().get("JDBC_DATABASE_URL");
} 


}