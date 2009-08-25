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
package ccc.rendering.server;

import static ccc.commons.Exceptions.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import ccc.api.PageDelta;
import ccc.api.template.StatefulReader;
import ccc.commons.Testing;
import ccc.domain.Alias;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.ResourceOrder;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.rendering.CharEncodingHeader;
import ccc.rendering.ContentTypeHeader;
import ccc.rendering.DateHeader;
import ccc.rendering.FileBody;
import ccc.rendering.Header;
import ccc.rendering.IntHeader;
import ccc.rendering.NotFoundException;
import ccc.rendering.PageBody;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.Response;
import ccc.rendering.StringHeader;
import ccc.rendering.response.DefaultRenderer;
import ccc.rendering.response.Renderer;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.types.MimeType;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


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

        final Page p =
            new Page(
                new ResourceName("foo"),
                "foo",
                null,
                _rm,
                Paragraph.fromText("bar", "baz"));
        p.publish(new User("aaaa"));
        final PageDelta delta = p.workingCopy();
        delta.setParagraphs(
            Collections.singleton(Paragraph.fromText("some", "other value")));
        p.workingCopy(delta);

        // ACT
        rr.renderWorkingCopy(p, _noParams);

        // ASSERT
        assertEquals(1, p.currentRevision().paragraphs().size());
        assertEquals("baz", p.currentRevision().paragraph("bar").text());
    }

    /**
     * Test.
     */
    public void testRenderWorkingCopy() {

        // ARRANGE
        final Page p =
            new Page(
                new ResourceName("foo"),
                "foo",
                null,
                _rm,
                Paragraph.fromText("bar", "baz"));
        final PageDelta delta = p.workingCopy();
        delta.setParagraphs(
            Collections.singleton(Paragraph.fromText("some", "other value")));
        p.workingCopy(delta);

        // ACT
        final Response r = _renderer.renderWorkingCopy(p, _noParams);

        // ASSERT
        assertEquals(PageBody.class, r.getBody().getClass());
        final PageBody body = (PageBody) r.getBody();
        assertEquals(1, body.getPage().getContent().size());
        assertEquals(
            "other value",
            body.getPage().getContent().iterator().next().text());
    }


    /**
     * Test.
     */
    public void testRenderHandlesUnsupportedResourceTypes() {

        // ARRANGE
        final Template t =
            new Template(
                "template",
                "",
                "",
                "<fields/>",
                MimeType.HTML,
                new RevisionMetadata(
                    new Date(),
                    User.SYSTEM_USER,
                    true,
                    "Created."));

        // ACT
        try {
            _renderer.render(t, _noParams);
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
            _renderer.render(null, _noParams);

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
            _renderer.render((Resource) null, _noParams);

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /**
     * Test.
     */
    public void testRenderPage() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);

        // ACT
        final Response r = _renderer.render(p, _noParams);

        // ASSERT
        final List<Header> expected = new ArrayList<Header>() {{
            add(new CharEncodingHeader(Charset.forName("UTF-8")));
            add(new ContentTypeHeader(MimeType.HTML));
            add(new StringHeader("Pragma", "no-cache"));
            add(new StringHeader("Cache-Control",
                "no-store, must-revalidate, max-age=0"));
            add(new DateHeader("Expires", new Date(0)));
        }};
        assertEquals(expected, r.getHeaders());
        assertNotNull(r.getBody());
        assertEquals(PageBody.class, r.getBody().getClass());
    }


    /**
     * Test.
     */
    public void testRenderFile() {

        // ARRANGE
        final File f =
            new File(new ResourceName("meh"),
                "meh",
                "meh",
                new Data(),
                0,
                MimeType.HTML,
                new HashMap<String, String>(),
                _rm);

        // ACT
        final Response r = _renderer.render(f, _noParams);

        // ASSERT
        final List<Header> expected = new ArrayList<Header>() {{
            add(new StringHeader("Content-Description", "meh"));
            add(new StringHeader(
                "Content-Disposition", "inline; filename=\""+f.name()+"\""));
            add(new ContentTypeHeader(MimeType.HTML));
            add(new IntHeader("Content-Length", 0));
            add(new StringHeader("Pragma", "no-cache"));
            add(new StringHeader("Cache-Control",
                "no-store, must-revalidate, max-age=0"));
            add(new DateHeader("Expires", new Date(0)));
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
            _renderer.render(f, _noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testRenderFolderRedirectsToFirstPage()
    throws RemoteExceptionSupport {

        // ARRANGE
        final Folder f = new Folder("folder");
        f.sortOrder(ResourceOrder.NAME_ALPHANUM_ASC);
        final Page a = new Page(new ResourceName("aaa"), "aaa", null, _rm);
        final Page z = new Page(new ResourceName("zzz"), "zzz", null, _rm);
        z.publish(_user); f.add(z);
        a.publish(_user); f.add(a);

        // ACT
        try {
            _renderer.render(f, _noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(a, e.getResource());
        }
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testRenderFolderRedirectsToIndexPage()
    throws RemoteExceptionSupport {

        // ARRANGE
        final Folder f = new Folder("folder");
        f.sortOrder(ResourceOrder.NAME_ALPHANUM_ASC);
        final Page a = new Page(new ResourceName("aaa"), "aaa", null, _rm);
        final Page z = new Page(new ResourceName("zzz"), "zzz", null, _rm);
        z.publish(_user); f.add(z);
        a.publish(_user); f.add(a);
        f.indexPage(z);

        // ACT
        try {
            _renderer.render(f, _noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(z, e.getResource());
        }
    }


    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testRenderFolderIgnoresNonVisiblePages()
    throws RemoteExceptionSupport {

        // ARRANGE
        final Folder f = new Folder("folder");
        final Page a = new Page(new ResourceName("aaa"), "aaa", null, _rm);
        final Page z = new Page(new ResourceName("zzz"), "zzz", null, _rm);
        z.publish(_user);
        f.add(a);
        f.add(z);

        // ACT
        try {
            _renderer.render(f, _noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(z, e.getResource());
        }
    }


    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testRenderFolderIgnoresNonPageResources()
    throws RemoteExceptionSupport {

        // ARRANGE
        final Folder root = new Folder("root");

        final Template a =
            new Template(
                "a",
                "",
                "",
                "",
                MimeType.HTML,
                new RevisionMetadata(
                    new Date(),
                    User.SYSTEM_USER,
                    true,
                    "Created."));
        final Folder b = new Folder("b");
        final File c =
            new File(
                new ResourceName("c"),
                "c",
                "c",
                new Data(),
                0,
                _rm);
        final Alias d = new Alias("d", a);
        final Page e = new Page(new ResourceName("page"), "page", null, _rm);
        a.publish(_user); root.add(a);
        b.publish(_user); root.add(b);
        c.publish(_user); root.add(c);
        d.publish(_user); root.add(d);
        e.publish(_user); root.add(e);

        // ACT
        try {
            _renderer.render(root, _noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException rre) {
            assertEquals(e, rre.getResource());
        }
    }


    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testRenderAlias() throws RemoteExceptionSupport {

        // ARRANGE
        final Page p = new Page(new ResourceName("bar"), "bar", null, _rm);
        final Alias a = new Alias("foo", p);

        // ACT
        try {
            _renderer.render(a, _noParams);
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
        final Page p = new Page(new ResourceName("zzz"), "zzz", null, _rm);

        // ACT
        try {
            rr.render(p, _noParams);
            fail("Should throw exception");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _renderer = new DefaultRenderer(_dm, _se, _sr, false);

    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _renderer = null;
    }


    private DefaultRenderer _renderer;
    private final DataManager _dm = Testing.dummy(DataManager.class);
    private final SearchEngine _se = Testing.dummy(SearchEngine.class);
    private final StatefulReader _sr = Testing.stub(StatefulReader.class);
    private final User _user = new User("fooo");
    private final Map<String, String[]> _noParams =
        new HashMap<String, String[]>();
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
