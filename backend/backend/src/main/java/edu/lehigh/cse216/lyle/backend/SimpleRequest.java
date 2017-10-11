package edu.lehigh.cse216.lyle.backend;

/**
 * SimpleRequest provides a format for clients to present title and message 
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class SimpleRequest {
    /**
     * The title being provided by the client.
     */
    public String mTitle;

    /**
     * The message being provided by the client.
     */
    public String mBody;

    /**
     * The comment provided by the client.
     */
    public String mCom;

    /**
     * The message attached to the comment.
     */
    public int mId;

    /**
     * The username provided by the client.
     */
    public String username;

    /**
     * The real name provided by the client.
     */
    public String realname;

    /**
     * The email provided by the client.
     */
    public String email;

    /**
     * just for testing
     */
    //public int uId;
}
