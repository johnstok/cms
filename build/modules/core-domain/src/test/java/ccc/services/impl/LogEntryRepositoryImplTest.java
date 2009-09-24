/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.impl;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.Repository;
import ccc.types.CommandType;
import ccc.types.ResourceName;
import ccc.types.Username;


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
        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
        final LogEntryRepository al = new LogEntryRepositoryImpl(_em);
        final LogEntry le = new LogEntry(
                                        _actor,
                                        CommandType.RESOURCE_RENAME,
                                        _happenedOn,
                                        p.id(),
                                        "");
        _em.create(isA(LogEntry.class));
        // ACT
        replay(_em);
        al.record(le);

        // ASSERT
        verify(_em);
        assertEquals(CommandType.RESOURCE_RENAME, le.action());
        assertEquals(p.id(), le.subjectId());
        assertEquals(_actor, le.actor());
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


    private final User _actor = new User(new Username("actor"), "password");
    private final Date _happenedOn = new Date();
    private Repository _em;
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
