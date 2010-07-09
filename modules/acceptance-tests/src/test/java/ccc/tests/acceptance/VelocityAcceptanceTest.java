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

import java.util.Calendar;
import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.commons.Environment;


/**
 * Acceptance tests for Velocity plugin.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     */
    public void testHostnameProperty() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$hostname");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary template = getTemplates().create(t);

        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue(pContent.startsWith(Environment.getHostname()));
    }


    /**
     * Test.
     */
    public void testUuidProperty() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final UUID uuid = UUID.randomUUID();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$uuid.fromString('"+uuid+"')");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary template = getTemplates().create(t);

        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertEquals(uuid.toString(), pContent);
    }


    /**
     * Test.
     */
    public void testHtmlProperty() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$html.escape('<>')");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary template = getTemplates().create(t);

        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertEquals("&lt;&gt;", pContent);
    }


    /**
     * Test.
     */
    public void testCalendarProperty() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$calendar.getInstance().getTimeZone().getID()");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary template = getTemplates().create(t);

        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertEquals(Calendar.getInstance().getTimeZone().getID(), pContent);
    }


    /**
     * Test.
     */
    public void testRandomProperty() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$random.nextInt(10)");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary template = getTemplates().create(t);

        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue(0 <= Integer.valueOf(pContent).intValue());
        assertTrue(10 > Integer.valueOf(pContent).intValue());
    }
}
