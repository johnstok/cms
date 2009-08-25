/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
import ccc.api.PageDelta;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


/**
 * Tests for working copy management.
 *
 * @author Civic Computing Ltd.
 */
public class WorkingCopyManagerTest
    extends
        TestCase {


    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testClearWorkingCopy() throws RemoteExceptionSupport {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
        p.lock(_user);
        p.workingCopy(p.createSnapshot());

        expect(_dao.find(Resource.class, p.id())).andReturn(p);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ClearWorkingCopyCommand(_dao, _audit).execute(
            _user, _now, p.id());

        // ASSERT
        verifyAll();
        assertFalse(p.hasWorkingCopy());
    }


    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testUpdateWorkingCopy() throws RemoteExceptionSupport {

        // ARRANGE
        final Page page =
            new Page(
                new ResourceName("test"),
                "test",
                null,
                _rm,
                Paragraph.fromText("abc", "def"));
        page.lock(_user);
        final PageDelta before = page.createSnapshot();

        expect(_dao.find(Page.class, page.id())).andReturn(page);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateWorkingCopyCommand(_dao, _audit).execute(
            _user, _now, page.id(), before);

        // ASSERT
        verifyAll();
        assertNotNull("Page must have a working copy", page.workingCopy());
    }


    private void verifyAll() {
        verify(_dao, _audit);
    }

    private void replayAll() {
        replay(_dao, _audit);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _dao = createStrictMock(Dao.class);
        _audit = createStrictMock(AuditLog.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _dao = null;
        _audit = null;
    }

    private Dao _dao;
    private AuditLog _audit;
    private final User _user = new User("currentUser");
    private final Date _now = new Date();
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
