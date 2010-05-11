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
package ccc.tests.acceptance;

import java.util.UUID;

import ccc.api.core.Alias;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceName;


/**
 * Tests for the manipulating resources.
 *
 * @author Civic Computing Ltd.
 */
public class AliasAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /**
     * Test.
     */
    public void testUpdateAlias() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary rs = tempAlias();

        // ACT
        getCommands().lock(rs.getId());
        getAliases().update(rs.getId(), new Alias(folder.getId()));

        // ASSERT
        final String targetName = getAliases().aliasTargetName(rs.getId());
        assertEquals(targetName, folder.getName());
    }


    /**
     * Test.
     */
    public void testCreateAlias() {

        final String name = UUID.randomUUID().toString();
        final ResourceSummary folder = tempFolder();
        final Alias alias =
            new Alias(
                folder.getId(), new ResourceName(name), folder.getId());

        // ACT
        final ResourceSummary rs = getAliases().create(alias);

        // ASSERT
        final String targetName = getAliases().aliasTargetName(rs.getId());
        assertEquals(name, rs.getName());
        assertEquals(folder.getId(), rs.getParent());
        assertEquals(targetName, folder.getName());
    }
}
