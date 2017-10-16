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
    private PreparedStatement mSelectMsgUserId;

    /**
     * A prepared statement for getting the messages upvoted by user
     */
    private PreparedStatement mSelectUpVotesMsg;

    /**
     * A prepared statement for getting the messages downvoted by user
     */
    private PreparedStatement mSelectDownVotesMsg;

    /**
     * A prepared statement for getting the messages posted by a specific user
     */
    private PreparedStatement mSelectByUser;

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
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO " + tblMessage + " VALUES (default, ?, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM " + tblMessage);
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * FROM " + tblMessage + " WHERE message_id = ?");
            db.mSelectMsgUserId = db.mConnection.prepareStatement("SELECT * FROM " + tblMessage + " WHERE user_id = ?");
            db.mSelectUpVotesMsg = db.mConnection.prepareStatement("SELECT * FROM " + tblMessage 
                        + " INNER JOIN " + UpVoteDatabase.getTblUpVote() 
                        + " ON tblUpVotes.user_id = tblMessage.user_id WHERE tblMessage.user_id = ? ORDER BY tblMessage.message_id DESC");
            db.mSelectDownVotesMsg = db.mConnection.prepareStatement("SELECT * FROM " + tblMessage
                        + " INNER JOIN " + DownVoteDatabase.getTblDownVote()
                        + " ON tblDownVotes.user_id = tblMessage.user_id WHERE tblMessage.user_id = ? ORDER by tblMessage.message_id DESC");

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
     * @param title The title for this new row
     * @param body The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(int user_id, String title, String body) {
        int count = 0;
        try {
            mInsertOne.setInt(1, user_id);
            mInsertOne.setString(2, title);
            mInsertOne.setString(3, body);
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

    /**
     * Get all messages for a specific user ID
     * 
     * @param user_id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    ArrayList<RowDataMsg> selectMsgUserId(int user_id) {
        ArrayList<RowDataMsg> res = new ArrayList<RowDataMsg>();
        try {
            mSelectMsgUserId.setInt(1, user_id);
            ResultSet rs = mSelectMsgUserId.executeQuery();
            while (rs.next()) {
                res.add(new RowDataMsg(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("body")));
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
     * Query database for all messages upvoted by a specific user, using a JOIN sql statement
     * 
     * @param user_id The id of the message
     * 
     * @return The data for row that match as an ArrayList
     */
    ArrayList<RowDataMsg> selectUpVotesMsg(int user_id) {
        ArrayList<RowDataMsg> res = new ArrayList<RowDataMsg>();
        try {
            mSelectUpVotesMsg.setInt(1, user_id);
            ResultSet rs = mSelectUpVotesMsg.executeQuery();
            while (rs.next()) {
                res.add(new RowDataMsg(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("body")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query database for all messages downvoted by a specific user, using a JOIN sql statement
     * 
     * @param user_id The id of the message
     * 
     * @return The data for row that match as an ArrayList
     */
    ArrayList<RowDataMsg> selectDownVotesMsg(int user_id) {
        ArrayList<RowDataMsg> res = new ArrayList<RowDataMsg>();
        try {
            mSelectDownVotesMsg.setInt(1, user_id);
            ResultSet rs = mSelectDownVotesMsg.executeQuery();
            while (rs.next()) {
                res.add(new RowDataMsg(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"), rs.getString("body")));
            }
            Collections.reverse(res);
            rs.close();
            return res;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
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