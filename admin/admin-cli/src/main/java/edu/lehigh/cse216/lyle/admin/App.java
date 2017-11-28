package edu.lehigh.cse216.lyle.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

import java.lang.*;


/**
 * App is our basic admin app.  For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println();
        System.out.println("  [?] Help");
        System.out.println("  [q] Quit");
        System.out.println();
        System.out.println("  [V] View all messages");
        System.out.println("  [R] View all users");
        System.out.println("  [O] View all docs");
        System.out.println();
        System.out.println("  [U] Create tblUser");
        System.out.println("  [M] Create tblMessage");
        System.out.println("  [C] Create tblComment");
        System.out.println("  [P] Create tblUpVotes");
        System.out.println("  [N] Create tblDownVotes");
        System.out.println("  [B] Create tblBlockedUsers");
//        System.out.println("  [W] Create tblDocs");
        System.out.println();
        System.out.println("  [u] Delete tblUser");
        System.out.println("  [m] Delete tblMessage");
        System.out.println("  [c] Delete tblComment");
        System.out.println("  [p] Delete tblUpVotes");
        System.out.println("  [n] Delete tblDownVotes");
        System.out.println("  [b] Delete tblBlockedUsers");
//        System.out.println("  [w] Delete tblDocs");
        System.out.println();
        System.out.println("  [-] Delete a user");
        System.out.println("  [x] Delete message");
        System.out.println("  [t] Delete comment");
        System.out.println("  [o] Delete a document");
        System.out.println("  [S] Delete Column from a Table");
        System.out.println();
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     *
     * @param in A BufferedReader, for reading from the keyboard
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q?UMCPVoORNABbuxmcpntES";

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     *
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     *
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     *
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void main(String[] argv) throws IOException {
        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;


        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        menu();
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            char action = prompt(in);


            switch (action) {
                case '?':
                    menu();
                    break;
                case 'q':
                    db.disconnect();
                    System.out.println("DISCONNECTED");
                    System.exit(1);
                    break;
                case 'V':
                    db.viewMessages();
                    break;
                case 'R':
                    db.viewUsers();
                    break;
                case 'O':
                    db.viewDocs();
                    break;
                case 'T':
                    db.createTable();
                    break;
                case 'D':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    String ans = getString(in, "");
                    if (ans.equals("Y"))
                        db.dropTable();
                    else {
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case '1':
                    int id = getInt(in, "Enter the row ID");
                    if (id == -1)
                        continue;
                    RowData res2 = db.selectOne(id);
                    if (res2 != null) {
                        System.out.println("  [" + res2.mId + "] " + res2.title);
                        System.out.println("  --> " + res2.body);
                    }
                    break;
                case '*':
                    ArrayList<RowData> res1 = db.selectAll();
                    if (res1 == null)
                        continue;
                    System.out.println("  Current Database Contents");
                    System.out.println("  -------------------------");
                    for (RowData rd : res1) {
                        System.out.println("  [" + rd.mId + "] " + rd.title);
                    }
                    break;
                case '-':
                    System.out.println();
                    db.viewUsers();
                    System.out.print("\nEnter the user ID: ");
                    System.out.println();
                    id = getInt(in, "");
                    if (id == -1)
                        continue;
                    int res = db.deleteUser(id);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows deleted");
                    break;
                case '+':
                    String subject = getString(in, "Enter the subject");
                    String message = getString(in, "Enter the message");
                    if (subject.equals("") || message.equals(""))
                        continue;
                    res = db.insertRow(subject, message);
                    System.out.println(res + " rows added");
                    break;
                case '~':
                    id = getInt(in, "Enter the row ID :> ");
                    if (id == -1)
                        continue;
                    String newMessage = getString(in, "Enter the new message");
                    res = db.updateOne(id, newMessage);
                    if (res == -1)
                        continue;
                    System.out.println("  " + res + " rows updated");
                    break;
                case 'U':
                    db.createUserTable();
                    break;
                case 'M':
                    db.createMessageTable();
                    break;
                case 'C':
                    db.createCommentTable();
                    break;
                case 'P':
                    db.createUpvoteTable();
                    break;
                case 'N':
                    db.createDownvoteTable();
                    break;
                case 'B':
                    db.createBlockedUserTable();
                    break;
                case 'u':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if (ans.equals("Y"))
                        db.dropUTable();
                    else {
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'm':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if (ans.equals("Y"))
                        db.dropMTable();
                    else {
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'c':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if (ans.equals("Y"))
                        db.dropCTable();
                    else {
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'p':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if (ans.equals("Y"))
                        db.dropUVTable();
                    else {
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'n':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if (ans.equals("Y"))
                        db.dropDVTable();
                    else {
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'b':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if (ans.equals("Y"))
                        db.dropBUTable();
                    else {
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'A':
                    ArrayList<User> resU = db.selectUnauth();
                    System.out.println();
                    System.out.printf("%s\t%-15s\t%-15s\t%-15s\n", "id", "name", "username", "email");
                    System.out.println("-----------------------------------------------");
                    for (int i = 0; i < resU.size(); i++) {
                        System.out.printf("%d\t%-15s\t%-15s\t%-15s\n", resU.get(i).getId(), resU.get(i).getName(),
                                resU.get(i).getUsername(), resU.get(i).getEmail());
                    }
                    break;
                case 'S':
                    System.out.println();
                    System.out.println("Enter Table Name: ");
                    String tblName = getString(in, "");
                    System.out.println("Enter Column Name: ");
                    String columnName = getString(in, "");
                    db.dropColumn(tblName, columnName);
                    break;
//                case 'W':
//                    db.createDocsTable();
//                    break;
//                case 'w':
//                    System.out.println("\nAre you sure you want to drop a table? Y/N");
//                    ans = getString(in, "");
//                    if(ans.equals("Y"))
//                        db.dropDocsTable();
//                    else{
//                        System.out.println("\nTable not deleted");
//                    }
//                    break;
                case 'x':
                    System.out.println();
                    db.viewMessages();
                    System.out.println("\nEnter Message Id: ");
                    int mid = getInt(in, "");
                    db.deleteMessage(mid);
                    break;
                case 't':
                    System.out.println();
                    db.viewComments();
                    System.out.println("\nEnter Comment Id: ");
                    mid = getInt(in, "");
                    db.deleteComment(mid);
                    break;
                case 'o':
                    db.viewDocs();
                    System.out.println("\nEnter Doc Id: ");
                    String docId = getString(in, "");
                    db.deleteDoc(docId);
                    break;
                default:
                    menu();
                    break;
            }

        }
//        db.disconnect();
        // Always remember to disconnect from the database when the program
        // exits

    }
}
