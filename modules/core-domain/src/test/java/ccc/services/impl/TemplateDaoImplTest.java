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
import ccc.api.TemplateDelta;
import ccc.commands.UpdateTemplateCommand;
import ccc.domain.LogEntry;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.types.MimeType;


/**
 * Tests for the {@link TemplateDelta} and {@link Template} class.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testUpdateTemplates() throws RemoteExceptionSupport {

        // ARRANGE
        final Template foo = new Template(
            "title",
            "description",
            "body",
            "<fields/>",
            MimeType.HTML,
            new RevisionMetadata(
                new Date(),
                User.SYSTEM_USER,
                true,
                "Created."));
        foo.lock(_user);
        final TemplateDelta td =
            new TemplateDelta("newBody", "newDefn", MimeType.BINARY_DATA);

        expect(_dao.find(Template.class, foo.id())).andReturn(foo);
        _al.record(isA(LogEntry.class));
        replay(_dao, _al);

        final UpdateTemplateCommand ut = new UpdateTemplateCommand(_dao, _al);


        // ACT
        ut.execute(_user, _now, foo.id(), td);


        // ASSERT
        verify(_dao, _al);
        assertEquals("newBody", foo.body());
        assertEquals("newDefn", foo.definition());
        assertEquals(MimeType.BINARY_DATA, foo.mimeType());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _dao = createStrictMock(Dao.class);
        _al = createStrictMock(AuditLog.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _al = null;
        _dao = null;
    }

    private Dao _dao;
    private AuditLog _al;
    private final Date _now = new Date();
    private final User _user = new User("user");
}
