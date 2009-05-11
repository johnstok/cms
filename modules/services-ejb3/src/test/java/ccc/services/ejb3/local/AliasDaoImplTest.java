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

import java.util.Date;

import junit.framework.TestCase;
import ccc.domain.Alias;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.ResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AliasDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testUpdateAlias() {

        // ARRANGE
        final Alias alias = new Alias("alias", _resource);

        expect(_dao.find(Resource.class, _r2.id())).andReturn(_r2);
        expect(_dao.findLocked(Alias.class, alias.id(), _user))
            .andReturn(alias);
        _audit.recordUpdate(alias, _user, _now);
        replay(_dao, _audit);

        // ACT
        _updateAlias.execute(_user, _now, _r2.id(), alias.id());

        // ASSERT
        verify(_dao, _audit);
        assertEquals(_r2, alias.target());
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(ResourceDao.class);
        _audit = createStrictMock(AuditLog.class);
        _updateAlias = new AliasDaoImpl(_dao, _audit);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _updateAlias = null;
        _audit = null;
        _dao = null;
    }

    private ResourceDao _dao;
    private AuditLog _audit;
    private AliasDaoImpl _updateAlias;
    private final User _user = new User("currentUser");
    private final Date _now = new Date();
    private final Page _resource = new Page("foo");
    private final Page _r2 = new Page("baa");
}
