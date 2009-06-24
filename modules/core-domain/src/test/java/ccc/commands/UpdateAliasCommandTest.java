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
package ccc.commands;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.domain.Alias;
import ccc.domain.Page;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasCommandTest
    extends
        TestCase {

    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testUpdateAlias() throws RemoteExceptionSupport {

        // ARRANGE
        final Alias alias = new Alias("alias", _resource);
        alias.lock(_user);

        expect(_dao.find(Alias.class, alias.id())).andReturn(alias);
        expect(_dao.find(Resource.class, _r2.id())).andReturn(_r2);
        _audit.recordUpdate(alias, _user, _now, null, false);
        replay(_dao, _audit);

        // ACT
        _updateAlias.execute(_user, _now, _r2.id(), alias.id());

        // ASSERT
        verify(_dao, _audit);
        assertEquals(_r2, alias.target());
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _dao = createStrictMock(Dao.class);
        _audit = createStrictMock(AuditLog.class);
        _updateAlias = new UpdateAliasCommand(_dao, _audit);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _updateAlias = null;
        _audit = null;
        _dao = null;
    }

    private Dao _dao;
    private AuditLog _audit;
    private UpdateAliasCommand _updateAlias;
    private final User _user = new User("currentUser");
    private final Date _now = new Date();
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
    private final Page _resource =
        new Page(new ResourceName("foo"), "foo", null, _rm);
    private final Page _r2 =
        new Page(new ResourceName("baa"), "baa", null, _rm);
}
