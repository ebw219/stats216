package edu.lehigh.cse216.lyle.admin;

import edu.lehigh.cse216.lyle.admin.Database.RowData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

public class Database {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private static Database db; //Made db a global variable
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
    private PreparedStatement mDeleteUser;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    private PreparedStatement mCreateUserTable;

    private PreparedStatement mCreateMessageTable;

    private PreparedStatement mCreateCommentTable;

    private PreparedStatement mCreateUpvoteTable;

    private PreparedStatement mCreateDownvoteTable;

    private PreparedStatement mSelectUnauthenticated;
    
    private PreparedStatement mDropUserTable;
    private PreparedStatement mDropMessageTable;
    private PreparedStatement mDropCommentTable;
    private PreparedStatement mDropUpVoteTable;
    private PreparedStatement mDropDownVoteTable;

    private PreparedStatement mGetEmail;

    private PreparedStatement mUpdateAuth;

    //private PreparedStatement mDropColumn;

    //private PreparedStatement maddColumnToUsers;
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
    public static class RowData {
        /**
         * The ID of this row of the database
         */
        int mId;
        /**
         * The subject stored in this row
         */
        String mSubject;
        /**
         * The message stored in this row
         */
        String mMessage;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int id, String subject, String message) {
            mId = id;
            mSubject = subject;
            mMessage = message;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {
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
    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        if (db == null) {
            db = new Database();
        } else {
            return db;
        }

        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-225-237-64.compute-1.amazonaws.com:5432/d9m9llpg6sa3t3?user=dosnlfxouuassv&password=62f6ee7278c7dba70ef3cbc252324cb643b4b326094319a7130fed897b84d0d1&sslmode=require");
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateTable = db.mConnection.prepareStatement(
                    "CREATE TABLE tblData (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
                    + "NOT NULL, message VARCHAR(500) NOT NULL)");
            //db.maddColumnToUsers = db.mConnection.prepareStatement("ALTER TABLE tblUser ADD COLUMN ? ?"); //(column name then column type) column type = integer or varchar(n) where n is string length
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE thebuzztable");
            db.mDropUserTable = db.mConnection.prepareStatement("DROP TABLE tblUser");
            db.mDropMessageTable = db.mConnection.prepareStatement("DROP TABLE tblMessage");
            db.mDropCommentTable = db.mConnection.prepareStatement("DROP TABLE tblComments");
            db.mDropUpVoteTable = db.mConnection.prepareStatement("DROP TABLE tblUpVotes");
            db.mDropDownVoteTable = db.mConnection.prepareStatement("DROP TABLE tblDownVotes");
            db.mGetEmail = db.mConnection.prepareStatement("SELECT email FROM tblUser WHERE id = ?");
            db.mCreateUserTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblUser (user_id SERIAL "
                + "PRIMARY KEY, username VARCHAR(255), realname VARCHAR(255), "
                + "email VARCHAR(255), "
                + "salt BYTEA, "
                + "password BYTEA, "
                + "auth INTEGER)"); // 0 = unauthorized account, 1 = authorized
            db.mCreateMessageTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblMessage ("
                + "message_id SERIAL PRIMARY KEY, "
                + "user_id INTEGER, title VARCHAR(50), "
                + "body VARCHAR(140), "
                + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id))");
            db.mCreateCommentTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblComments ("
                + "comment_id SERIAL PRIMARY KEY, "
                + "user_id INTEGER, "
                + "message_id INTEGER, "
                + "comment_text VARCHAR(255), "
                + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id), "
                + "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id))");
            db.mCreateDownvoteTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblDownVotes ("
                + "user_id INTEGER,"
                + "message_id INTEGER, "
                + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id), "
                + "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id), "
                + "PRIMARY KEY (user_id, message_id))");
            db.mCreateUpvoteTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblUpVotes ("
                + "user_id INTEGER, "
                + "message_id INTEGER,"
                + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id), "
                + "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id), "
                + "PRIMARY KEY (user_id, message_id))");
            // Standard CRUD operations
            db.mDeleteUser = db.mConnection.prepareStatement("DELETE FROM tblUser WHERE user_id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT id, subject FROM tblData");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ? WHERE id = ?");
            db.mSelectUnauthenticated = db.mConnection.prepareStatement("SELECT * from tblUser WHERE auth = 0"); //unsure if = or ==
            db.mUpdateAuth = db.mConnection.prepareStatement("UPDATE tblUser SET auth = 1 WHERE email = ?");
            //db.mDropColumn= db.mConnection.prepareStatement();

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
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
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String subject, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), null));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteUser(int id) {
        int res = -1;
        try {
            mDeleteUser.setInt(1, id);
            res = mDeleteUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOne(int id, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    

    ArrayList<User> selectUnauth(){
        ArrayList<User> res = new ArrayList<User>();
        try {
            ResultSet rs = mSelectUnauthenticated.executeQuery();
            while (rs.next()) {
                res.add(new User(rs.getInt(1), rs.getString(3), rs.getString(2), rs.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create tblData.  If it already exists, this will print an error
     */
    void createTable() {
        try {
            mCreateTable.execute();
	    System.out.println("successfully created table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createUserTable() {
        try {
            mCreateUserTable.execute();
	        System.out.println("successfully created table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createMessageTable() {
        try {
            mCreateMessageTable.execute();
            System.out.println("successfully created table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createCommentTable() {
        try {
            mCreateCommentTable.execute();
            System.out.println("successfully created table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createUpvoteTable() {
        try {
            mCreateUpvoteTable.execute();
            System.out.println("successfully created table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createDownvoteTable() {
        try {
            mCreateDownvoteTable.execute();
            System.out.println("successfully created table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropUTable() {
        try {
            mDropUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropMTable() {
        try {
            mDropMessageTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropCTable() {
        try {
            mDropCommentTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropUVTable() {
        try {
            mDropUpVoteTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropDVTable() {
        try {
            mDropDownVoteTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String getEmail(int userId) {
        String email = null;
        try {
            mGetEmail.setInt(1, userId);
            ResultSet rs = mGetEmail.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

    void updateAuth(String email) {
        // int res = -1;
        try {
            mUpdateAuth.setString(1, email);
            mUpdateAuth.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // return res;
    }
    //db.mDropColumn= db.mConnection.prepareStatement("ALTER TABLE ? DROP COLUMN ?");
    
    void DropColumn(String tblName, String columnName){
        String sql = "ALTER TABLE " + tblName + " DROP COLUMN " + columnName + ";";
        
        try{
            PreparedStatement dropColumn = db.mConnection.prepareStatement(sql);
            dropColumn.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace(); 
        }
    }

    //db.maddColumnToUsers = db.mConnection.prepareStatement("ALTER TABLE table_name ADD COLUMN column_name column_type"); //column type = integer or varchar(n) where n is string length
   /* void addColumnToUsers(){
        int count=0;
        try{
            maddColumntoUsers
        } catch(SQLException e){
            e.printStackTrace(); 
        }
    }*/
}