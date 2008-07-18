/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import junit.framework.TestCase;


/**
 * Tests for the {@link Resource} class.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceTest extends TestCase {

    /**
     * Test.
     */
    public void testResourceConstructorRejectsNullUrl() {

        // ACT
        try {
            new DummyResource((ResourceName) null);
            fail("Resources should reject NULL for the url parameter.");

         // ASSERT
        } catch (IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsEmptyStringForName() {

        // ACT
        try {
            new DummyResource(new ResourceName("foo"),"");
            fail("Resources should reject the ZLS for the url parameter.");

         // ASSERT
        } catch (IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.", e.getMessage());
        }
    }


//    /**
//     * Test.
//     */
//    public void testAddMetadata() {
//        fail();
//    }

    /**
     * Dummy resource for testing only.
     *
     * @author Civic Computing Ltd
     */
    private final class DummyResource extends Resource {

        /**
         * Constructor.
         *
         * @param object
         */
        public DummyResource(final ResourceName url) {
            super(url);
        }

        /**
         * Constructor.
         *
         * @param string
         */
        public DummyResource(final ResourceName url, final String name) {
            super(url, name);
        }

        @Override
        public ResourceType type() { return ResourceType.FOLDER; }
    }

}
