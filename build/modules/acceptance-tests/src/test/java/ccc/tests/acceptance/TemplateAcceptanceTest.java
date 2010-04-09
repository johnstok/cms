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

import ccc.api.dto.ResourceSummary;
import ccc.api.dto.TemplateDelta;
import ccc.api.dto.TemplateDto;
import ccc.api.exceptions.RestException;
import ccc.types.MimeType;


/**
 * Acceptance tests for Templates REST methods.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateAcceptanceTest extends
        AbstractAcceptanceTest {

    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testCreateTemplate() throws RestException {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final TemplateDelta newTemplate =
            new TemplateDelta("body", "<fields/>", MimeType.HTML);

        // ACT
        final ResourceSummary ts =
            getTemplates().createTemplate(
                new TemplateDto(
                    templateFolder.getId(),
                    newTemplate,
                    "t-title",
                    "t-desc",
                    name));

        // ASSERT
        assertEquals("/assets/templates/"+name, ts.getAbsolutePath());
        assertEquals("t-desc", ts.getDescription());
        assertEquals(name, ts.getName());
        assertEquals("t-title", ts.getTitle());
    }

    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testTemplateDelta() throws RestException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final String name = UUID.randomUUID().toString();

        final TemplateDelta newTemplate =
            new TemplateDelta("body", "<fields/>", MimeType.HTML);

        // ACT
        final ResourceSummary ts =
            getTemplates().createTemplate(
                new TemplateDto(
                    folder.getId(),
                    newTemplate,
                    "t-title",
                    "t-desc",
                    name));


        // ACT
        final TemplateDelta fetched = getTemplates().templateDelta(ts.getId());

        // ASSERT
        assertEquals("body", fetched.getBody());
        assertEquals("<fields/>", fetched.getDefinition());
        assertEquals(MimeType.HTML, fetched.getMimeType());
    }

    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testTemplateNameExists() throws RestException {

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
     * @throws RestException If the test fails.
     */
    public void testUpdateTemplate() throws RestException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary t = dummyTemplate(folder);

        // ACT
        final TemplateDelta delta = new TemplateDelta("newBody",
            "<fields><field name=\"test\" type=\"html\"/></fields>",
            MimeType.BINARY_DATA);
        getCommands().lock(t.getId());
        getTemplates().updateTemplate(t.getId(), delta);
        final TemplateDelta updated = getTemplates().templateDelta(t.getId());

        // ASSERT
        assertEquals(delta.getBody(), updated.getBody());
        assertEquals(delta.getDefinition(), updated.getDefinition());
        assertEquals(MimeType.BINARY_DATA, updated.getMimeType());

    }

}
