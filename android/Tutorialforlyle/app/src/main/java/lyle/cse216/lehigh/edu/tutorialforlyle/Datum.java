

/**
 * Created by lizzieshaffran on 9/25/17.
 */

package lyle.cse216.lehigh.edu.tutorialforlyle;

class Datum {
    /**
     * The string contents that comprise this piece of data
     */
    String mTitle;

    String mMessage;

    private int mVotes;
    int user_id;
    int message_id;


    /**
     * Construct a Datum by setting its index and text
     *
     * @param uId The user id of the message
     * @param title The string contents for this piece of data
     * @param message The message of the data
     */
    Datum(int uId, int mId, String title, String message, int votes) {
        user_id = uId;
        message_id = mId;
        mTitle = title;
        mMessage = message;
        mVotes = votes;
    }


    void setmVotes(int i){
        mVotes = i;
    }

    int getmVotes(){
        return mVotes;
    }

}