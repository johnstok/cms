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
import ccc.api.Paragraph;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.LockMismatchException;
import ccc.domain.Page;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.domain.WorkingCopyAware;
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
     */
    public void testClearWorkingCopy()
    throws UnlockedException, LockMismatchException {

        // ARRANGE
        final Page p = new Page("foo");
        p.lock(_user);
        p.workingCopy(p.createSnapshot());

        expect(_dao.find(WorkingCopyAware.class, p.id())).andReturn(p);
        replayAll();

        // ACT
        new ClearWorkingCopyCommand(_dao, null).execute(
            _user, _now, p.id());

        // ASSERT
        verifyAll();
//        assertNull(p.workingCopy());
    }


    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testUpdateWorkingCopy()
    throws UnlockedException, LockMismatchException {

        // ARRANGE
        final Page page = new Page("test");
        page.lock(_user);
        page.addParagraph(Paragraph.fromText("abc", "def"));
        final PageDelta before = page.createSnapshot();

        expect(_dao.find(Page.class, page.id())).andReturn(page);
        replayAll();

        // ACT
        new UpdateWorkingCopyCommand(_dao, null).execute(
            _user, _now, page.id(), before);

        // ASSERT
        verifyAll();
        assertNotNull("Page must have a working copy", page.workingCopy());
        assertEquals("test", page.workingCopy().getTitle());

    }


    private void verifyAll() {
        verify(_dao);
    }

    private void replayAll() {
        replay(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _dao = createStrictMock(Dao.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _dao = null;
    }

    private Dao _dao;
    private final User _user = new User("currentUser");
    private final Date _now = new Date();
}