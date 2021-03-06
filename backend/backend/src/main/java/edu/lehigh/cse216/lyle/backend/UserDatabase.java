package edu.lehigh.cse216.lyle.backend;

import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;

public class UserDatabase {

    static byte[] salt;

    private static final String tblUser = "tblUser";
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
     * A prepared statement for getting all messages for a specific user.
     */
    private PreparedStatement mSelectMsgId;

    /**
     * A prepared statement for getting all comments for a specific user.
     */
    private PreparedStatement mSelectComId;

    /**
     * A prepared statement for getting all the upvotes for a specific user.
    */
    private PreparedStatement mSelectUpVotes;

    /**
     * A prepared statement for getting all the downvotes for a specific user.
    */
    private PreparedStatement mSelectDownVotes;

    /**
     * A prepared statement for getting a row in the user table for a specific username
    */
    private PreparedStatement mSelectUsername;

    /**
     * A prepared statement for getting a row in the user table for a specific password
    */
    private PreparedStatement mSelectUserPass;
    
    /**
     * A prepared statement for geting the authentication value of a user (0 if not, 1 if valid)
     */
    private PreparedStatement mSelectAuth;


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
    /*CREATE TABLE IF NOT EXISTS tblUser (
        user_id SERIAL PRIMARY KEY,
        username VARCHAR(255),
        realname VARCHAR(255),
        email VARCHAR(255),
        salt BYTEA,
        password BYTEA
        );*/
    public static class RowDataUser {
        /**
         * The user id
         */
        int uId;
        /**
         * The username stored in this row
         */
        String username;
        /**
         * The real name stored in this row
         */
        String realname;
        /**
         * The email stored in this row
         */
        String email;
        /**
         * The salt stored in this row
         */
        byte[] salt;
        /**
         * The salted password stored in this row
         */
        byte[] password;
        /**
         * Value of the user's authorization -- 0 for unauthorized, 1 for not
         */
        int auth;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataUser(int user_id, String user, String real, String email, byte[] salt, byte[] pass, int auth) {
            uId = user_id;
            username = user;
            realname = real;
            email = email;
            salt = salt;
            password = pass;
            auth = auth;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private UserDatabase() {
    }

    public static String getTblUser() {
        return tblUser;
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
    static UserDatabase getUserDatabase(String url) {
        // Create an un-configured Database object
        UserDatabase db = new UserDatabase();
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
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM " + tblUser + " WHERE user_id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO " + tblUser + " VALUES (default, ?, ?, ?, ?, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM " + tblUser);
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * FROM " + tblUser + " WHERE user_id = ?");
            db.mSelectMsgId = db.mConnection.prepareStatement("SELECT * FROM " + MsgDatabase.getTblMessage()
                                + "INNER JOIN" + tblUser
                                + "ON tblUser.user_id = tblMessage.user_id WHERE tblUser.user_id = ? ORDER BY tblMessage.message_id DESC");                    
            db.mSelectComId = db.mConnection.prepareStatement("SELECT * FROM " + ComDatabase.getTblComment()
                                + "INNER JOIN" + tblUser
                                + "ON tblUser.user_id = tblComments.user_id WHERE tblUser.user_id = ? ORDER BY tblComments.comment_id DESC");
            db.mSelectUpVotes = db.mConnection.prepareStatement("SELECT * FROM " + UpVoteDatabase.getTblUpVote()
                                + "INNER JOIN" + tblUser
                                + "ON tblUser.user_id = tblUpVotes.user_id WHERE tblUser.user_id = ? ORDER BY tlbUser.user_id DESC");
            db.mSelectDownVotes = db.mConnection.prepareStatement("SELECT * FROM " + DownVoteDatabase.getTblDownVote()
                                + "INNER JOIN" + tblUser
                                + "ON tblUser.user_id = tblUpVotes.user_id WHERE tblUser.user_id = ? ORDER BY tblUser.user_id DESC");
            db.mSelectUsername = db.mConnection.prepareStatement("SELECT * FROM " + tblUser + " WHERE username = ?");
            db.mSelectUserPass = db.mConnection.prepareStatement("SELECT * FROM " + tblUser + " WHERE username = ? AND password = ?");
            db.mSelectAuth = db.mConnection.prepareStatement("SELECT * FROM " + tblUser + " WHERE username = ?");
            
            
            /*db.mSelectMsgId = db.mConnection.prepareStatement("SELECT * FROM " + tblComment 
                    + " INNER JOIN " + MsgDatabase.getTblMessage() 
                    + " ON tblComment.message_id = tblMessage.message_id WHERE tblMessage.message_id = ? ORDER BY comment_id DESC");

                    */

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
     * @param username The username for this new row
     * @param realname The username for this row
     * @param email The email for this row
     * @param password
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String username, String realname, String email, String password) {
        int count = 0;
        int auth = 0;
        RowDataUser res = null;
        ResultSet rs = null;
        try {
            mSelectUsername.setString(1, username);
            rs = mSelectUsername.executeQuery();
            if (rs.next()) {
                res = new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth"));
                if (username.equals(rs.getString("username"))) {
                    count = -2;
                    return count;
                }
            } else {
                salt = SaltRegister.getSalt();
                byte[] pass = SaltRegister.saltReg(password.getBytes());            
                mInsertOne.setString(1, username);
                mInsertOne.setString(2, realname);
                mInsertOne.setString(3, email);
                mInsertOne.setBytes(4, salt);
                mInsertOne.setBytes(5, pass);
                mInsertOne.setInt(6, auth);
                count += mInsertOne.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all users and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowDataUser> selectAll() {
        ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth")));
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
     * Query database for all messages from a specific user, using a JOIN sql statement
     * 
     * @param user_id The id of the message
     * 
     * @return The data for row that match as an ArrayList
     */
    ArrayList<RowDataUser> selectMsgId(int user_id) {
        ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
        try {
            mSelectMsgId.setInt(1, user_id);
            ResultSet rs = mSelectMsgId.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth")));
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
     * Query database for all comments from a specific user, using a JOIN sql statement
     * 
     * @param user_id The id of the message
     * 
     * @return The data for row that match as an ArrayList
     */
    ArrayList<RowDataUser> selectComId(int user_id) {
        ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
        try {
            mSelectComId.setInt(1, user_id);
            ResultSet rs = mSelectComId.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth")));
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
     * Query database for all upvotes from a specific user, using a JOIN sql statement
     * 
     * @param user_id The id of the message
     * 
     * @return The data for row that match as an ArrayList
     */
    ArrayList<RowDataUser> selectUpVotes(int user_id) {
        ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
        try {
            mSelectUpVotes.setInt(1, user_id);
            ResultSet rs = mSelectUpVotes.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth")));
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
     * Query database for all downvotes from a specific user, using a JOIN sql statement
     * 
     * @param user_id The id of the message
     * 
     * @return The data for row that match as an ArrayList
     */
    ArrayList<RowDataUser> selectDownVotes(int user_id) {
        ArrayList<RowDataUser> res = new ArrayList<RowDataUser>();
        try {
            mSelectDownVotes.setInt(1, user_id);
            ResultSet rs = mSelectDownVotes.executeQuery();
            while (rs.next()) {
                res.add(new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth")));
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
     * Get all data for a specific row, by ID
     * 
     * @param user_id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataUser selectOne(int user_id) {
        RowDataUser res = null;
        try {
            mSelectOne.setInt(1, user_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth"));
                System.out.println("realname: " + rs.getString("realname"));
                System.out.println("email: " + rs.getString("email"));
                System.out.println("salt: " + rs.getBytes("salt"));
                System.out.println("password: " + rs.getBytes("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all data for a specific row, by username
     * 
     * @param username The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataUser selectUsername(String username) {
        RowDataUser res = null;
        try {
            mSelectUsername.setString(1, username);
            ResultSet rs = mSelectUsername.executeQuery();
            if (rs.next()) {
                res = new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get all data for a specific row, by username
     * 
     * @param username The username of the row being requested
     * @param password The password of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    boolean selectUserPass(String username, byte[] password) {
        RowDataUser res = null;
        try {
            mSelectUserPass.setString(1, username);
            mSelectUserPass.setBytes(2, password);
            ResultSet rs = mSelectUserPass.executeQuery();
            if (rs.next()) {
                res = new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (res != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get authorization value for a specific row by username (0 if not, 1 if valid)
     * 
     * @param username The username of the row
     * 
     * @return The value of the authentication
     */
    int selectAuth(String username) {
        int auth = 0;
        RowDataUser res = null;
        try {
            mSelectAuth.setString(1, username);
            ResultSet rs = mSelectAuth.executeQuery();
            if (rs.next()) {
                res = new RowDataUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"), rs.getString("email"), rs.getBytes("salt"), rs.getBytes("password"), rs.getInt("auth"));
                auth += rs.getInt("auth");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auth;
    }

    /**
     * Delete a row by ID
     * 
     * @param user_id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int user_id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, user_id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}