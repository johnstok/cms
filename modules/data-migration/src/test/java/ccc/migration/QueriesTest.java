package ccc.migration;

import junit.framework.TestCase;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class QueriesTest extends TestCase {

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test.
     *
     */
    public void testConstructRejectsNull() {

        // ACT
        try {
            new Queries(null);
            fail("Queries failed to reject a NULL connection.");
        } catch (IllegalArgumentException e) {

            //ASSERT
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
