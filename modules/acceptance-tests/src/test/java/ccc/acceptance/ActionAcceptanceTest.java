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

import java.util.Collection;
import java.util.HashMap;

import ccc.domain.CommandFailedException;
import ccc.rest.ActionNew;
import ccc.rest.ActionSummary;
import ccc.rest.ResourceSummary;
import ccc.types.CommandType;


/**
 * Tests for the action scheduler.
 *
 * @author Civic Computing Ltd.
 */
public class ActionAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateAction() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary rs = tempFolder();

        // ACT
        _commands.createAction(
            new ActionNew(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                Long.MAX_VALUE,
                new HashMap<String, String>()));
        final Collection<ActionSummary> pending = _queries.listPendingActions();

        // ASSERT
        assertEquals(1, pending.size());
        final ActionSummary actual = pending.iterator().next();
        assertEquals(rs.getAbsolutePath(), actual.getSubjectPath());
    }

    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testCancelAction() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary rs = tempFolder();
        _commands.createAction(
            new ActionNew(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                Long.MAX_VALUE,
                new HashMap<String, String>()));

        // ACT
        for (final ActionSummary as : _queries.listPendingActions()) {
            _commands.cancelAction(as.getId());
        }

        // ASSERT
        assertEquals(0, _queries.listPendingActions().size());
    }
}
