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
package ccc.services.impl;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.commands.UpdateTemplateCommand;
import ccc.domain.LockMismatchException;
import ccc.domain.Template;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.api.TemplateDelta;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testUpdateTemplates()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        final Template foo =
            new Template("title", "description", "body", "<fields/>");
        foo.lock(_user);
        final TemplateDelta td = new TemplateDelta(
            "newTitle", "newDesc", "newBody", "newDefn");

        expect(_dao.find(Template.class, foo.id())).andReturn(foo);
        _al.recordUpdate(foo, _user, _now, null, false);
        replay(_dao, _al);

        final UpdateTemplateCommand ut = new UpdateTemplateCommand(_dao, _al);


        // ACT
        ut.execute(_user, _now, foo.id(), td);


        // ASSERT
        verify(_dao, _al);
        assertEquals("newTitle", foo.title());
        assertEquals("newDesc", foo.description());
        assertEquals("newBody", foo.body());
        assertEquals("newDefn", foo.definition());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(Dao.class);
        _al = createStrictMock(AuditLog.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _al = null;
        _dao = null;
    }

    private Dao _dao;
    private AuditLog _al;
    private final Date _now = new Date();
    private final User _user = new User("user");
}
