package edu.lehigh.cse216.lyle.backend;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;

public class MsgDatabase {

    private static final String tblMessage = "tblMessage";
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
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

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
    public static class RowDataMsg {
        /**
         * The ID of this row of the database
         */
        int mId;
        /**
         * The user id stored in this row
         */
        int uId;
        /**
         * The title stored in this row
         */
        String mTitle;

        /**
         * The  body of the message stored in this row
         */
        String mBody;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataMsg(int message_id, int user_id, String title, String body) {
            mId = message_id;
            uId = user_id;
            mTitle = title;
            mBody = body;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private MsgDatabase() {
    }

    public static String getTblMessage() {
        return tblMessage;
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
    static MsgDatabase getMsgDatabase(String url) {
        // Create an un-configured Database object
        MsgDatabase db = new MsgDatabase();
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
        try {
            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM " + tblMessage + " WHERE message_id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO " + tblMessage + " VALUES (default, default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM " + tblMessage);
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from " + tblMessage + " WHERE message_id = ?");
            db.mSelectUserId = db.mConnection.prepareStatement("SELECT * " + tblMessage + " WHERE user_id = ?");

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
     * @param title The title for this new row
     * @param body The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String title, String body) {
        int count = 0;
        try {
            mInsertOne.setString(1, title);
            mInsertOne.setString(2, body);
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
    ArrayList<RowDataMsg> selectAll() {
        ArrayList<RowDataMsg> res = new ArrayList<RowDataMsg>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataMsg(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("body")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //DOES THIS NEED TO BE AN ARRAYLIST
    /**
     * Get all data for a specific row, by user ID
     * 
     * @param user_id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataMsg selectUserId(int user_id) {
        RowDataMsg res = null;
        try {
            mSelectUserId.setInt(1, user_id);
            ResultSet rs = mSelectUserId.executeQuery();
            if (rs.next()) {
                res = new RowDataMsg(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("body"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param message_id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataMsg selectOne(int message_id) {
        RowDataMsg res = null;
        try {
            mSelectOne.setInt(1, message_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataMsg(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("body"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param message_id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int message_id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, message_id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}