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
 * Tests for the {@link Alias} class.
 *
 * @author Civic Computing Ltd
 */
public class AliasTest extends TestCase {

    /**
     * Test.
     */
    public void testTypeReturnsAlias() {

        // ACT
        final ResourceType t = new Alias().type();

        // ASSERT
        assertEquals(ResourceType.ALIAS, t);
    }

    /**
     * Test.
     */
    public void testCreateAlias() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"));

        // ACT
        final Alias alias = new Alias(new ResourceName("bar"), p);

        // ASSERT
        assertEquals(p, alias.target());
    }

    /**
     * Test.
     */
    public void testNullTargetIsRejected() {

        // ACT
        try {
            new Alias(new ResourceName("foo"), null);
            fail("Null should be rejected");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }

    }
}
