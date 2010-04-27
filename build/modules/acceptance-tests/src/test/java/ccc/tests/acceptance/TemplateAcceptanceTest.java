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

import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.core.TemplateDto;
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
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final TemplateDto t = new TemplateDto();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        // ACT
        final ResourceSummary ts = getTemplates().createTemplate(t);

        // ASSERT
        assertEquals("/assets/templates/"+name, ts.getAbsolutePath());
        assertEquals("t-desc", ts.getDescription());
        assertEquals(name, ts.getName());
        assertEquals("t-title", ts.getTitle());
    }

    /**
     * Test.
     */
    public void testTemplateDelta() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final String name = UUID.randomUUID().toString();

        final TemplateDto t = new TemplateDto();
        t.setName(new ResourceName(name));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        // ACT
        final ResourceSummary ts = getTemplates().createTemplate(t);

        // ACT
        final TemplateDto fetched = getTemplates().templateDelta(ts.getId());

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
        final ResourceSummary folder = tempFolder();
        final ResourceSummary t = dummyTemplate(folder);

        // ACT
        final Boolean exists = getTemplates().templateNameExists(t.getName());

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
        final ResourceSummary folder = tempFolder();
        final ResourceSummary t = dummyTemplate(folder);

        final TemplateDto delta = new TemplateDto();
        delta.setBody("newBody");
        delta.setDefinition(
            "<fields><field name=\"test\" type=\"html\"/></fields>");
        delta.setMimeType(MimeType.BINARY_DATA);

        // ACT
        getCommands().lock(t.getId());
        getTemplates().updateTemplate(t.getId(), delta);
        final TemplateDto updated = getTemplates().templateDelta(t.getId());

        // ASSERT
        assertEquals(delta.getBody(), updated.getBody());
        assertEquals(delta.getDefinition(), updated.getDefinition());
        assertEquals(MimeType.BINARY_DATA, updated.getMimeType());

    }

}
