/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.tests.acceptance;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import ccc.api.core.ACL;
import ccc.api.core.Alias;
import ccc.api.core.File;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;


/**
 * Tests for the content servlet.
 *
 * @author Civic Computing Ltd.
 */
public class ContentServletAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     */
    public void testRequestForSecurePageReturnsPage() {

        // ARRANGE
        final Resource simple =
            getCommands().resourceForPath("/assets/templates/simple");

        final ACL.Entry aclEntry = new ACL.Entry();
        aclEntry.setPrincipal(getUsers().retrieveCurrent().getId());
        aclEntry.setReadable(true);
        aclEntry.setWriteable(true);

        final Resource parent = getCommands().resourceForPath("");
        final Page p = tempPage(parent.getId(), simple.getId());
        getCommands().lock(p.getId());
        getCommands().publish(p.getId());
        getCommands().changeAcl(
            p.getId(), new ACL().setUsers(Collections.singleton(aclEntry)));

        // ACT
        final String content = getBrowser().get(p.getAbsolutePath());

        // ASSERT
        assertTrue(content.contains("test content"));
    }


    /**
     * Test.
     */
    public void testRequestForSecurePageRedirectsToLoginScreen() {

        // ARRANGE
        final User u = tempUser();

        final ACL.Entry aclEntry = new ACL.Entry();
        aclEntry.setPrincipal(u.getId());
        aclEntry.setReadable(true);
        aclEntry.setWriteable(true);

        final Resource parent = getCommands().resourceForPath("");
        final Page p = tempPage(parent.getId(), null);
        getCommands().lock(p.getId());
        getCommands().publish(p.getId());
        getCommands().changeAcl(
            p.getId(), new ACL().setUsers(Collections.singleton(aclEntry)));

        // ACT
        try {
            getBrowser().post(p);
            fail();

            // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(is302(e));
        }
    }


    /**
     * Test.
     */
    public void testWorkingCopiesCanBePreviewed() {

        // ARRANGE
        final Resource simple =
            getCommands().resourceForPath("/assets/templates/simple");
        final Resource parent = getCommands().resourceForPath("");
        final Page p = tempPage(parent.getId(), simple.getId());
        getCommands().lock(p.getId());
        final Page pWc = getPages().retrieveWorkingCopy(p.getId());
        pWc.setParagraphs(
            Collections.singleton(Paragraph.fromText("content", "meh")));
        getPages().updateWorkingCopy(p.getId(), pWc);

        // ACT
        final String content = getBrowser().previewContent(p, true);

        // ASSERT
        assertTrue(content.contains("meh"));
    }


    /**
     * Test.
     */
    public void testMissingLegacyPagesReturn404() {

        // ARRANGE

        // ACT
        final String content = getBrowser().get("/4444.html");


        // ASSERT
        assertTrue(content.contains("404"));
    }


    /**
     * Test.
     */
    public void testLegacyPagesCanBeAccessedById() {

        // ARRANGE
        final long id = System.currentTimeMillis();
        final Resource metadata = new Resource();
        metadata.setTitle("foo");
        metadata.setDescription("foo");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(
            Collections.singletonMap("legacyId", String.valueOf(id)));

        final Resource simple =
            getCommands().resourceForPath("/assets/templates/simple");

        final Resource parent = getCommands().resourceForPath("");
        final Page p = tempPage(parent.getId(), simple.getId());
        getCommands().lock(p.getId());
        getCommands().publish(p.getId());
        getCommands().updateMetadata(p.getId(), metadata);

        // ACT
        final String content = getBrowser().get("/"+id+".html");

        // ASSERT
        assertTrue(content.contains("test content"));
    }


    /**
     * Test.
     */
    public void testUnpublishedPagesCanBePreviewed() {

        // ARRANGE
        final Resource simple =
            getCommands().resourceForPath("/assets/templates/simple");

        final Resource parent = getCommands().resourceForPath("");
        final Page p = tempPage(parent.getId(), simple.getId());
        getCommands().lock(p.getId());

        // ACT
        final String content = getBrowser().previewContent(p, false);

        // ASSERT
        assertTrue(content.contains("test content"));
    }


    /**
     * Test.
     */
    public void testRequestForUnpublishedPageReturns404() {

        // ARRANGE
        final Resource parent = getCommands().resourceForPath("");
        final Page p = tempPage(parent.getId(), null);
        getCommands().lock(p.getId());

        // ACT
        final String content = getBrowser().get(p.getAbsolutePath());

        // ASSERT
        assertTrue(content.contains("404"));
    }


    /**
     * Test.
     */
    public void testRequestForAliasSendsRedirect() {

        // ARRANGE
        final ResourceName aliasName = new ResourceName(uid());
        final Resource welcome =
            getCommands().resourceForPath("/welcome");
        final Alias alias =
            new Alias(welcome.getParent(), aliasName, welcome.getId());
        final Alias as = getAliases().create(alias);
        getCommands().lock(as.getId());
        getCommands().publish(as.getId());

        // ACT
        final String content =
            getBrowser().get(new ResourcePath(aliasName).toString());

        // ASSERT
        assertTrue(content.contains("Welcome to Content Control!"));
    }


    /**
     * Test.
     */
    public void testRequestForPublishedPageReturnsContent() {

        // ARRANGE

        // ACT
        final String content = getBrowser().get("/welcome");

        // ASSERT
        assertTrue(content.contains("Welcome to Content Control!"));
    }


    /**
     * Test.
     */
    public void testPostRedirectReturns302() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("foo");
        metadata.setDescription("foo");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final Resource filesFolder =
            getCommands().resourceForPath("");
        final File script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "response.sendRedirect("
                        + "request.getContextPath()+'/welcome');"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        try {
            getBrowser().post(script);
            fail();

            // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(is302(e));
        }
    }


    /**
     * Test.
     */
    public void testForwardedRequestsForMissingResourcesReturn500() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("foo");
        metadata.setDescription("foo");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final Resource filesFolder =
            getCommands().resourceForPath("/files");
        final File script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "request.getRequestDispatcher(\"/doesnt/exist\")"
                    + ".forward(request,  response);"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        try {
            getBrowser().previewContent(script, false);
            fail();

            // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(is500(e));
        }
    }


    /**
     * Test.
     */
    public void testCommittedResponsesHandledDirectly() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("fail.js");
        metadata.setDescription("fail.js");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final Resource filesFolder =
            getCommands().resourceForPath("/files");
        final File script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "print('foo\\n'); response.flushBuffer(); throw 'foo';"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        final String content = getBrowser().previewContent(script, false);

        // ASSERT
        assertTrue(content.startsWith("foo\nAn error occurred: "));
    }


    /**
     * Test.
     */
    public void testUncommittedResponsesHandledByJsp() {

        // ARRANGE
        final Resource metadata = new Resource();
        metadata.setTitle("fail.js");
        metadata.setDescription("fail.js");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("executable", "true"));
        final String fName = UUID.randomUUID().toString();
        final Resource filesFolder =
            getCommands().resourceForPath("/files");
        final File script =
            getFiles().createTextFile(
                new File(
                    filesFolder.getId(),
                    fName,
                    MimeType.TEXT,
                    true,
                    "",
                    "throw 'foo';"));
        getCommands().lock(script.getId());
        getCommands().updateMetadata(script.getId(), metadata);
        getCommands().publish(script.getId());

        // ACT
        try {
            getBrowser().previewContent(script, false);
            fail();

        // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(is500(e));
        }
    }


    /**
     * Test.
     */
    public void testUrlEncodedParamsCorrectlyDecoded() {

        // ARRANGE
        final Resource folder = getCommands().resourceForPath("");

        final Template t = new Template();
        t.setName(new ResourceName(uid()));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$!request.getParameter(\"‡\")");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());
        getCommands().lock(page.getId());
        getCommands().publish(page.getId());

        // ACT
        final String pContent =
            getBrowser().get(
                page.getAbsolutePath(),
                Collections.singletonMap("‡", new String[] {"案例学习"}));

        // ASSERT
        assertEquals("案例学习", pContent);
    }


    /**
     * Test.
     */
    public void testUrlEncodedBodyCorrectlyDecoded() {

        // ARRANGE
        final Resource folder = getCommands().resourceForPath("");

        final Template t = new Template();
        t.setName(new ResourceName(uid()));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody("$!request.getParameter(\"‡\")");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());
        getCommands().lock(page.getId());
        getCommands().publish(page.getId());

        // ACT
        final String pContent =
            getBrowser().postUrlEncoded(
                page,
                Collections.singletonMap("‡", new String[] {"案例学习"}));

        // ASSERT
        assertEquals("案例学习", pContent);
    }


    /**
     * Test.
     */
    public void testMultipartBodyCorrectlyDecoded() {

        // ARRANGE
        final Resource folder = getCommands().resourceForPath("");

        final Template t = new Template();
        t.setName(new ResourceName(uid()));
        t.setParent(folder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setBody(
            "#set ($upload = $multipart.parse("
                + "$request.getCharacterEncoding(), "
                + "$request.getContentLength(), "
                + "$request.getContentType(), "
                + "$request.getInputStream()))"
            + "$upload.getString(\"‡\")");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);
        final Template template = getTemplates().create(t);

        final Page page = tempPage(folder.getId(), template.getId());
        getCommands().lock(page.getId());
        getCommands().publish(page.getId());

        // ACT
        final String pContent =
            getBrowser().postMultipart(
                page,
                Collections.singletonMap("‡", "案例学习"));

        // ASSERT
        assertEquals("案例学习", pContent);
    }


    /**
     * Test.
     */
    public void testDeletedResourceRetrive() {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setDefinition("<fields/>");
        t.setBody("#set($resources = $services.getResources()) "
            +"#set($id = $uuid.fromString(\"1fe95530-3e2c-41a8-a572-3fadbf8aa076\")) "
            +"ok"
            + "$resources.retrieve($id)");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary ts = getTemplates().create(t);

        final ResourceSummary f = tempFolder();
        name = UUID.randomUUID().toString();
        final Page page = new Page(f.getId(),
            name,
            ts.getId(),
            "title",
            "",
            true);
        final ResourceSummary ps = getPages().create(page);

        // ACT
        getCommands().lock(ps.getId());
        getCommands().lock(f.getId());
        getCommands().publish(ps.getId());
        getCommands().publish(f.getId());

        // ASSERT
        final String content =
            getBrowser().get(ps.getAbsolutePath());
        assertEquals(" ok$resources.retrieve($id)", content);

    }

    private boolean is500(final RuntimeException e) {
        return e.getMessage().startsWith("500: <!-- An error occurred: ");
    }


    private boolean is302(final RuntimeException e) {
        return e.getMessage().startsWith("302: ");
    }


    private boolean is404(final RuntimeException e) {
        return e.getMessage().startsWith("404: ");
    }
}
