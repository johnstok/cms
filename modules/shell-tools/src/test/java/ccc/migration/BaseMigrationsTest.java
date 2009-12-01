/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;


/**
 * Tests for the {@link BaseMigrations} class.
 *
 * @author Civic Computing Ltd.
 */
public class BaseMigrationsTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testname() {

        // ARRANGE
        final BaseMigrations bm =
            new BaseMigrations(null, null, null, null, null, null);
        final Map<String, StringBuffer> dups =
            new HashMap<String, StringBuffer>();

        dups.put("Header", null);
        dups.put("HEADER", null);
        dups.put("foo", null);
        dups.put("Foo", null);

        // ACT
        try {
            bm.checkDuplicateKeys(dups);

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals("Duplicate paragraphs found.", e.getMessage());
        }
    }
}
