package edu.lehigh.cse216.lyle.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import spark.Spark;

/**
 * Unit test for simple App.
 */
public class MsgDatabaseTest extends TestCase {

    String db = "jdbc:postgresql://ec2-54-225-237-64.compute-1.amazonaws.com:5432/d9m9llpg6sa3t3?user=dosnlfxouuassv&password=62f6ee7278c7dba70ef3cbc252324cb643b4b326094319a7130fed897b84d0d1&sslmode=require";
    int message_id = 1;
    int user_id = 1;
    String title = "Test Title";
    String content = "Test Content";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MsgDatabaseTest( String testName ) {
        super( testName );
    }

    public void testApp() {
        assertTrue( true );
    }

    /**
     * Test if upvote request words
     */
    /*public void testMsg(){

        final MsgDatabase msgDatabase = MsgDatabase.getMsgDatabase(db);

        MsgDatabase.RowDataMsg test = new RowDataMsg(message_id, user_id, title, content);
        //msgDatabase.insertRow(this.user_id, this.title, this.content);
        assertTrue(test.message_id = test.mId);
        


        //assertTrue(d2.mTitle.equals(d.mTitle));
//        database.upVote()

//res.add(new RowDataMsg(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("body")));

    }
    */


}
