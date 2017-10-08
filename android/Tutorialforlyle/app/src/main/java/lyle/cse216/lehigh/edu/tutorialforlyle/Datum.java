

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

//    boolean liked = false;
//    boolean disliked = false;

    /**
     * Construct a Datum by setting its index and text
     *
     * @param idx The index of this piece of data
     * @param title The string contents for this piece of data
     */
    Datum(int idx, String title, String message, int votes) {
        mIndex = idx;
        mTitle = title;
        mMessage = message;
        mVotes = votes;
    }

}