

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
    String mText;

    /**
     * Construct a Datum by setting its index and text
     *
     * @param idx The index of this piece of data
     * @param txt The string contents for this piece of data
     */
    Datum(int idx, String txt) {
        mIndex = idx;
        mText = txt;
    }
}