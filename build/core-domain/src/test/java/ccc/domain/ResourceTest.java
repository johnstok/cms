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
 * TODO: Test titles > 256 are disallowed.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceTest extends TestCase {

    /**
     * Test.
     */
    public void testToJson() {

        // ARRANGE
        final Resource resource = new DummyResource(new ResourceName("foo"));

        // ACT
        final String json = resource.toJSON();

        // ASSERT
        assertEquals("{}", json);
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsNullUrl() {

        // ACT
        try {
            new DummyResource((ResourceName) null);
            fail("Resources should reject NULL for the url parameter.");

         // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testResourceConstructorRejectsEmptyStringForName() {

        // ACT
        try {
            new DummyResource(new ResourceName("foo"), "");
            fail("Resources should reject the ZLS for the url parameter.");

         // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.", e.getMessage());
        }
    }


    /**
     * Dummy resource for testing only.
     *
     * @author Civic Computing Ltd
     */
    private final class DummyResource extends Resource {

        /** serialVersionUID : long. */
        private static final long serialVersionUID = 1264881241929108542L;

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

        @Override
        public String toJSON() { return "{}"; }
    }

}
