package edu.lehigh.cse216.lyle.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.lehigh.cse216.lyle.admin.Database.RowData;


public class DatabaseTest
    extends TestCase
{
    /**                                                                            
     * Create the test case                                                        
     *                                                                             
     * @param testName name of the test case                                       
     */
    public DatabaseTest( String testName )
    {
        super( testName );
    }

    /**                                                                            
     * @return the suite of tests being tested                                     
     */
    public static Test suite()
    {
        return new TestSuite( DatabaseTest.class );
    }

    /**                                                                            
     * Rigourous Test :-)                                                          
     */
    public void testConstructor()
    {
        String subject = "Test Subject";
	String message = "Test Message";
	int id = 10;
	RowData d = new RowData(id, subject, message);

	assertTrue(d.mSubject.equals(subject));
	assertTrue(d.mMessage.equals(message));
	assertTrue(d.mId == id);

    }

}