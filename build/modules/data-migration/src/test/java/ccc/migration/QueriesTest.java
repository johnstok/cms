package ccc.migration;

import junit.framework.TestCase;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class QueriesTest extends TestCase {

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /** {@inheritDoc} */
    @Override
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
            new LegacyDBQueries(null);
            fail("Queries failed to reject a NULL connection.");
        } catch (final IllegalArgumentException e) {

            //ASSERT
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
