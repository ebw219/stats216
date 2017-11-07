

/**
 * Created by lizzieshaffran on 9/25/17.
 */

package lyle.cse216.lehigh.edu.tutorialforlyle;

class Titles {
    /**
     * The string contents that comprise this piece of data
     */
    String mTitle;

    /**
     * Construct a Datum by setting its index and text
     *
     * @param title The string contents for this piece of data
     */
    Titles(String title) {
        mTitle = title;
    }

    public String toString(){
        return mTitle;
    }
}