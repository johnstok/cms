/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
import ccc.api.ServiceLocator;
import ccc.commons.Testing;
import ccc.rest.extensions.ResourcesExt;
import ccc.services.Migration;


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
            new BaseMigrations(
                Testing.dummy(ServiceLocator.class),
                Testing.dummy(Migration.class),
                Testing.dummy(ResourcesExt.class),
                new LegacyDBQueries(Testing.dummy(DB.class)),
                new TemplateMigration(null, null),
                "/");
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
