/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.tests.acceptance;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ccc.api.core.ACL;
import ccc.api.core.Folder;
import ccc.api.core.Resource;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.core.ACL.Entry;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


/**
 * Acceptance tests for Templates REST methods.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateAcceptanceTest extends
        AbstractAcceptanceTest {

    /**
     * Test.
     */
    public void testCreateTemplate() {

        // ARRANGE
        final Resource templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        // ACT
        final Template ts = getTemplates().create(t);

        // ASSERT
        assertEquals("/assets/templates/"+name, ts.getAbsolutePath());
        assertEquals("t-desc", ts.getDescription());
        assertEquals(name, ts.getName().toString());
        assertEquals("t-title", ts.getTitle());
    }

    /**
     * Test.
     */
    public void testTemplateDelta() {

        // ARRANGE
        final Folder folder = tempFolder();
        final String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        // ACT
        final Template ts = getTemplates().create(t);

        // ACT
        final Template fetched = getTemplates().retrieve(ts.getId());

        // ASSERT
        assertEquals("body", fetched.getBody());
        assertEquals("<fields/>", fetched.getDefinition());
        assertEquals(MimeType.HTML, fetched.getMimeType());
    }

    /**
     * Test.
     */
    public void testTemplateNameExists() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Template t = dummyTemplate(folder);

        // ACT
        final Boolean exists =
            getTemplates().templateNameExists(t.getName().toString());

        //ASSERT
        assertTrue("Template should exists", exists.booleanValue());
        assertFalse("Template should not exists",
            getTemplates().templateNameExists("foo").booleanValue());
    }

    /**
     * Test.
     */
    public void testUpdateTemplate() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Template t = dummyTemplate(folder);

        final Template delta = new Template();
        delta.setBody("newBody");
        delta.setDefinition(
            "<fields><field name=\"test\" type=\"html\"/></fields>");
        delta.setMimeType(MimeType.BINARY_DATA);

        // ACT
        getCommands().lock(t.getId());
        getTemplates().update(t.getId(), delta);
        final Template updated = getTemplates().retrieve(t.getId());

        // ASSERT
        assertEquals(delta.getBody(), updated.getBody());
        assertEquals(delta.getDefinition(), updated.getDefinition());
        assertEquals(MimeType.BINARY_DATA, updated.getMimeType());

    }


    /**
     * Test.
     */
    public void testRetrieveTemplateRevision() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Template t = dummyTemplate(folder);

        final Template delta = new Template();
        delta.setBody("newBody");
        delta.setDefinition(
            "<fields><field name=\"test\" type=\"html\"/></fields>");
        delta.setMimeType(MimeType.BINARY_DATA);

        getCommands().lock(t.getId());
        getTemplates().update(t.getId(), delta);

        // ACT
        final Template original = getTemplates().retrieveRevision(t.getId(), 0);
        final Template updated = getTemplates().retrieveRevision(t.getId(), 1);

        // ASSERT
        assertEquals("newBody", updated.getBody());
        assertEquals("<fields><field name=\"test\" type=\"html\"/></fields>",
                     updated.getDefinition());
        assertEquals(MimeType.BINARY_DATA, updated.getMimeType());

        assertEquals("body", original.getBody());
        assertEquals("<fields/>", original.getDefinition());
        assertEquals(MimeType.HTML, original.getMimeType());

    }


    /**
     * Test.
     */
    public void testUpdateTemplateFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Template rs = dummyTemplate(folder);

        getCommands().lock(rs.getId());

        final ACL acl = new ACL();
        final List<Entry> users = new ArrayList<Entry>();
        final Entry e = new Entry();
        final User u = getUsers().retrieveCurrent();
        e.setName(u.getName());
        e.setPrincipal(u.getId());
        e.setReadable(true);
        e.setWriteable(false);
        users.add(e);
        acl.setUsers(users);
        getCommands().changeAcl(rs.getId(), acl);

        // ACT
        try {
            getTemplates().update(rs.getId(), rs);
            // ASSERT
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(rs.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }
}
