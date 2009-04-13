/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import static ccc.commons.Exceptions.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.content.response.CharEncodingHeader;
import ccc.content.response.ContentTypeHeader;
import ccc.content.response.DateHeader;
import ccc.content.response.FileBody;
import ccc.content.response.Header;
import ccc.content.response.IntHeader;
import ccc.content.response.PageBody;
import ccc.content.response.Response;
import ccc.content.response.StringHeader;
import ccc.domain.Alias;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourceOrder;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.StatefulReader;


/**
 * Tests for the {@link DefaultRenderer} class.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultRendererTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRenderWorkingCopyChecksRespectVisibility() {

        // ARRANGE
        final Renderer rr =
            new DefaultRenderer(_dm, _se, _sr, true);

        final Page p = new Page("foo");
        p.publish(new User("aaaa"));
        p.addParagraph(Paragraph.fromText("bar", "baz"));
        p.createWorkingCopy();
        p.workingCopy().set(
            "paragraphs",
            Collections.singletonList(
                Paragraph.fromText("some", "other value").createSnapshot()));

        // ACT
        rr.renderWorkingCopy(p, noParams);

        // ASSERT
        assertEquals(1, p.paragraphs().size());
        assertEquals("baz", p.paragraph("bar").text());
    }

    /**
     * Test.
     */
    public void testRenderWorkingCopy() {

        // ARRANGE
        final Page p = new Page("foo");
        p.addParagraph(Paragraph.fromText("bar", "baz"));
        p.createWorkingCopy();
        p.workingCopy().set(
            "paragraphs",
            Collections.singletonList(
                Paragraph.fromText("some", "other value").createSnapshot()));

        // ACT
        _renderer.renderWorkingCopy(p, noParams);

        // ASSERT
        assertEquals(1, p.paragraphs().size());
        assertEquals("other value", p.paragraph("some").text());
    }


    /**
     * Test.
     */
    public void testRenderHandlesUnsupportedResourceTypes() {

        // ARRANGE
        final Template t = new Template("template", "", "", "<fields/>");

        // ACT
        try {
            _renderer.render(t, noParams);
            fail();

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /**
     * Test.
     */
    public void testRenderResourceHandlesNullInput() {

        // ARRANGE

        // ACT
        try {
            _renderer.render(null, noParams);

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /**
     * Test.
     */
    public void testRenderHandlesNullInput() {

        // ARRANGE

        // ACT
        try {
            _renderer.render((Resource) null, noParams);

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /**
     * Test.
     * @throws MimeTypeParseException For invalids mime types.
     */
    public void testRenderPage() throws MimeTypeParseException {

        // ARRANGE
        final MimeType htmlMimeType = new MimeType("text", "html");
        final Page p = new Page("foo");

        // ACT
        final Response r = _renderer.render(p, noParams);

        // ASSERT
        final List<Header> expected = new ArrayList<Header>() {{
            add(new DateHeader("Expires", new Date(0)));
            add(new CharEncodingHeader(Charset.forName("UTF-8")));
            add(new ContentTypeHeader(htmlMimeType));
        }};
        assertEquals(expected, r.getHeaders());
        assertNotNull(r.getBody());
        assertEquals(PageBody.class, r.getBody().getClass());
    }


    /**
     * Test.
     * @throws MimeTypeParseException For invalid mime type.
     */
    public void testRenderFile() throws MimeTypeParseException {

        // ARRANGE
        final MimeType htmlMimeType = new MimeType("text", "html");
        final File f =
            new File(new ResourceName("meh"),
                "meh",
                "meh",
                new Data(),
                0,
                new MimeType("text", "html"));

        // ACT
        final Response r = _renderer.render(f, noParams);

        // ASSERT
        final List<Header> expected = new ArrayList<Header>() {{
            add(new StringHeader("Content-Description", "meh"));
            add(new StringHeader(
                "Content-Disposition", "inline; filename=\""+f.name()+"\""));
            add(new ContentTypeHeader(htmlMimeType));
            add(new DateHeader("Expires", new Date(0)));
            add(new IntHeader("Content-Length", 0));
        }};
        assertEquals(expected, r.getHeaders());


        assertNotNull(r.getBody());
        assertEquals(FileBody.class, r.getBody().getClass());
    }


    /**
     * Test.
     */
    public void testRenderEmptyFolderThrowsNotFoundException() {

        // ARRANGE
        final Folder f = new Folder("empty_folder");

        // ACT
        try {
            _renderer.render(f, noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /**
     * Test.
     */
    public void testRenderFolderRedirectsToFirstPage() {

        // ARRANGE
        final Folder f = new Folder("folder");
        f.sortOrder(ResourceOrder.NAME_ALPHANUM_ASC);
        final Page a = new Page("aaa");
        final Page z = new Page("zzz");
        z.publish(_user); f.add(z);
        a.publish(_user); f.add(a);

        // ACT
        try {
            _renderer.render(f, noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(a, e.getResource());
        }
    }

    /**
     * Test.
     */
    public void testRenderFolderIgnoresNonVisiblePages() {

        // ARRANGE
        final Folder f = new Folder("folder");
        final Page a = new Page("aaa");
        final Page z = new Page("zzz");
        z.publish(_user);
        f.add(a);
        f.add(z);

        // ACT
        try {
            _renderer.render(f, noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(z, e.getResource());
        }
    }


    /**
     * Test.
     */
    public void testRenderFolderIgnoresNonPageResources() {

        // ARRANGE
        final Folder root = new Folder("root");

        final Template a = new Template("a", "", "", "");
        final Folder b = new Folder("b");
        final File c = new File(new ResourceName("c"), "c", "c", new Data(), 0);
        final Alias d = new Alias("d", a);
        final Page e = new Page("page");
        a.publish(_user); root.add(a);
        b.publish(_user); root.add(b);
        c.publish(_user); root.add(c);
        d.publish(_user); root.add(d);
        e.publish(_user); root.add(e);

        // ACT
        try {
            _renderer.render(root, noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertEquals(e, rre.getResource());
        }
    }


    /**
     * Test.
     */
    public void testRenderAlias() {

        // ARRANGE
        final Page p = new Page("bar");
        final Alias a = new Alias("foo", p);

        // ACT
        try {
            _renderer.render(a, noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(p, e.getResource());
        }
    }


    /**
     * Test.
     */
    public void testRenderRespectsIsVisible() {

        // ARRANGE
        final Renderer rr =
            new DefaultRenderer(_dm, _se, _sr, true);
        final Page p = new Page("private page");

        // ACT
        try {
            rr.render(p, noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _renderer = new DefaultRenderer(_dm, _se, _sr, false);

    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _renderer = null;
    }


    private DefaultRenderer _renderer;
    private final DataManager _dm = Testing.dummy(DataManager.class);
    private final SearchEngine _se = Testing.dummy(SearchEngine.class);
    private final StatefulReader _sr = Testing.stub(StatefulReader.class);
    private final User _user = new User("fooo");
    private final Map<String, String[]> noParams =
        new HashMap<String, String[]>();
}
