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
package ccc.acceptance;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import ccc.rest.RestException;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.ResourceSummary;
import ccc.types.ActionStatus;
import ccc.types.CommandType;


/**
 * Tests for the action scheduler.
 *
 * @author Civic Computing Ltd.
 */
public class ActionAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /** ONE_SECOND : int. */
    private static final int ONE_SECOND = 1000;

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testCreateAction() throws RestException {

        // ARRANGE
        final ResourceSummary rs = tempFolder();

        // ACT
        getActions().createAction(
            new ActionDto(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                new Date(24*60*60*1000),
                new HashMap<String, String>()));
        final Collection<ActionSummary> pending =
            getActions().listPendingActions();

        // ASSERT
        assertEquals(1, pending.size());
        final ActionSummary actual = pending.iterator().next();
        assertEquals(rs.getAbsolutePath(), actual.getSubjectPath());
    }

    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testCancelAction() throws RestException {

        // ARRANGE
        final ResourceSummary rs = tempFolder();
        final ActionSummary a = getActions().createAction(
            new ActionDto(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                new Date(24*60*60*1000),
                new HashMap<String, String>()));

        // ACT
        getActions().cancelAction(a.getId());

        // ASSERT
        final ActionSummary cancelled = getActions().findAction(a.getId());
        assertEquals(ActionStatus.CANCELLED, cancelled.getStatus());
    }

    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testExecuteAction() throws RestException {
        // ARRANGE
        final Date time =  new Date(new Date().getTime()-ONE_SECOND);
        final ResourceSummary rs = tempFolder();
        getCommands().lock(rs.getId());
        final ActionSummary a = getActions().createAction(
            new ActionDto(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                time,
                new HashMap<String, String>()));

        // ACT
        getActions().executeAll();

        // ASSERT
        final ActionSummary completed = getActions().findAction(a.getId());
        assertEquals(ActionStatus.COMPLETE, completed.getStatus());
    }

    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testFailingAction() throws RestException {
        // ARRANGE
        final Date epoch =  new Date(0);
        final ResourceSummary rs = tempFolder();
        final ActionSummary a = getActions().createAction(
            new ActionDto(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                epoch,
                new HashMap<String, String>()));

        // ACT
        getActions().executeAll();

        // ASSERT
        final ActionSummary failed = getActions().findAction(a.getId());
        assertEquals(ActionStatus.FAILED, failed.getStatus());
    }

    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testExecuteDeleteAction() throws RestException {

        // ARRANGE
        final Date time =  new Date(0);
        final ResourceSummary rs = tempFolder();
        getCommands().lock(rs.getId());
        final ActionSummary a = getActions().createAction(
            new ActionDto(
                rs.getId(),
                CommandType.RESOURCE_DELETE,
                time,
                new HashMap<String, String>()));

        // ACT
        getActions().executeAll();

        // ASSERT
        final ActionSummary completed = getActions().findAction(a.getId());
        assertEquals(ActionStatus.COMPLETE, completed.getStatus());
    }
}
