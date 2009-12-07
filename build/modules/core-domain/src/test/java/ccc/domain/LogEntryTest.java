/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.domain;

import java.util.Date;

import junit.framework.TestCase;
import ccc.serialization.JsonImpl;
import ccc.types.CommandType;
import ccc.types.Username;


/**
 * Tests for the {@link LogEntry} class.
 * TODO: Test that summary cannot be longer than 1024 chars.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntryTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCreator() {

        // ARRANGE
        final Page p = new Page("foo", _rm);

        // ACT
        final LogEntry le = new LogEntry(_actor, CommandType.RESOURCE_RENAME,
            _happenedOn, p.id(), new JsonImpl(p).getDetail());

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_RENAME.name(), le.action());
    }

    /**
     * Test.
     */
    public void testCreatorActionAsString() {

        // ARRANGE
        final String actionAsString = "TEST_ACTION_NAME";
        final Page p = new Page("foo", _rm);

        // ACT
        final LogEntry le = new LogEntry(_actor, actionAsString,
            _happenedOn, p.id(), new JsonImpl(p).getDetail());

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(actionAsString, le.action());
    }



    private final User _actor = new User(new Username("actor"), "password");
    private final Date _happenedOn = new Date();
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
