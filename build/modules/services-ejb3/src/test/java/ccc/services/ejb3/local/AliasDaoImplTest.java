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
import junit.framework.TestCase;
import ccc.domain.Alias;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.AliasDao;
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
    public void testNoArgsConstructor() {

        // ARRANGE

        // ACT
        new AliasDaoImpl();

        // ASSERT

    }

    /**
     * Test.
     */
    public void testUpdateAlias() {

        // ARRANGE
        final Page resource = new Page("foo");
        final Page r2 = new Page("baa");
        final Alias alias = new Alias("alias", resource);

        expect(_dao.find(Resource.class, r2.id())).andReturn(r2);
        expect(_dao.findLocked(Alias.class, alias.id(), _user))
            .andReturn(alias);
        _dao.update(_user, alias);
        replay(_dao);

        // ACT
        _cm.updateAlias(_user, r2.id(), alias.id());

        // ASSERT
        verify(_dao);
        assertEquals(r2, alias.target());
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(ResourceDao.class);
        _cm = new AliasDaoImpl(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _cm = null;
    }

    private ResourceDao _dao;
    private AliasDao _cm;
    private final User _user = new User("currentUser");
}
