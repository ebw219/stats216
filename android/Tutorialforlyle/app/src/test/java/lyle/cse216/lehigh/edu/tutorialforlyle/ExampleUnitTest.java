package lyle.cse216.lehigh.edu.tutorialforlyle;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void Datum_constructor_sets_fields() throws Exception {
        Datum d = new Datum(7, "hello world");
        assertEquals(d.mIndex, 8);
        assertEquals(d.mText, "hello world");
    }
}