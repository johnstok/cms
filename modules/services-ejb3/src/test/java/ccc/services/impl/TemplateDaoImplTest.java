/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import ccc.api.dto.TemplateDelta;
import ccc.commands.AbstractCommandTest;
import ccc.commands.UpdateTemplateCommand;
import ccc.domain.LogEntry;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.types.MimeType;


/**
 * Tests for the {@link TemplateDelta} and {@link Template} class.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateDaoImplTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     */
    public void testUpdateTemplates() {

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

        expect(_repository.find(Template.class, foo.getId())).andReturn(foo);
        _audit.record(isA(LogEntry.class));
        replayAll();

        final UpdateTemplateCommand ut =
            new UpdateTemplateCommand(_repoFactory, foo.getId(), td);


        // ACT
        ut.execute(_user, _now);


        // ASSERT
        verifyAll();
        assertEquals("newBody", foo.getBody());
        assertEquals("newDefn", foo.getDefinition());
        assertEquals(MimeType.BINARY_DATA, foo.getMimeType());
    }
}
