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
package ccc.acceptance;

import java.util.UUID;

import ccc.rest.CommandFailedException;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.ResourceSummary;


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
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateAlias() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary rs = tempAlias();

        // ACT
        _commands.lock(rs.getId());
        _commands.updateAlias(rs.getId(), new AliasDelta(folder.getId()));

        // ASSERT
        final String targetName = _queries.aliasTargetName(rs.getId());
        assertEquals(targetName, folder.getName());
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateAlias() throws CommandFailedException {

        final String name = UUID.randomUUID().toString();
        final ResourceSummary folder = tempFolder();
        final AliasDto alias =
            new AliasDto(folder.getId(), name, folder.getId());

        // ACT
        final ResourceSummary rs = _commands.createAlias(alias);

        // ASSERT
        final String targetName = _queries.aliasTargetName(rs.getId());
        assertEquals(name, rs.getName());
        assertEquals(folder.getId(), rs.getParentId());
        assertEquals(targetName, folder.getName());
    }
}
