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

import ccc.api.AliasDelta;
import ccc.api.ResourceSummary;
import ccc.api.rest.AliasNew;
import ccc.domain.CommandFailedException;


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
        _commands.updateAlias(rs.getId(), new AliasDelta(null, folder.getId()));

        // ASSERT
        final AliasDelta ad = _queries.aliasDelta(rs.getId());
        assertEquals(ad.getTargetId(), folder.getId());
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateAlias() throws CommandFailedException {

        final String name = UUID.randomUUID().toString();
        final ResourceSummary folder = tempFolder();
        final AliasNew alias =
            new AliasNew(folder.getId(), name, folder.getId());

        // ACT
        final ResourceSummary rs = _commands.createAlias(alias);

        // ASSERT
        final AliasDelta ad = _queries.aliasDelta(rs.getId());
        assertEquals(name, rs.getName());
        assertEquals(folder.getId(), rs.getParentId());
        assertEquals(ad.getTargetId(), folder.getId());
    }
}
