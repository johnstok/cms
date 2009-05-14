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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.actions.ReorderFolderContentsCommand;
import ccc.actions.UpdateFolderCommand;
import ccc.domain.Folder;
import ccc.domain.LockMismatchException;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceOrder;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.ResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testUpdateSortOrder()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        _f.lock(_regularUser);
        expect(_dao.find(Folder.class, _f.id()))
            .andReturn(_f);
        _al.recordUpdateSortOrder(eq(_f), eq(_regularUser), isA(Date.class));
        replayAll();

        final UpdateFolderCommand uf = new UpdateFolderCommand(_dao, _al);

        // ACT
        uf.execute(
            _regularUser, new Date(), _f.id(), ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        verifyAll();
        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, _f.sortOrder());
    }

    /**
     * Test.
     * @throws LockMismatchException
     * @throws ResourceExistsException
     * @throws UnlockedException
     */
    public void testReorder()
    throws LockMismatchException, ResourceExistsException, UnlockedException {

        // ARRANGE
        _f.lock(_regularUser);
        final Page foo = new Page("foo");
        final Page bar = new Page("bar");
        final Page baz = new Page("baz");
        _f.add(foo);
        _f.add(bar);
        _f.add(baz);

        expect(_dao.find(Folder.class, _f.id()))
            .andReturn(_f);
        _al.recordReorder(eq(_f), eq(_regularUser), isA(Date.class));
        replayAll();

        final ReorderFolderContentsCommand rf =
            new ReorderFolderContentsCommand(_dao, _al);

        // ACT
        final List<UUID> order = new ArrayList<UUID>();
        order.add(baz.id());
        order.add(foo.id());
        order.add(bar.id());

        rf.execute(_regularUser, new Date(), _f.id(), order);

        // ASSERT
        verifyAll();
        final List<Resource> entries = _f.entries();
        assertEquals(3, entries.size());
        assertEquals(baz, entries.get(0));
        assertEquals(foo, entries.get(1));
        assertEquals(bar, entries.get(2));

    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _al = createStrictMock(AuditLog.class);
        _rdao = createStrictMock(ResourceDao.class);
        _dao = createStrictMock(Dao.class);

        _f = new Folder("foo");
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _rdao = null;
        _al = null;
    }

    private void replayAll() {
        replay(_rdao, _al, _dao);
    }

    private void verifyAll() {
        verify(_rdao, _al, _dao);
    }
    private final User _regularUser = new User("regular");

    private Folder _f;

    private AuditLog _al;
    private ResourceDao _rdao;
    private Dao _dao;
}
