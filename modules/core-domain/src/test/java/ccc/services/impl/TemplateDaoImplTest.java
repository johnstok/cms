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
import ccc.domain.LogEntry;
import ccc.domain.CccCheckedException;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.rest.dto.TemplateDelta;
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
     * @throws CccCheckedException If the command fails.
     */
    public void testUpdateTemplates() throws CccCheckedException {

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

        expect(_repository.find(Template.class, foo.id())).andReturn(foo);
        _al.record(isA(LogEntry.class));
        replay(_repository, _al);

        final UpdateTemplateCommand ut = new UpdateTemplateCommand(_repository, _al);


        // ACT
        ut.execute(_user, _now, foo.id(), td);


        // ASSERT
        verify(_repository, _al);
        assertEquals("newBody", foo.body());
        assertEquals("newDefn", foo.definition());
        assertEquals(MimeType.BINARY_DATA, foo.mimeType());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _repository = createStrictMock(Repository.class);
        _al = createStrictMock(AuditLog.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _al = null;
        _repository = null;
    }

    private Repository _repository;
    private AuditLog _al;
    private final Date _now = new Date();
    private final User _user = new User("user");
}
