package edu.lehigh.cse216.lyle.backend;

import edu.lehigh.cse216.lyle.backend.UpVoteDatabase.RowDataUpVote;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;

public class UpVoteDatabase {

    private static final String tblUpVotes = "tblupvotes";
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database for one message
     */
    private PreparedStatement mSelectMsgId;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne; //do we need this for votes?

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for counting the number of rows in the database with the same message id
     */
    private PreparedStatement mCountUpVotes;

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class RowDataUpVote {
        /**
         * The user id stored in this row
         */
        int uId;
        /**
         * The message id stored in this row
         */
        int mId;
    

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataUpVote(int user_id, int message_id) {
            uId = user_id;
            mId = message_id;            
        }
    }
    

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private UpVoteDatabase() {
    }

    public static String getTblUpVote() {
        return tblUpVotes;
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static UpVoteDatabase getUpVoteDatabase(String url) {
        // Create an un-configured Database object
        UpVoteDatabase db = new UpVoteDatabase();
        // Give the Database object a connection, fail if we cannot get one

        try {
            Connection conn = getConnection(url);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;

        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.err.println("Error: DriverManager.getConnection() threw a URISyntaxException");
            e.printStackTrace();
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {/*
                CREATE TABLE IF NOT EXISTS tblUpVotes (
                user_id INTEGER,
                message_id INTEGER,
                FOREIGN KEY (user_id) REFERENCES tblUser (user_id),
                FOREIGN KEY (message_id) REFERENCES tblMessage (message_id),
                PRIMARY KEY (user_id, message_id)
                );
                */

                //where user_id and message_id
            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM " + tblUpVotes + " WHERE user_id = ? AND message_id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO " + tblUpVotes + " VALUES (?, ?)");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from " + tblUpVotes + " WHERE user_id = ? AND message_id = ?");
            db.mSelectMsgId = db.mConnection.prepareStatement("SELECT * FROM " + tblUpVotes 
                        + " INNER JOIN " + MsgDatabase.getTblMessage() 
                        + " ON tblUpVotes.message_id = tblMessage.message_id WHERE tblMessage.message_id = ? ORDER BY tblMessage.message_id DESC");
            db.mCountUpVotes = db.mConnection.prepareStatement("SELECT COUNT(message_id) FROM " + tblUpVotes + " WHERE message_id = ?");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    
    private static Connection getConnection(String dbUrl) throws URISyntaxException, SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the database
     * 
     * @param user_id The user id for this new row
     * @param message_id The message id for this row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(int user_id, int message_id) {
        int count = 0;
        try {
            mInsertOne.setInt(1, user_id);
            mInsertOne.setInt(2, message_id);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    //change this to be get all rows with a specific id
    /**
     * Get all data for a specific row, by ID
     * 
     * @param user_id The user id of the row being requested
     * @param message_id The message id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataUpVote selectOne(int user_id, int message_id) {
        RowDataUpVote res = null;
        try {
            mSelectOne.setInt(1, user_id);
            mSelectOne.setInt(2, message_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataUpVote(rs.getInt("user_id"), rs.getInt("message_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Query database by user ID
     * 
     * @param message_id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    ArrayList<RowDataUpVote> selectMsgId(int message_id) {
        ArrayList<RowDataUpVote> res = new ArrayList<RowDataUpVote>();
        try {
            mSelectMsgId.setInt(1, message_id);
            ResultSet rs = mSelectMsgId.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUpVote(rs.getInt("user_id"), rs.getInt("message_id")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * count number of upvotes for a specific message_id
     * 
     * @param message_id The message_id of the row
     * 
     * @return The number of rows that match this message_id
     */
    int countUpVotes(int message_id) {
        ArrayList<RowDataUpVote> res = new ArrayList<RowDataUpVote>();
        int count = 0;
        System.out.println("count before: " + count);
        try {
            mCountUpVotes.setInt(1, message_id);
            ResultSet rs = mCountUpVotes.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUpVote(rs.getInt("user_id"), rs.getInt("message_id")));
                count++;
            }
            //count = rs.getInt(1);
            //count += mCountUpVotes.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("upvote count: " + count);
        return count;
    }

    /**
     * ArrayList<RowDataUpVote> selectMsgId(int message_id) {
        ArrayList<RowDataUpVote> res = new ArrayList<RowDataUpVote>();
        try {
            mSelectMsgId.setInt(1, message_id);
            ResultSet rs = mSelectMsgId.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUpVote(rs.getInt("user_id"), rs.getInt("message_id")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
     */

    /**
     * Delete a row by ID
     * 
     * @param user_id
     * @param message_id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int user_id, int message_id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, user_id);
            mDeleteOne.setInt(2, message_id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}