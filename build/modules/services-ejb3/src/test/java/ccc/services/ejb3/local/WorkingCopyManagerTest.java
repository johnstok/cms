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
package ccc.services.ejb3.local;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.actions.ClearWorkingCopyCommand;
import ccc.actions.UpdateWorkingCopyCommand;
import ccc.domain.LockMismatchException;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.Dao;


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
     * @throws LockMismatchException
     * @throws UnlockedException
     * @throws ResourceExistsException
     */
    public void testClearWorkingCopy()
    throws UnlockedException, LockMismatchException, ResourceExistsException {

        // ARRANGE
        final Page p = new Page("foo");
        p.lock(_user);
        p.workingCopy(p.createSnapshot());

        expect(_dao.find(Resource.class, p.id())).andReturn(p);
        replayAll();

        // ACT
        new ClearWorkingCopyCommand(_dao, null).execute(
            _user, _now, p.id());

        // ASSERT
        verifyAll();
        assertNull(p.workingCopy());
    }


    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     * @throws ResourceExistsException
     */
    public void testUpdateWorkingCopy()
    throws UnlockedException, LockMismatchException, ResourceExistsException {

        // ARRANGE
        final Page page = new Page("test");
        page.lock(_user);
        page.addParagraph(Paragraph.fromText("abc", "def"));
        final Snapshot before = page.createSnapshot();

        expect(_dao.find(Resource.class, page.id())).andReturn(page);
        replayAll();

        // ACT
        new UpdateWorkingCopyCommand(_dao, null).execute(
            _user, _now, page.id(), before);

        // ASSERT
        verifyAll();
        assertNotNull("Page must have a working copy", page.workingCopy());
        assertEquals("test", page.workingCopy().getString("title"));

    }


    private void verifyAll() {
        verify(_dao);
    }

    private void replayAll() {
        replay(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(Dao.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
    }

    private Dao _dao;
    private final User _user = new User("currentUser");
    private final Date _now = new Date();
}
