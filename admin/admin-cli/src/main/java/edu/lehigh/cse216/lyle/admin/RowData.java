package edu.lehigh.cse216.lyle.admin;

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
public class RowData {
    /**
     * The ID of this row of the database
     */
    int mId;
    int uId;
    /**
     * The subject stored in this row
     */
    String title;
    /**
     * The message stored in this row
     */
    String body;

    /**
     * Construct a RowData object by providing values for its fields
     */
    public RowData(int id, int uid, String subject, String message) {
        mId = id;
        uId = uid;
        title = subject;
        body = message;
    }

    public String toString(){
        return (mId + "\t" + uId + "\t" + title + "\t" + body);
    }
}