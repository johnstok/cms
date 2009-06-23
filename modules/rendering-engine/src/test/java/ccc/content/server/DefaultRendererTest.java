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

import junit.framework.TestCase;
import ccc.api.MimeType;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.commons.Testing;
import ccc.content.exceptions.NotFoundException;
import ccc.content.exceptions.RedirectRequiredException;
import ccc.content.response.CharEncodingHeader;
import ccc.content.response.ContentTypeHeader;
import ccc.content.response.DateHeader;
import ccc.content.response.DefaultRenderer;
import ccc.content.response.FileBody;
import ccc.content.response.Header;
import ccc.content.response.IntHeader;
import ccc.content.response.PageBody;
import ccc.content.response.Renderer;
import ccc.content.response.Response;
import ccc.content.response.StringHeader;
import ccc.domain.Alias;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.RemoteExceptionSupport;
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

        final Page p =
            new Page(
                new ResourceName("foo"),
                "foo",
                null,
                Paragraph.fromText("bar", "baz"));
        p.publish(new User("aaaa"));
        final PageDelta delta = p.workingCopy();
        delta.setParagraphs(
            Collections.singleton(Paragraph.fromText("some", "other value")));
        p.workingCopy(delta);

        // ACT
        rr.renderWorkingCopy(p, _noParams);

        // ASSERT
        assertEquals(1, p.paragraphs().size());
        assertEquals("baz", p.paragraph("bar").text());
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
                Paragraph.fromText("bar", "baz"));
        final PageDelta delta = p.workingCopy();
        delta.setParagraphs(
            Collections.singleton(Paragraph.fromText("some", "other value")));
        p.workingCopy(delta);

        // ACT
        _renderer.renderWorkingCopy(p, _noParams);

        // ASSERT
        assertEquals(1, p.paragraphs().size());
        assertEquals("other value", p.paragraph("some").text());
    }


    /**
     * Test.
     */
    public void testRenderHandlesUnsupportedResourceTypes() {

        // ARRANGE
        final Template t =
            new Template("template", "", "", "<fields/>", MimeType.HTML);

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
        final Page p = new Page("foo");

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
                MimeType.HTML);

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
        final Page a = new Page("aaa");
        final Page z = new Page("zzz");
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
        final Page a = new Page("aaa");
        final Page z = new Page("zzz");
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
        final Page a = new Page("aaa");
        final Page z = new Page("zzz");
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

        final Template a = new Template("a", "", "", "", MimeType.HTML);
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
        final Page p = new Page("bar");
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
        final Page p = new Page("private page");

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
}
