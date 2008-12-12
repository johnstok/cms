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
package ccc.services.ejb3.local;

import static org.easymock.EasyMock.*;

import javax.persistence.EntityManager;

import junit.framework.TestCase;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.services.AuditLog;
import ccc.services.FolderDao;


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
    public  void testCreateFolder() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder("foo");

        _al.recordCreate(isA(Folder.class));
        expect(_em.find(Folder.class, contentRoot.id()))
            .andReturn(contentRoot);
        _em.persist(foo);
        replay(_em, _al);


        // ACT
        _cm.create(contentRoot.id(), foo);


        // ASSERT
        verify(_em, _al);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _al = createStrictMock(AuditLog.class);
        _cm = new FolderDaoImpl(_em, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _al = null;
        _cm = null;
    }

    private EntityManager _em;
    private AuditLog _al;
    private FolderDao _cm;
}
