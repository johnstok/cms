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
package ccc.persistence;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.api.types.Username;
import ccc.domain.LogEntry;
import ccc.domain.PageEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;


/**
 * Tests for the {@link LogEntryRepository} class.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntryRepositoryImplTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testRecordLockRejectsNull() {

        // ARRANGE

        // ACT
        try {
            new LogEntryRepositoryImpl((Repository) null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRecordCreate() {

        // ARRANGE
        final PageEntity p = new PageEntity(new ResourceName("foo"), "foo", null, _rm);
        final LogEntryRepository al = new LogEntryRepositoryImpl(_em);
        final LogEntry le = new LogEntry(
                                        _actor,
                                        CommandType.RESOURCE_RENAME,
                                        _happenedOn,
                                        p.getId(),
                                        "");
        _em.create(isA(LogEntry.class));
        // ACT
        replay(_em);
        al.record(le);

        // ASSERT
        verify(_em);
        assertEquals(CommandType.RESOURCE_RENAME.toString(), le.getAction());
        assertEquals(p.getId(), le.getSubjectId());
        assertEquals(_actor, le.getActor());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _em = createStrictMock(Repository.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _em = null;
    }


    private final UserEntity _actor = new UserEntity(new Username("actor"), "password");
    private final Date _happenedOn = new Date();
    private Repository _em;
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), UserEntity.SYSTEM_USER, true, "Created.");
}
