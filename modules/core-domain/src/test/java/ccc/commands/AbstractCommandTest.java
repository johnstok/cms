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

package ccc.commands;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Abstract class to simplify writing tests for commands.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractCommandTest
    extends
        TestCase {

    protected final User _user = new User("currentUser");
    protected final Date _now = new Date();

    protected Dao _dao;
    protected AuditLog _audit;


    /** Constructor. */
    public AbstractCommandTest() { super(); }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _dao = createStrictMock(Dao.class);
        _audit = createStrictMock(AuditLog.class);
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _audit = null;
        _dao = null;
    }


    /** Verify all mocks. */
    protected void verifyAll() {
        verify(_dao, _audit);
    }


    /** Replay all mocks. */
    protected void replayAll() {
        replay(_dao, _audit);
    }
}