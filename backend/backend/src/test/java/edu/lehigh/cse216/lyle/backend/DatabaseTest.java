package edu.lehigh.cse216.lyle.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import spark.Spark;

/**
 * Unit test for simple App.
 */
public class DatabaseTest 
    extends TestCase
{

    String db = "jdbc:postgresql://ec2-54-225-237-64.compute-1.amazonaws.com:5432/d9m9llpg6sa3t3?user=dosnlfxouuassv&password=62f6ee7278c7dba70ef3cbc252324cb643b4b326094319a7130fed897b84d0d1&sslmode=require";
    String title = "Test Title";
    String content = "Test Content";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DatabaseTest( String testName )
    {

        super( testName );
    }

    public void testApp()
    {
        assertTrue( true );
    }

    /**
     * Test if title and content of message is correct
     */
//    public void testCreateEntry(){
//
//        final Database database = Database.getDatabase("jdbc:postgresql://ec2-54-225-237-64.compute-1.amazonaws.com:5432/d9m9llpg6sa3t3?user=dosnlfxouuassv&password=62f6ee7278c7dba70ef3cbc252324cb643b4b326094319a7130fed897b84d0d1&sslmode=require"); //changed to database instead of datastore
//
//        int newId = database.insertRow(this.title, this.content);
//
//        System.out.println("NEWID:  " + database.selectOne(newId));
//
//        assertTrue((database.selectOne(newId).mTitle).equals(this.title));
//        assertTrue((database.selectOne(newId).mMessage).equals(this.content));

//    }

    /**
     * Test if upvote request words
     */
    public void upVote(){

        final Database database = Database.getDatabase(db);

        database.insertRow(this.title, this.content);
//        database.upVote()


    }


}
