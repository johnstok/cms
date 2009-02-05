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
import ccc.domain.Folder;
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

        expect(_dao.findLocked(Folder.class, f.id())).andReturn(f);
        replay(_dao);

        // ACT
        _fdao.updateSortOrder(f.id(), ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        verify(_dao);
        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, f.sortOrder());
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
