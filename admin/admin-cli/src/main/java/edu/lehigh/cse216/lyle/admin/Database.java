package edu.lehigh.cse216.lyle.admin;

//import edu.lehigh.cse216.lyle.admin.Database.RowData;

//import com.sun.xml.internal.bind.v2.model.annotation.Quick;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
     * A prepared statement for getting all flagged rows from the database
     */
    private PreparedStatement mSelectFlagMsg;
    private PreparedStatement mSelectFlagCom;

    /**
     * A prepared statement for getting all blocked users from the database
     */
    private PreparedStatement mSelectBlockedUsers;

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

    private PreparedStatement mCreateBlockedUserTable;

    private PreparedStatement mSelectUnauthenticated;

    private PreparedStatement mSelectAllUsers;

    private PreparedStatement mSelectAllDocs;

    private PreparedStatement mDropUserTable;
    private PreparedStatement mDropMessageTable;
    private PreparedStatement mDropCommentTable;
    private PreparedStatement mDropUpVoteTable;
    private PreparedStatement mDropDownVoteTable;
    private PreparedStatement mDropDocsTable;
    private PreparedStatement mDropBlockedUserTable;

    private PreparedStatement mGetEmail;

    private PreparedStatement mUpdateAuth;

    private PreparedStatement mCreateDocsTable;

    private PreparedStatement mDeleteUpVote;

    private PreparedStatement mDeleteDownVote;

    private PreparedStatement mDeleteMessage;

    private PreparedStatement mDeleteComment;

    private PreparedStatement mDeleteFlagMsg;

    private PreparedStatement mDeleteFlagCom;

    private PreparedStatement mSelectComments;


    //private PreparedStatement mDropColumn;

    //private PreparedStatement maddColumnToUsers;

    /**
     * The Database constructor is private: we only create Database objects
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     *
     * @param db_url
     * @return
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
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE thebuzztable CASCADE");
            db.mDropUserTable = db.mConnection.prepareStatement("DROP TABLE tblUser CASCADE");
            db.mDropMessageTable = db.mConnection.prepareStatement("DROP TABLE tblMessage CASCADE");
            db.mDropCommentTable = db.mConnection.prepareStatement("DROP TABLE tblComments CASCADE");
            db.mDropUpVoteTable = db.mConnection.prepareStatement("DROP TABLE tblUpVotes CASCADE");
            db.mDropDownVoteTable = db.mConnection.prepareStatement("DROP TABLE tblDownVotes CASCADE");
            db.mDropBlockedUserTable = db.mConnection.prepareStatement("DROP TABLE tblBlockedUsers CASCADE");
//            db.mDropDocsTable = db.mConnection.prepareStatement("DROP TABLE tblDocs CASCADE");
            db.mGetEmail = db.mConnection.prepareStatement("SELECT email FROM tblUser WHERE id = ?");

            db.mCreateUserTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblUser (user_id SERIAL "
                    + "PRIMARY KEY , "
                    + "username VARCHAR(255) NOT NULL UNIQUE, "
                    + "realname VARCHAR(255) NOT NULL, "
                    + "email VARCHAR(255) NOT NULL UNIQUE, "
                    + "auth INTEGER)");
            db.mCreateMessageTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblMessage ("
                    + "message_id SERIAL PRIMARY KEY, "
                    + "user_id INTEGER, "
                    + "title VARCHAR(50) NOT NULL, "
                    + "body VARCHAR(140) NOT NULL, "
                    + "date_created DATE DEFAULT now(),"
                    + "pdf VARCHAR(75),"
                    + "link VARCHAR(140),"
                    + "image VARCHAR(50),"
                    + "flag INTEGER DEFAULT 0, "
                    + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE)");
            db.mCreateCommentTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblComments ("
                    + "comment_id SERIAL PRIMARY KEY, "
                    + "user_id INTEGER, "
                    + "message_id INTEGER, "
                    + "comment_text VARCHAR(255) NOT NULL, "
                    + "date_created DATE DEFAULT now(),"
                    + "flag INTEGER DEFAULT 0, "
                    + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id) ON DELETE CASCADE)");
            db.mCreateDownvoteTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblDownVotes ("
                    + "user_id INTEGER,"
                    + "message_id INTEGER, "
                    + "date_created DATE DEFAULT now(),"
                    + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id) ON DELETE CASCADE, "
                    + "PRIMARY KEY (user_id, message_id))");
            db.mCreateUpvoteTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblUpVotes ("
                    + "user_id INTEGER, "
                    + "message_id INTEGER,"
                    + "date_created DATE DEFAULT now(),"
                    + "FOREIGN KEY (user_id) REFERENCES tblUser (user_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (message_id) REFERENCES tblMessage (message_id) ON DELETE CASCADE, "
                    + "PRIMARY KEY (user_id, message_id))");
            db.mCreateBlockedUserTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblBlockedUsers ("
                    + "user_id1 INTEGER, "
                    + "user_id2 INTEGER, "
                    + "FOREIGN KEY (user_id1) REFERENCES tblUser (user_id) ON DELETE CASCADE, "
                    + "FOREIGN KEY (user_id2) REFERENCES tblUser (user_id) ON DELETE CASCADE, "
                    + "PRIMARY KEY (user_id1, user_id2))");

//            db.mCreateDocsTable = db.mConnection.prepareStatement("CREATE TABLE IF NOT EXISTS tblDocs ("
//                    + "doc_owner_id INTEGER,"
//                    + "doc_id INTEGER PRIMARY KEY,"
//                    + "doc_title VARCHAR(140) NOT NULL,"
//                    + "date_created DATE DEFAULT now(),"
//                    + "FOREIGN KEY (doc_owner_id) REFERENCES tblUser (user_id) ON DELETE CASCADE)");

            // Standard CRUD operations
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO tblData VALUES (default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM tblMessage");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from tblData WHERE id=?");
            db.mSelectFlagMsg = db.mConnection.prepareStatement("SELECT * FROM tblMessage WHERE flag>3");
            db.mSelectFlagCom = db.mConnection.prepareStatement("SELECT * FROM tblComments WHERE flag>3");            
            db.mSelectBlockedUsers = db.mConnection.prepareStatement("SELECT * FROM tblBlockedUsers");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE tblData SET message = ? WHERE id = ?");
            db.mSelectUnauthenticated = db.mConnection.prepareStatement("SELECT * from tblUser WHERE auth = 0"); //unsure if = or ==
            db.mUpdateAuth = db.mConnection.prepareStatement("UPDATE tblUser SET auth = 1 WHERE email = ?");
            db.mSelectAllUsers = db.mConnection.prepareStatement("SELECT * FROM tblUser");
//            db.mSelectAllDocs = db.mConnection.prepareStatement("SELECT * FROM tblDocs");
            //db.mDropColumn= db.mConnection.prepareStatement();

            db.mDeleteUser = db.mConnection.prepareStatement("DELETE FROM tblUser WHERE user_id = ?");
            db.mDeleteMessage = db.mConnection.prepareStatement("DELETE FROM tblMessage WHERE message_id = ?");
            db.mDeleteComment = db.mConnection.prepareStatement("DELETE FROM tblComments WHERE comment_id = ?");
            db.mDeleteUpVote = db.mConnection.prepareStatement("DELETE FROM tblUpVotes WHERE message_id = ?");
            db.mDeleteDownVote = db.mConnection.prepareStatement("DELETE FROM tblDownVotes WHERE message_id = ?");
            db.mDeleteFlagMsg = db.mConnection.prepareStatement("DELETE FROM tblMessage WHERE flag>3");
            db.mDeleteFlagCom = db.mConnection.prepareStatement("DELETE FROM tblComments WHERE flag>3");
            
            db.mSelectComments = db.mConnection.prepareStatement("SELECT * FROM tblComments WHERE message_id = ?");


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
     * <p>
     * NB: The connection will always be null after this call, even if an
     * error occurred during the closing operation.
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
                res.add(new RowData(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"),
                        rs.getString("body")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all subjects and their IDs that have been flagged at least 3 times
     *
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectFlagMsg() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectFlagMsg.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("title"),
                        rs.getString("body")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<Comment> selectFlagCom() {
        ArrayList<Comment> res = new ArrayList<Comment>();
        try {
            ResultSet rs = mSelectFlagCom.executeQuery();
            while (rs.next()) {
                res.add(new Comment(rs.getInt("comment_id"), rs.getInt("user_id"), rs.getInt("message_id"),
                        rs.getString("comment_text")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all blocked users
     *
     * @return All rows, as an ArrayList
     */
    ArrayList<UserBlocked> selectBlockedUsers() {
        ArrayList<UserBlocked> res = new ArrayList<UserBlocked>();
        try {
            ResultSet rs = mSelectBlockedUsers.executeQuery();
            while (rs.next()) {
                res.add(new UserBlocked(rs.getInt("user_id1"), rs.getInt("user_id2")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all subjects and their IDs
     *
     * @return All rows, as an ArrayList
     */
    ArrayList<User> selectAllUsers() {
        ArrayList<User> res = new ArrayList<User>();
        try {
            ResultSet rs = mSelectAllUsers.executeQuery();
            while (rs.next()) {
                res.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("realname"),
                        rs.getString("email")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<Doc> selectAllDocs() {
        ArrayList<Doc> res = new ArrayList<Doc>();
        try {
            ResultSet rs = mSelectAllDocs.executeQuery();
            while (rs.next()) {
                res.add(new Doc(rs.getInt("doc_owner_id"), rs.getInt("doc_id"), rs.getString("doc_title")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    void viewUsers() {
        System.out.println("\n\nUsers: ");
        System.out.println("\nUID\tUsername\tName\tEmail");
        System.out.println("-------------------------------------");
        ArrayList<User> users = db.selectAllUsers();
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }
    }

    void viewDocs() {
        try {
            Quickstart.printFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void viewMessages() {
        System.out.println("\n\nMessages: ");
        System.out.println("\nMID\tUID\tTitle\tBody");
        System.out.println("-----------------------------");
        ArrayList<RowData> messages = db.selectAll();
        for (int i = 0; i < messages.size(); i++) {
            System.out.println(messages.get(i));
        }
    }

    void viewFlaggedMessages() {
        System.out.println("\n\nFlagged Messages: ");
        System.out.println("\nMID\tUID\tTitle\tBody");
        System.out.println("-----------------------------");
        ArrayList<RowData> messages = db.selectFlagMsg();
        for (int i = 0; i < messages.size(); i++) {
            System.out.println(messages.get(i));
        }
    }

    void viewFlaggedComments () {
        System.out.println("\n\nFlagged Comments: ");
        System.out.println("\nCID\tUID\tMID\tBody");
        System.out.println("-----------------------------");
        ArrayList<Comment> comments = db.selectFlagCom();
        for (int i = 0; i < comments.size(); i++) {
            System.out.println(comments.get(i));
        }
    }

    void viewBlockedUsers() {
        System.out.println("\n\nBlocked Users: ");
        System.out.println("\nUser Id 1\tUser Id 2");
        System.out.println("-------------------------------------");
        ArrayList<UserBlocked> users = db.selectBlockedUsers();
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i));
        }
    }

    void viewComments() {
        ArrayList<RowData> messages = db.selectAll();
        for (int i = 0; i < messages.size(); i++) {
            try {
                mSelectComments.setInt(1, messages.get(i).mId);
                ArrayList<Comment> comments = new ArrayList<Comment>();
                ResultSet rs = db.mSelectComments.executeQuery();
                if (rs.next()) {
                    comments.add(new Comment(rs.getInt("comment_id"), rs.getInt("user_id"), rs.getInt("message_id"),
                            rs.getString("comment_text")));
                }
                System.out.println("\n\nMessage: ");
                System.out.println("\nMID\tUID\tTitle\tBody");
                System.out.println("-----------------------------");
                System.out.println(messages.get(i));

                System.out.println("\n\nComments: ");
                System.out.println("\n\tCID\tUID\tMID\tBody");
                System.out.println("\t-----------------------------");
                for (int j = 0; j < comments.size(); j++) {
                    System.out.println("\t" + comments.get(j));
                }
                System.out.println();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//    void viewDocs(){
//        System.out.println("\n\nDocs: ");
//        System.out.println("\nOwner Id\tDoc Id\tTitle");
//        System.out.println("-----------------------------");
//        ArrayList<Doc> docs = db.selectAllDocs();
//        for (int i = 0; i < docs.size(); i++){
//            System.out.println(docs.get(i));
//        }
//    }

    /**
     * Get all data for a specific row, by ID
     *
     * @param id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getInt("uId"), rs.getString("subject"), rs.getString("message"));
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

    int deleteComment(int id) {
        int res = -1;
        try {
            mDeleteComment.setInt(1, id);
            res = mDeleteComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    void deleteFlagMsg() {
        try {
            mDeleteFlagMsg.execute();
            System.out.println("Successfully deleted all flagged messages");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteFlagCom() {
        try {
            mDeleteFlagCom.execute();
            System.out.println("Successfully deleted all flagged comments");
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the message for a row in the database
     *
     * @param id      The id of the row to update
     * @param message The new message contents
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


    ArrayList<User> selectUnauth() {
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

    void createBlockedUserTable() {
        try {
            mCreateBlockedUserTable.execute();
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

    void createDocsTable() {
        try {
            mCreateDocsTable.execute();
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
            System.out.println("successfully deleted table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropUTable() {
        try {
            mDropUserTable.execute();
            System.out.println("successfully deleted table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropMTable() {
        try {
            mDropMessageTable.execute();
            System.out.println("successfully deleted table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropCTable() {
        try {
            mDropCommentTable.execute();
            System.out.println("successfully deleted table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropUVTable() {
        try {
            mDropUpVoteTable.execute();
            System.out.println("successfully deleted table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropDVTable() {
        try {
            mDropDownVoteTable.execute();
            System.out.println("successfully deleted table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropBUTable() {
        try {
            mDropBlockedUserTable.execute();
            System.out.println("successfully deleted table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropDocsTable() {
        try {
            mDropDocsTable.execute();
            System.out.println("successfully deleted table");
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

    void deleteMessage(int message_id) {
        try {
            mDeleteMessage.setInt(1, message_id);
            mDeleteMessage.execute();
            System.out.println("Successfully deleted message");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropColumn(String tblName, String columnName) {
        String sql = "ALTER TABLE " + tblName + " DROP COLUMN " + columnName + ";";

        try {
            PreparedStatement dropColumn = db.mConnection.prepareStatement(sql);
            dropColumn.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteDoc(String id) throws IOException {
        Quickstart.deleteFile(id);
    }

}