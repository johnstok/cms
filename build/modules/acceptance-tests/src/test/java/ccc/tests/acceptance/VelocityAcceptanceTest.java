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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.UUID;

import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.Template;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


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
    public void testUuidProperty() {

        // ARRANGE
        final Folder folder = tempFolder();
        final UUID uuid = UUID.randomUUID();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$uuid.fromString('"+uuid+"')");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());

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
        final Folder folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$html.escape('<>')");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());

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
        final Folder folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$calendar.getInstance().getTimeZone().getID()");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());

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
        final Folder folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$random.nextInt(10)");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue(0 <= Integer.valueOf(pContent).intValue());
        assertTrue(10 > Integer.valueOf(pContent).intValue());
    }

    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testGetStyleMacro() throws IOException {

        // ARRANGE
        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n\n #getStyle()");
        final Folder folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(macroContent.toString());
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertEquals("default", pContent.trim());
    }

    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testBreadCrumbMacro() throws IOException {

        // ARRANGE
        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n");
        macroContent.append(
            readFile("../application-ear/templates/breadCrumb.vm"));
        macroContent.append("\n\n #breadcrumb()");
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        folder.setTitle("first");
        getCommands().updateMetadata(folder.getId(), folder);

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(macroContent.toString());
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());
        getCommands().lock(page.getId());
        page.setTitle("second");
        getCommands().updateMetadata(page.getId(), page);

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertEquals("<a href=\"/"+folder.getName()
            +"\">first</a>\n&nbsp;&gt;&nbsp;\nsecond", pContent.trim());
    }

    private static String readFile(final String path) throws IOException {
        final FileInputStream stream = new FileInputStream(new File(path));
        try {
            final FileChannel fc = stream.getChannel();
            final MappedByteBuffer bb =
                fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }

}
