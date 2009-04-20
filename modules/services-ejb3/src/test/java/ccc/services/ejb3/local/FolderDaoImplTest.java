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
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceOrder;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.FolderDao;
import ccc.services.ResourceDao;
import ccc.services.UserManager;


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
     */
    public void testUpdateSortOrder() {

        // ARRANGE
        _f.lock(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.findLocked(Folder.class, _f.id())).andReturn(_f);
        _al.recordUpdateSortOrder(eq(_f), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _fdao.updateSortOrder(_f.id(), ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        verifyAll();
        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, _f.sortOrder());
    }

    /**
     * Test.
     */
    public void testReorder() {

        // ARRANGE
        _f.lock(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        final Page foo = new Page("foo");
        final Page bar = new Page("bar");
        final Page baz = new Page("baz");
        _f.add(foo);
        _f.add(bar);
        _f.add(baz);

        expect(_dao.findLocked(Folder.class, _f.id())).andReturn(_f);
        _al.recordReorder(eq(_f), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        final List<UUID> order = new ArrayList<UUID>();
        order.add(baz.id());
        order.add(foo.id());
        order.add(bar.id());

        _fdao.reorder(_f.id(), order);

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
        _users = createStrictMock(UserManager.class);
        _dao = createStrictMock(ResourceDao.class);
        _fdao = new FolderDaoImpl(_dao, _al, _users);

        _f = new Folder("foo");
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _fdao = null;
        _al = null;
        _users = null;
    }

    private void replayAll() {
        replay(_dao, _users, _al);
    }

    private void verifyAll() {
        verify(_dao, _users, _al);
    }
    private final User _regularUser = new User("regular");

    private Folder _f;

    private AuditLog _al;
    private UserManager _users;
    private ResourceDao _dao;
    private FolderDao _fdao;
}
