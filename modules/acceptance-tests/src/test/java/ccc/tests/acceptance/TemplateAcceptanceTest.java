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
import ccc.api.core.Template;
import ccc.api.exceptions.InvalidException;
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

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        // ACT
        final ResourceSummary ts = getTemplates().create(t);

        // ASSERT
        assertEquals("/assets/templates/"+name, ts.getAbsolutePath());
        assertEquals("t-desc", ts.getDescription());
        assertEquals(name, ts.getName());
        assertEquals("t-title", ts.getTitle());
    }


    /**
     * Test.
     */
    public void testCreateTemplateWithValidSchema() {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition(VALID_SCHEMA_EXAMPLE);
        t.setMimeType(MimeType.HTML);

        // ACT
        ResourceSummary ts;
        try {
            ts = getTemplates().create(t);
            final Template newTemplate = getTemplates().retrieve(ts.getId());

            // ASSERT
            assertEquals(VALID_SCHEMA_EXAMPLE, newTemplate.getDefinition());
        } catch (final InvalidException e) {
            fail();
        }
    }


    /**
     * Test.
     */
    public void testCreateTemplateWithInvalidXML() {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("<fields><notclosed<previoustag />"); //invalid XML
        t.setMimeType(MimeType.HTML);

        // ACT
        try {
            getTemplates().create(t);
            fail();
        } catch (final InvalidException e) {

            // ASSERT
            assertTrue(e.getMessage()
                .startsWith("Invalid template definition"));
        }
    }

    /**
     * Test.
     */
    public void testCreateTemplateWithInvalidSchema() {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("<fields><garbage /></fields>"); //invalid Schema
        t.setMimeType(MimeType.HTML);

        // ACT
        try {
            getTemplates().create(t);
            fail();
        } catch (final InvalidException e) {

            // ASSERT
            assertTrue(e.getMessage()
                .startsWith("Invalid template definition"));
        }
    }


    /**
     * Test.
     */
    public void testCreateTemplateWithEmptyDefinition() {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        final String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("body");
        t.setDefinition("");
        t.setMimeType(MimeType.HTML);

        // ACT
        try {
            getTemplates().create(t);
            fail();
        } catch (final InvalidException e) {

            // ASSERT
            assertTrue(
                e.getMessage().startsWith("Invalid template definition"));
        }
    }


    /**
     * Test.
     */
    public void testTemplateDelta() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
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
        final ResourceSummary ts = getTemplates().create(t);

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
    public void testUpdateTemplateWithValidSchema() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary dte = dummyTemplate(folder);

        final Template delta = getTemplates().retrieve(dte.getId());
        delta.setDefinition(VALID_SCHEMA_EXAMPLE);

        // ACT
        try {
            getCommands().lock(dte.getId());
            getTemplates().update(dte.getId(), delta);
            final Template updated = getTemplates().retrieve(dte.getId());

            // ASSERT
            assertEquals(delta.getDefinition(), updated.getDefinition());
        } catch (final InvalidException e) {
            fail("Exception thrown during the template update");
        }
    }


    /**
     * Test.
     */
    public void testUpdateTemplateWithInvalidXML() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary dte = dummyTemplate(folder);

        final Template delta = new Template();
        delta.setDefinition("<fields><notclosed<previoustag />"); //invalid XML


        // ACT
        try {
            getCommands().lock(dte.getId());
            getTemplates().update(dte.getId(), delta);
            fail();
        } catch (final InvalidException e) {

            // ASSERT
            assertTrue(e.getMessage()
                .startsWith("Invalid template definition"));
        }
    }


    /**
     * Test.
     */
    public void testUpdateTemplateWithInvalidSchema() {
        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary dte = dummyTemplate(folder);

        final Template delta = new Template();
        delta.setDefinition("<fields><garbage /></fields>"); //invalid Schema

        // ACT
        try {
            getCommands().lock(dte.getId());
            getTemplates().update(dte.getId(), delta);
            fail();
        } catch (final InvalidException e) {

            // ASSERT
            assertTrue(e.getMessage()
                .startsWith("Invalid template definition"));
        }
    }


    /**
     * Test.
     */
    public void testUpdateTemplateWithEmptyDefinition() {
     // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary dte = dummyTemplate(folder);

        final Template delta = new Template();
        delta.setDefinition("");
        // ACT
        try {
            getCommands().lock(dte.getId());
            getTemplates().update(dte.getId(), delta);
            fail();
        } catch (final InvalidException e) {

            // ASSERT
            assertTrue(
                e.getMessage().startsWith("Invalid template definition"));
        }
    }


    /**
     * Test.
     */
    public void testRetrieveTemplateRevision() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary t = dummyTemplate(folder);

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

    // taken from field-definition.textile
    private static final String VALID_SCHEMA_EXAMPLE =
        "<fields>"
        +"    <field name=\"dateField\" type=\"date\" "
        +               "description=\"birthday\"/>"
        +"    <field name=\"oneLine\" type=\"text_field\" />"
        +"    <field name=\"oneLineWithValidation\" type=\"text_field\" "
                +"regexp=\"\\d{1,3}\"/>"
        +"    <field name=\"manyLines\" type=\"text_area\" "
                +"title=\"Longer text\" />"
        +"    <field name=\"richText\" type=\"html\" />"
        +"    <field name=\"checkBoxes\" type=\"checkbox\">"
        +"        <option default=\"true\" title=\"My Value\" "
                +"value=\"my_value\"/>"
        +"        <option title=\"Other Value\" value=\"other_value\"/>"
        +"    </field>"
        +"    <field name=\"radio\" type=\"radio\">"
        +"        <option default=\"true\" title=\"My Value\" "
                +"value=\"my_value\"/>"
        +"        <option title=\"Other Value\" value=\"other_value\"/>"
        +"    </field>"
        +"    <field name=\"combos\" type=\"combobox\">"
        +"        <option default=\"true\" title=\"My Value\" "
                +"value=\"my_value\"/>"
        +"        <option title=\"Other Value\" value=\"other_value\"/>"
        +"    </field>"
        +"    <field name=\"list\" type=\"list\">"
        +"        <option default=\"true\" title=\"My Value\" "
                +"value=\"my_value\"/>"
        +"        <option title=\"Other Value\" value=\"other_value\"/>"
        +"    </field>"
        +"    <field name=\"photo\" type=\"image\"/>"
        +"    <field name=\"number\" type=\"number\"/>"
        +"</fields>";

}
