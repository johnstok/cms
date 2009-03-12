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
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceOrder;
import ccc.services.FolderDao;
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
     */
    public void testUpdateSortOrder() {

        // ARRANGE
        final Folder f = new Folder("foo");

        expect(_dao.find(Folder.class, f.id())).andReturn(f);
        replay(_dao);

        // ACT
        _fdao.updateSortOrder(f.id(), ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        verify(_dao);
        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, f.sortOrder());
    }

    /**
     * Test.
     */
    public void testReorder() {

        // ARRANGE
        final Folder f = new Folder("foo");
        final Page foo = new Page("foo");
        final Page bar = new Page("bar");
        final Page baz = new Page("baz");
        f.add(foo);
        f.add(bar);
        f.add(baz);

        expect(_dao.find(Folder.class, f.id())).andReturn(f);
        replay(_dao);

        // ACT
        final List<UUID> order = new ArrayList<UUID>();
        order.add(baz.id());
        order.add(foo.id());
        order.add(bar.id());

        _fdao.reorder(f.id(), order);

        // ASSERT
        verify(_dao);
        final List<Resource> entries = f.entries();
        assertEquals(3, entries.size());
        assertEquals(baz, entries.get(0));
        assertEquals(foo, entries.get(1));
        assertEquals(bar, entries.get(2));

    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(ResourceDao.class);
        _fdao = new FolderDaoImpl(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _fdao = null;
    }

    private ResourceDao _dao;
    private FolderDao _fdao;
}
