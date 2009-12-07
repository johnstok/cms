/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration.ccc6.handlers;

import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import junit.framework.TestCase;

import org.h2.tools.Csv;


/**
 * Tests for the {@link ShowInMainMenuSelector} class.
 *
 * @author Civic Computing Ltd.
 */
public class ShowInMainMenuSelectorTest
    extends
        TestCase {

    /**
     * Test.
     * @throws IOException If reading CSV fails.
     * @throws SQLException From JDBC API.
     */
    public void testHandle() throws SQLException, IOException {

        // ARRANGE
        final Csv csv = Csv.getInstance();
        final ResultSet rs =
            csv.read(
                new StringReader("1\n2"),
                new String[]{"CONTENT_ID"});

        // ACT
        final Set<Integer> result = new ShowInMainMenuSelector().handle(rs);

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.contains(Integer.valueOf(1)));
    }
}
