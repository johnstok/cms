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
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.Comment;
import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.Template;
import ccc.api.types.CommentStatus;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
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

    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testMenuTagMacro() throws IOException {

        // ARRANGE
        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n");
        macroContent.append(
            readFile("../application-ear/templates/mainMenu.vm"));
        macroContent.append("\n\n #menuTag()");

        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        folder.setTitle("first");
        getCommands().updateMetadata(folder.getId(), folder);
        getCommands().publish(folder.getId());
        getCommands().includeInMainMenu(folder.getId());

        final String fName = UUID.randomUUID().toString();
        final Folder f2 = getFolders().create(
            new Folder(folder.getId(), new ResourceName(fName)));
        getCommands().lock(f2.getId());
        f2.setTitle("second");
        getCommands().updateMetadata(f2.getId(), f2);
        getCommands().publish(f2.getId());
        getCommands().includeInMainMenu(f2.getId());

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(macroContent.toString());
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(f2.getId(), template.getId());
        getCommands().lock(page.getId());
        page.setTitle("third");
        getCommands().publish(page.getId());
        getCommands().includeInMainMenu(page.getId());
        getCommands().updateMetadata(page.getId(), page);

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue("Should not have 'first' entry"
            ,  pContent.indexOf("first") == -1);
        assertTrue("Should have 'second' entry"
            ,  pContent.indexOf("second") != -1);
        assertTrue("Should have 'third' entry"
            ,  pContent.indexOf("third") != -1);
        assertTrue("'second' should be before 'third'"
            ,  pContent.indexOf("second") <  pContent.indexOf("third"));
    }

    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testEmptyMenuTagMacro() throws IOException {

        // ARRANGE
        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n");
        macroContent.append(
            readFile("../application-ear/templates/mainMenu.vm"));
        macroContent.append("\n\n #menuTag()");

        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        folder.setTitle("first");
        getCommands().updateMetadata(folder.getId(), folder);
        getCommands().publish(folder.getId());

        final String fName = UUID.randomUUID().toString();
        final Folder f2 = getFolders().create(
            new Folder(folder.getId(), new ResourceName(fName)));
        getCommands().lock(f2.getId());
        f2.setTitle("second");
        getCommands().updateMetadata(f2.getId(), f2);
        getCommands().publish(f2.getId());

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(macroContent.toString());
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(f2.getId(), template.getId());
        getCommands().lock(page.getId());
        page.setTitle("third");
        getCommands().publish(page.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue("Should not have 'li' entry"
            ,  pContent.indexOf("<li>") == -1);
        assertTrue("Should no have 'ul' entry"
            ,  pContent.indexOf("<ul>") == -1);
    }

    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testSiteMap() throws IOException {

        // ARRANGE
        final Folder folder = tempFolder();
        folder.setTitle("root");
        getCommands().lock(folder.getId());
        getCommands().updateMetadata(folder.getId(), folder);
        getCommands().publish(folder.getId());

        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n");
        macroContent.append(
            readFile("../application-ear/templates/siteMap.vm"));
        macroContent.append("\n\n #set($parent = "
            + "$resources.retrieve($uuid.fromString(\""
            + folder.getId().toString()+"\")))");
        macroContent.append("\n\n #printChildren($parent)");

        final String fName = UUID.randomUUID().toString();
        final Folder f2 = getFolders().create(
            new Folder(folder.getId(), new ResourceName(fName)));
        getCommands().lock(f2.getId());
        f2.setTitle("f2");
        getCommands().publish(f2.getId());
        getCommands().updateMetadata(f2.getId(), f2);

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(macroContent.toString());
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(f2.getId(), template.getId());
        getCommands().lock(page.getId());
        page.setTitle("page");
        getCommands().updateMetadata(page.getId(), page);
        getCommands().publish(page.getId());

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue("Should not have 'root' entry"
            ,  pContent.indexOf(folder.getTitle()) == -1);
        assertTrue("Should have 'f2' entry"
            ,  pContent.indexOf(f2.getTitle()) != -1);
        assertTrue("Should have 'page' entry"
            ,  pContent.indexOf(page.getTitle()) != -1);
    }

    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testDisplayComments() throws IOException {

        // ARRANGE
        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n");
        macroContent.append(
            readFile("../application-ear/templates/displayComments.vm"));
        macroContent.append("\n");
        macroContent.append("#displayComments($resource.getId(), "
            + "$enums.of(\"ccc.api.types.CommentStatus\", \"APPROVED\"))");

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
        final Comment approved =
            new Comment("testUser", "ok", page.getId(), new Date(), null);
        approved.setStatus(CommentStatus.APPROVED);
        approved.setEmail("test@civicuk.com");
        getComments().create(approved);

        final Comment spam =
            new Comment("spamUser", "spam", page.getId(), new Date(), null);
        spam.setStatus(CommentStatus.SPAM);
        spam.setEmail("spam@civicuk.com");
        getComments().create(spam);

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue("Should show approved comment body"
            ,  pContent.indexOf("ok") != -1);
        assertTrue("Should not show spam comment body"
            ,  pContent.indexOf("spam") == -1);

    }

    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testOptionalImage() throws IOException {
        // ARRANGE
        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n");
        macroContent.append("#optionalImage(\"ok\")\n");
        macroContent.append("#optionalImage(\"fail\")\n");

        final Folder folder = tempFolder();

        final Template t = new Template();
        t.setName(new ResourceName("template"));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(macroContent.toString());
        t.setDefinition("<fields>"
            + "<field name=\"fail\" type=\"image\"/>"
            + "<field name=\"ok\" type=\"image\"/>"
            + "</fields>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());
        getCommands().lock(page.getId());
        final Set<Paragraph> paras = page.getParagraphs();

        final Paragraph fail =
            Paragraph.fromText("fail", "d10ea9ad-55ca-45b6-9238-b03f10ddd979");
        final Paragraph ok =
            Paragraph.fromText("ok", folder.getId().toString());
        paras.add(fail);
        paras.add(ok);

        page.setParagraphs(paras);
        getPages().update(page.getId(), page);

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue("Should show path of the image"
            ,  pContent.indexOf(folder.getAbsolutePath()) != -1);
        assertTrue("Should not show fail"
            ,  pContent.indexOf("fail") == -1);

    }


    /**
     * Test.
     * @throws IOException Exception.
     */
    public void testCreateIndexMacro() throws IOException {

        // ARRANGE
        final Folder folder = tempFolder();

        final StringBuffer macroContent = new StringBuffer();
        macroContent.append(
            readFile("../application-ear/templates/default.vm"));
        macroContent.append("\n");
        macroContent.append("#set($id = $uuid.fromString(\""
            +folder.getId()+"\"))");
        macroContent.append("\n\n #createIndex($id)");

        getCommands().lock(folder.getId());
        folder.setTitle("first");
        getCommands().updateMetadata(folder.getId(), folder);
        getCommands().publish(folder.getId());
        getCommands().includeInMainMenu(folder.getId());

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
        getCommands().publish(page.getId());
        getCommands().updateMetadata(page.getId(), page);

        final Page page2 = tempPage(folder.getId(), template.getId());
        getCommands().lock(page2.getId());
        page2.setTitle("third");
        getCommands().publish(page2.getId());
        getCommands().updateMetadata(page2.getId(), page2);

        // ACT
        final String pContent = getBrowser().previewContent(page, false);

        // ASSERT
        assertTrue("Should not have 'first' entry"
            ,  pContent.indexOf("first") == -1);
        assertTrue("Should have 'second' entry"
            ,  pContent.indexOf("second") != -1);
        assertTrue("Should have 'third' entry"
            ,  pContent.indexOf("third") != -1);
        assertTrue("'second' should be after 'third'"
            ,  pContent.indexOf("second") >  pContent.indexOf("third"));
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
