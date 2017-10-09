

/**
 * Created by lizzieshaffran on 9/25/17.
 */

package lyle.cse216.lehigh.edu.tutorialforlyle;

class Datum {
    /**
     * An integer index for this piece of data
     */
    int mIndex;

    /**
     * The string contents that comprise this piece of data
     */
    String mTitle;

    String mMessage;

    int mVotes;
    int user_id;

//    boolean liked = false;
//    boolean disliked = false;

    /**
     * Construct a Datum by setting its index and text
     *
     * @param idx The index of this piece of data
     * @param uID The user id of the message
     * @param title The string contents for this piece of data
     * @param message The message of the data
     * @param votes The net votes
     */
    Datum(int idx, int uID, String title, String message, int votes) {
        mIndex = idx;
        user_id = uID;
        mTitle = title;
        mMessage = message;
        mVotes = votes;
    }

}