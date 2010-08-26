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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.FailureCode;
import ccc.api.types.SortOrder;


/**
 * Tests for the action scheduler.
 *
 * @author Civic Computing Ltd.
 */
public class ActionAcceptanceTest
    extends
        AbstractAcceptanceTest {

    private static final int ONE_DAY = 24*60*60*1000;
    private static final int ONE_SECOND = 1000;


    /**
     * Test.
     */
    public void testCreateAction() {

        // ARRANGE
        getActions().executeAll();
        final ResourceSummary rs = tempFolder();

        // ACT
        getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                new Date(ONE_DAY),
                new HashMap<String, String>()));
        final PagedCollection<ActionSummary> pending =
            getActions().listActions(null,
                null,
                null,
                ActionStatus.SCHEDULED,
                null,
                null,
                "",
                SortOrder.ASC,
                1,
                20);

        // ASSERT
        assertEquals(1, pending.getTotalCount());
        final ActionSummary actual = pending.getElements().iterator().next();
        assertEquals(rs.getAbsolutePath(), actual.getSubjectPath());
    }


    /**
     * Test.
     */
    public void testCancelAction() {

        // ARRANGE
        final ResourceSummary rs = tempFolder();
        final ActionSummary a = getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                new Date(ONE_DAY),
                new HashMap<String, String>()));

        // ACT
        getActions().cancel(a.getId());

        // ASSERT
        final ActionSummary cancelled = getActions().retrieve(a.getId());
        assertEquals(ActionStatus.CANCELLED, cancelled.getStatus());
    }


    /**
     * Test.
     */
    public void testExecuteAction() {
        // ARRANGE
        final Date time =  new Date(new Date().getTime()-ONE_SECOND);
        final ResourceSummary rs = tempFolder();
        getCommands().lock(rs.getId());
        final ActionSummary a = getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                time,
                new HashMap<String, String>()));

        // ACT
        getActions().executeAll();

        // ASSERT
        final ActionSummary completed = getActions().retrieve(a.getId());
        assertEquals(ActionStatus.COMPLETE, completed.getStatus());
    }


    /**
     * Test.
     */
    public void testFailingAction() {
        // ARRANGE
        final Date epoch =  new Date(0);
        final ResourceSummary rs = tempFolder();
        final ActionSummary a = getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                epoch,
                new HashMap<String, String>()));

        // ACT
        getActions().executeAll();

        // ASSERT
        final ActionSummary failed = getActions().retrieve(a.getId());
        assertEquals(ActionStatus.FAILED, failed.getStatus());
    }


    /**
     * Test.
     */
    public void testExecuteDeleteAction() {

        // ARRANGE
        final Date time =  new Date(0);
        final ResourceSummary rs = tempFolder();
        getCommands().lock(rs.getId());
        final ActionSummary a = getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_DELETE,
                time,
                new HashMap<String, String>()));

        // ACT
        getActions().executeAll();

        // ASSERT
        final ActionSummary completed = getActions().retrieve(a.getId());
        assertEquals(ActionStatus.COMPLETE, completed.getStatus());
    }


    /**
     * Test.
     */
    public void testStartStopActionScheduler() {

        // ARRANGE

        // ACT
        final boolean startedAtFirst = getActions().isRunning();

        getActions().start();
        final boolean startedAfterStart = getActions().isRunning();

        getActions().stop();
        final boolean startedAfterStop = getActions().isRunning();

        // ASSERT
        assertFalse(startedAtFirst);
        assertTrue(startedAfterStart);
        assertFalse(startedAfterStop);
    }


    /**
     * Test.
     */
    public void testStartSchedulerIsIdempotent() {

        // ARRANGE

        // ACT
        try {
            getActions().start();
            getActions().start();
            getActions().start();
        } finally {
            getActions().stop();
        }

        // ASSERT
    }


    /**
     * Test.
     */
    public void testActionListWithExecuteAfter() {

        // ARRANGE
        getActions().executeAll();
        final ResourceSummary rs = tempFolder();
        final Calendar cal = Calendar.getInstance();
        cal.set(2010, 1, 2, 1, 2);
        final Calendar calEarly = Calendar.getInstance();
        calEarly.set(2009, 1, 2, 1, 2);
        final Calendar calLate = Calendar.getInstance();
        calLate.set(2011, 1, 2, 1, 2);

        // ACT
        getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                cal.getTime(),
                new HashMap<String, String>()));
        final PagedCollection<ActionSummary> actionAfter =
            getActions().listActions(null,
                null,
                null,
                ActionStatus.SCHEDULED,
                calEarly.getTime(),
                null,
                "",
                SortOrder.ASC,
                1,
                20);

        final PagedCollection<ActionSummary> actionBefore =
            getActions().listActions(null,
                null,
                null,
                ActionStatus.SCHEDULED,
                calLate.getTime(),
                null,
                "",
                SortOrder.ASC,
                1,
                20);

        final PagedCollection<ActionSummary> actionSame =
            getActions().listActions(null,
                null,
                null,
                ActionStatus.SCHEDULED,
                cal.getTime(),
                null,
                "",
                SortOrder.ASC,
                1,
                20);

        // ASSERT
        assertEquals(0, actionSame.getTotalCount());

        assertEquals(0, actionBefore.getTotalCount());

        assertEquals(1, actionAfter.getTotalCount());
        final ActionSummary actual =
            actionAfter.getElements().iterator().next();
        assertEquals(rs.getAbsolutePath(), actual.getSubjectPath());
    }


    /**
     * Test.
     */
    public void testActionListWithCommandTypeAction() {

        // ARRANGE
        getActions().executeAll();
        final ResourceSummary rs = tempFolder();

        // ACT
        getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_INCLUDE_IN_MM,
                new Date(ONE_DAY),
                new HashMap<String, String>()));
        final PagedCollection<ActionSummary> pending =
            getActions().listActions(null,
                CommandType.RESOURCE_INCLUDE_IN_MM,
                null,
                ActionStatus.SCHEDULED,
                null,
                null,
                "",
                SortOrder.ASC,
                1,
                20);

        // ASSERT
        assertEquals(1, pending.getTotalCount());
        final ActionSummary actual = pending.getElements().iterator().next();
        assertEquals(rs.getAbsolutePath(), actual.getSubjectPath());
        assertEquals(CommandType.RESOURCE_INCLUDE_IN_MM, actual.getType());
    }

    /**
     * Test.
     */
    public void testActionListFailingAction() {
        // ARRANGE
        final Date epoch =  new Date(0);
        final ResourceSummary rs = tempFolder();
        final ActionSummary a = getActions().create(
            new Action(
                rs.getId(),
                CommandType.RESOURCE_PUBLISH,
                epoch,
                new HashMap<String, String>()));
        getActions().executeAll();

        // ACT
        final PagedCollection<ActionSummary> failed =
            getActions().listActions(null,
                CommandType.RESOURCE_PUBLISH,
                FailureCode.UNLOCKED,
                ActionStatus.FAILED,
                null,
                rs.getId(),
                "",
                SortOrder.ASC,
                1,
                20);

        // ASSERT
        assertEquals(1, failed.getTotalCount());
        final ActionSummary actual = failed.getElements().iterator().next();
        assertEquals(ActionStatus.FAILED, actual.getStatus());
    }
}
