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
import junit.framework.TestCase;
import ccc.domain.LockMismatchException;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.ResourceDao;
import ccc.services.WorkingCopyManager;


/**
 * TODO: Add Description for this type.
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
        p.workingCopy(p.createSnapshot());

        expect(_dao.findLocked(Resource.class, p.id(), _user)).andReturn(p);
        replayAll();

        // ACT
        _wcm.clearWorkingCopy(_user, p.id());

        // ASSERT
        verifyAll();
        assertNull(p.workingCopy());
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
        page.addParagraph(Paragraph.fromText("abc", "def"));
        final Snapshot before = page.createSnapshot();

        expect(_dao.findLocked(Resource.class, page.id(), _user)).andReturn(page);
        replayAll();

        // ACT
        _wcm.updateWorkingCopy(_user, page.id(), before);

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
        _dao = createStrictMock(ResourceDao.class);
        _wcm = new WorkingCopyManager(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _wcm = null;
    }

    private ResourceDao _dao;
    private WorkingCopyManager _wcm;
    private final User _user = new User("currentUser");
}
