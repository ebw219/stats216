package edu.lehigh.cse216.lyle.backend;

import edu.lehigh.cse216.lyle.backend.ComDatabase.RowDataCom;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;

public class ComDatabase {

    //NOTE: this table is tblcomments in the database, plural
    private static final String tblComment = "tblComments";
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne; //do we need this for comments?

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for getting all the comments for a specific message
     */
    private PreparedStatement mSelectMsgId;
    /**
     * A prepared statement for getting all the rows in the database with the same user id
     */
    private PreparedStatement mSelectUserId;

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
    public static class RowDataCom {
        /**
         * The ID of this row of the database
         */
        int cId;
        /**
         * The user id stored in this row
         */
        int uId;
        /**
         * The message id stored in this row
         */
        int mId;
        /**
         * The comment stored in this row
         */
        String mCom;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataCom(int comment_id, int user_id, int message_id, String comment_text) {
            cId = comment_id;
            uId = user_id;
            mId = message_id;            
            mCom = comment_text;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private ComDatabase() {
    }

    public static String getTblComment() {
        return tblComment;
    }

    public static String getComMessage() {
        return tblComment;
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
    static ComDatabase getComDatabase(String url) {
        // Create an un-configured Database object
        ComDatabase db = new ComDatabase();
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
                CREATE TABLE IF NOT EXISTS tblComments (
                comment_id SERIAL PRIMARY KEY,
                user_id INTEGER,
                message_id INTEGER,
                comment_text VARCHAR(255),
                # Need to add creation date/time
                FOREIGN KEY (user_id) REFERENCES tblUser (user_id),
                FOREIGN KEY (message_id) REFERENCES tblMessage (message_id)
                );
                */

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM " + tblComment + " WHERE comment_id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO " + tblComment + " VALUES (default, ?, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM " + tblComment);
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * FROM " + tblComment + " WHERE comment_id = ?");
            db.mSelectMsgId = db.mConnection.prepareStatement("SELECT * FROM " + tblComment 
                    + " INNER JOIN " + MsgDatabase.getTblMessage() 
                    + " ON tblComments.message_id = tblMessage.message_id WHERE tblMessage.message_id = ? ORDER BY comment_id DESC");

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
     * @param comment_text The comment for this new row
     * @param message_id The message id for this comment
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(int user_id, int message_id, String comment_text) {
        int count = 0;
        try {
            mInsertOne.setInt(1, user_id);
            mInsertOne.setInt(2, message_id);
            mInsertOne.setString(3, comment_text);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all titles and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataCom> selectAll() {
        ArrayList<RowDataCom> res = new ArrayList<RowDataCom>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataCom(rs.getInt("comment_id"), rs.getInt("user_id"), rs.getInt("message_id"), rs.getString("comment_text")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query database for all comments on a specific message, using a JOIN sql statement
     * 
     * @param message_id The id of the message
     * 
     * @return The data for row that match as an ArrayList
     */
    ArrayList<RowDataCom> selectMsgId(int message_id) {
        ArrayList<RowDataCom> res = new ArrayList<RowDataCom>();
        try {
            mSelectMsgId.setInt(1, message_id);
            ResultSet rs = mSelectMsgId.executeQuery();
            System.out.println("mSelectMsgId: " + rs);
            while (rs.next()) {
                res.add(new RowDataCom(rs.getInt("comment_id"), rs.getInt("user_id"), rs.getInt("message_id"), rs.getString("comment_text")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    //this prepared statement doesn't exist please fix it
    /**
     * Query database by user ID
     * 
     * @param user_id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    ArrayList<RowDataCom> selectUserId(int user_id) {
        ArrayList<RowDataCom> res = new ArrayList<RowDataCom>();
        try {
            mSelectUserId.setInt(1, user_id);
            ResultSet rs = mSelectUserId.executeQuery();
            while (rs.next()) {
                res.add(new RowDataCom(rs.getInt("comment_id"), rs.getInt("user_id"), rs.getInt("message_id"), rs.getString("comment_text")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //change this to be get all rows with a specific id
    /**
     * Get all data for a specific row, by ID
     * 
     * @param comment_id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataCom selectOne(int comment_id) {
        RowDataCom res = null;
        try {
            mSelectOne.setInt(1, comment_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataCom(rs.getInt("comment_id"), rs.getInt("user_id"), rs.getInt("message_id"), rs.getString("comment_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param comment_id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int comment_id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, comment_id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}