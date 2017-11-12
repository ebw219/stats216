package edu.lehigh.cse216.lyle.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

import com.sendgrid.*;
import java.lang.*;
import java.sql.ResultSet;
import edu.lehigh.cse216.lyle.admin.Database.RowData;

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
        System.out.println("  [q] Quit\n");
        System.out.println("  [U] Create tblUser");
        System.out.println("  [M] Create tblMessage");
        System.out.println("  [C] Create tblComment");
        System.out.println("  [P] Create tblUpVotes");
        System.out.println("  [N] Create tblDownVotes");
        System.out.println("  [W] Create tblDocs");
        System.out.println();
        System.out.println("  [u] Delete tblUser");
        System.out.println("  [m] Delete tblMessage");
        System.out.println("  [c] Delete tblComment");
        System.out.println("  [p] Delete tblUpVotes");
        System.out.println("  [n] Delete tblDownVotes");
        System.out.println("  [w] Delete tblDocs");
        System.out.println("  [x] Delete Message");
        System.out.println();
        //System.out.println("  [A] Show unauthenticated users");
        System.out.println("  [-] Delete a user");
        //System.out.println("  [E] Email a user their password and authorize their email");
        System.out.println("  [S] Delete Column from a Table");
        System.out.println();
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     *
     * @param in A BufferedReader, for reading from the keyboard
     *
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q?UMCPNAWwuxmcpnES";

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
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     *
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
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     *
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
     * static void emailUser(String userEmail) {

     Email from = new Email(System.getenv("FROM_EMAIL"));
     Email to = new Email(userEmail);
     Content content = new Content("text/plain", "You are registered");
     String subject = "The Buzz Account Information";
     Mail mail = new Mail(from, subject, to, content);

     //String token = "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";
     String token = System.getenv("SENDGRID_KEY");
     SendGrid sendgrid = new SendGrid(token);
     Request request = new Request();
     try {
     request.setMethod(Method.POST);
     request.setEndpoint("mail/send");
     request.setBody(mail.build());
     Response response = sendgrid.api(request);
     System.out.println(response.getStatusCode());
     System.out.println(response.getHeaders());
     } catch (IOException ex) {
     // throw ex;
     ex.printStackTrace();
     }
     }*/

    /**
     * The main routine runs a loop that gets a request from the user and
     * processes it
     *
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void main(String[] argv) {
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


            switch(action){
                case '?':
                    menu();
                    break;
                case 'q':
                    db.disconnect();
                    break;
                case 'T':
                    db.createTable();
                    break;
                case 'D':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    String ans = getString(in, "");
                    if(ans.equals("Y"))
                        db.dropTable();
                    else{
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case '1':
                    if(true) {
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        Database.RowData res = db.selectOne(id);
                        if (res != null) {
                            System.out.println("  [" + res.mId + "] " + res.mSubject);
                            System.out.println("  --> " + res.mMessage);
                        }
                    }
                    break;
                case'*':
                    if(true) {
                        ArrayList<Database.RowData> res = db.selectAll();
                        if (res == null)
                            continue;
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------");
                        for (Database.RowData rd : res) {
                            System.out.println("  [" + rd.mId + "] " + rd.mSubject);
                        }
                    }
                    break;
                case'-':
                    if(true) {
                        System.out.print("Enter the user ID: ");
                        System.out.println();
                        int id = getInt(in, "");
                        if (id == -1)
                            continue;
                        int res = db.deleteUser(id);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows deleted");
                    }
                    break;
                case '+':
                    if(true) {
                        String subject = getString(in, "Enter the subject");
                        String message = getString(in, "Enter the message");
                        if (subject.equals("") || message.equals(""))
                            continue;
                        int res = db.insertRow(subject, message);
                        System.out.println(res + " rows added");
                    }
                    break;
                case '~':
                    if(true) {
                        int id = getInt(in, "Enter the row ID :> ");
                        if (id == -1)
                            continue;
                        String newMessage = getString(in, "Enter the new message");
                        int res = db.updateOne(id, newMessage);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows updated");
                    }
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
                case'u':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if(ans.equals("Y"))
                        db.dropUTable();
                    else{
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'm':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if(ans.equals("Y"))
                        db.dropMTable();
                    else{
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'c':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if(ans.equals("Y"))
                        db.dropCTable();
                    else{
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'p':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if(ans.equals("Y"))
                        db.dropUVTable();
                    else{
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'n':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if(ans.equals("Y"))
                        db.dropDVTable();
                    else{
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'A':
                    ArrayList<User> res = db.selectUnauth();
                    System.out.println();
                    System.out.printf("%s\t%-15s\t%-15s\t%-15s\n", "id", "name", "username", "email");
                    System.out.println("-----------------------------------------------");
                    for (int i=0; i<res.size(); i++) {
                        System.out.printf("%d\t%-15s\t%-15s\t%-15s\n", res.get(i).getId(), res.get(i).getName(), res.get(i).getUsername(), res.get(i).getEmail());
                    }
                    break;
                case 'S':
                    System.out.println();
                    System.out.println("Enter Table Name: ");
                    String tblName = getString(in, "");
                    System.out.println("Enter Column Name: ");
                    String columnName = getString(in,"");
                    db.DropColumn(tblName, columnName);
                    break;
                case 'W':
                    db.createDocsTable();
                    break;
                case 'w':
                    System.out.println("\nAre you sure you want to drop a table? Y/N");
                    ans = getString(in, "");
                    if(ans.equals("Y"))
                        db.dropDocsTable();
                    else{
                        System.out.println("\nTable not deleted");
                    }
                    break;
                case 'x':
                    System.out.println();
                    System.out.println("Enter Message Id: ");
                    int mid = getInt(in, "");
                    db.deleteMessage(mid);
                    break;
                default:
                    menu();
                    break;
            }

////            if (action == '?') {
////                menu();
////            } else if (action == 'q') {
////                break;
////            } else if (action == 'T') {
////                db.createTable();
////            } else if (action == 'D') {
////                db.dropTable();
////            } else if (action == '1') {
////                int id = getInt(in, "Enter the row ID");
////                if (id == -1)
////                    continue;
////                Database.RowData res = db.selectOne(id);
////                if (res != null) {
////                    System.out.println("  [" + res.mId + "] " + res.mSubject);
////                    System.out.println("  --> " + res.mMessage);
////                }
////            } else if (action == '*') {
////                ArrayList<Database.RowData> res = db.selectAll();
////                if (res == null)
////                    continue;
////                System.out.println("  Current Database Contents");
////                System.out.println("  -------------------------");
////                for (Database.RowData rd : res) {
////                    System.out.println("  [" + rd.mId + "] " + rd.mSubject);
////                }
////            } else if (action == '-') {
////                System.out.print("Enter the user ID: ");
////                System.out.println();
////                int id = getInt(in, "");
////                if (id == -1)
////                    continue;
////                int res = db.deleteUser(id);
////                if (res == -1)
////                    continue;
////                System.out.println("  " + res + " rows deleted");
//            } else if (action == '+') {
//                String subject = getString(in, "Enter the subject");
//                String message = getString(in, "Enter the message");
//                if (subject.equals("") || message.equals(""))
//                    continue;
//                int res = db.insertRow(subject, message);
//                System.out.println(res + " rows added");
//            } else if (action == '~') {
//                int id = getInt(in, "Enter the row ID :> ");
//                if (id == -1)
//                    continue;
//                String newMessage = getString(in, "Enter the new message");
//                int res = db.updateOne(id, newMessage);
//                if (res == -1)
//                    continue;
//                System.out.println("  " + res + " rows updated");
//            } else if (action == 'U') {
//                db.createUserTable();
//            } else if (action == 'M') {
//                db.createMessageTable();
//            } else if (action == 'C') {
//                db.createCommentTable();
//            } else if (action == 'P') {
//                db.createUpvoteTable();
//            } else if (action == 'N') {
//                db.createDownvoteTable();
//            } else if (action == 'u') {
//                db.dropUTable();
//            } else if (action == 'm') {
//                db.dropMTable();
//            } else if (action == 'c') {
//                db.dropCTable();
//            } else if (action == 'p') {
//                db.dropUVTable();
//            } else if (action == 'n') {
//                db.dropDVTable();
//            } else if (action == 'A') {
//
//                ArrayList<User> res = db.selectUnauth();
//                System.out.println();
//                System.out.printf("%s\t%-15s\t%-15s\t%-15s\n", "id", "name", "username", "email");
//                System.out.println("-----------------------------------------------");
//                for (int i=0; i<res.size(); i++) {
//                    System.out.printf("%d\t%-15s\t%-15s\t%-15s\n", res.get(i).getId(), res.get(i).getName(), res.get(i).getUsername(), res.get(i).getEmail());
//                }
//            } else if(action == 'S'){
//                System.out.println();
//                System.out.println("Enter Table Name: ");
//                String tblName = getString(in, "");
//                System.out.println("Enter Column Name: ");
//                String columnName = getString(in,"");
//                db.DropColumn(tblName, columnName);
//            }/*else if (action == 'E') {
//                System.out.print("Enter the user's email: ");
//                System.out.println();
//                String email = getString(in, "");
//                emailUser(email);
//                System.out.println("Email sent to " + email);
//                db.updateAuth(email);
//
//
//            }*/

        }
//        db.disconnect();
        // Always remember to disconnect from the database when the program
        // exits

    }
}
