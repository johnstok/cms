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
package ccc.web;

import junit.framework.TestCase;
import ccc.api.SearchEngine;
import ccc.commons.Testing;


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
    public void testFoo() {
        // TODO Fix these tests.
    }

//    /**
//     * Test.
//     */
//    public void testRenderWorkingCopyChecksRespectVisibility() {
//
//        // ARRANGE
//        final Renderer rr =
//            new DefaultRenderer(true);
//
//        final Page p =
//            new Page(
//                new ResourceName("foo"),
//                "foo",
//                null,
//                _rm,
//                Paragraph.fromText("bar", "baz"));
//        p.publish(new User(new Username("aaaa"), "password"));
//        final PageDelta delta = p.getOrCreateWorkingCopy();
//        delta.setParagraphs(
//            Collections.singleton(Paragraph.fromText("some", "other value")));
//        p.setOrUpdateWorkingCopy(delta);
//
//        // ACT
//        rr.renderWorkingCopy(p, new Context());
//
//        // ASSERT
//        assertEquals(1, p.currentRevision().paragraphs().size());
//        assertEquals("baz", p.currentRevision().paragraph("bar").text());
//    }
//
//    /**
//     * Test.
//     */
//    public void testRenderWorkingCopy() {
//
//        // ARRANGE
//        final Page p =
//            new Page(
//                new ResourceName("foo"),
//                "foo",
//                null,
//                _rm,
//                Paragraph.fromText("bar", "baz"));
//        final PageDelta delta = p.getOrCreateWorkingCopy();
//        delta.setParagraphs(
//            Collections.singleton(Paragraph.fromText("some", "other value")));
//        p.setOrUpdateWorkingCopy(delta);
//        final Context ctxt = new Context();
//
//        // ACT
//        final Response r = _renderer.renderWorkingCopy(p, ctxt);
//
//        // ASSERT
//        assertEquals(PageBody.class, r.getBody().getClass());
//        final PageBody body = (PageBody) r.getBody();
//        assertEquals(1, ctxt.get("resource", PageSnapshot.class).getContent().size());
//        assertEquals(
//            "other value",
//            ctxt.get("resource", PageSnapshot.class).getContent().iterator().next().text());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRenderHandlesUnsupportedResourceTypes() {
//
//        // ARRANGE
//        final Template t =
//            new Template(
//                "template",
//                "",
//                "",
//                "<fields/>",
//                MimeType.HTML,
//                new RevisionMetadata(
//                    new Date(),
//                    User.SYSTEM_USER,
//                    true,
//                    "Created."));
//
//        // ACT
//        try {
//            _renderer.render(t, new Context());
//            fail();
//
//        // ASSERT
//        } catch (final NotFoundException e) {
//            swallow(e);
//        }
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRenderResourceHandlesNullInput() {
//
//        // ARRANGE
//
//        // ACT
//        try {
//            _renderer.render(null, new Context());
//
//        // ASSERT
//        } catch (final NotFoundException e) {
//            swallow(e);
//        }
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRenderHandlesNullInput() {
//
//        // ARRANGE
//
//        // ACT
//        try {
//            _renderer.render(null, new Context());
//
//        // ASSERT
//        } catch (final NotFoundException e) {
//            swallow(e);
//        }
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRenderPage() {
//
//        // ARRANGE
//        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
//
//        // ACT
//        final Response r = _renderer.render(p, new Context());
//
//        // ASSERT
//        final List<Header> expected = new ArrayList<Header>() {{
//            add(new CharEncodingHeader(Charset.forName("UTF-8")));
//            add(new ContentTypeHeader(MimeType.HTML));
//            add(new StringHeader("Pragma", "no-cache"));
//            add(new StringHeader("Cache-Control",
//                "no-store, must-revalidate, max-age=0"));
//            add(new DateHeader("Expires", new Date(0)));
//        }};
//        assertEquals(expected, new ArrayList<Header>(r.getHeaders().values()));
//        assertNotNull(r.getBody());
//        assertEquals(PageBody.class, r.getBody().getClass());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRenderFile() {
//
//        // ARRANGE
//        final File f =
//            new File(new ResourceName("meh"),
//                "meh",
//                "meh",
//                new Data(),
//                0,
//                MimeType.HTML,
//                new HashMap<String, String>(),
//                _rm);
//
//        // ACT
//        final Response r = _renderer.render(f, new Context());
//
//        // ASSERT
//        final List<Header> expected = new ArrayList<Header>() {{
//            add(new StringHeader("Content-Description", "meh"));
//            add(new StringHeader(
//                "Content-Disposition", "inline; filename=\""+f.name()+"\""));
//            add(new ContentTypeHeader(MimeType.HTML));
//            add(new IntHeader("Content-Length", 0));
//            add(new StringHeader("Pragma", "no-cache"));
//            add(new StringHeader("Cache-Control",
//                "no-store, must-revalidate, max-age=0"));
//            add(new DateHeader("Expires", new Date(0)));
//        }};
//        assertEquals(expected, new ArrayList<Header>(r.getHeaders().values()));
//
//
//        assertNotNull(r.getBody());
//        assertEquals(FileBody.class, r.getBody().getClass());
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRenderEmptyFolderThrowsNotFoundException() {
//
//        // ARRANGE
//        final Folder f = new Folder("empty_folder");
//
//        // ACT
//        try {
//            _renderer.render(f, new Context());
//            fail("Should throw exception");
//
//        // ASSERT
//        } catch (final NotFoundException e) {
//            swallow(e);
//        }
//    }
//
//
//    /**
//     * Test.
//     * @throws CccCheckedException If the test fails.
//     */
//    public void testRenderFolderRedirectsToFirstPage()
//    throws CccCheckedException {
//
//        // ARRANGE
//        final Folder f = new Folder("folder");
//        f.sortOrder(ResourceOrder.NAME_ALPHANUM_ASC);
//        final Page a = new Page(new ResourceName("aaa"), "aaa", null, _rm);
//        final Page z = new Page(new ResourceName("zzz"), "zzz", null, _rm);
//        z.publish(_user); f.add(z);
//        a.publish(_user); f.add(a);
//
//        // ACT
//        try {
//            _renderer.render(f, new Context());
//            fail("Should throw exception");
//
//        // ASSERT
//        } catch (final RedirectRequiredException e) {
//            assertEquals(
//                a.absolutePath().removeTop().toString(), e.getTarget());
//        }
//    }
//
//    /**
//     * Test.
//     * @throws CccCheckedException If the test fails.
//     */
//    public void testRenderFolderRedirectsToIndexPage()
//    throws CccCheckedException {
//
//        // ARRANGE
//        final Folder f = new Folder("folder");
//        f.sortOrder(ResourceOrder.NAME_ALPHANUM_ASC);
//        final Page a = new Page(new ResourceName("aaa"), "aaa", null, _rm);
//        final Page z = new Page(new ResourceName("zzz"), "zzz", null, _rm);
//        z.publish(_user); f.add(z);
//        a.publish(_user); f.add(a);
//        f.indexPage(z);
//
//        // ACT
//        try {
//            _renderer.render(f, new Context());
//            fail("Should throw exception");
//
//        // ASSERT
//        } catch (final RedirectRequiredException e) {
//            assertEquals(
//                z.absolutePath().removeTop().toString(), e.getTarget());
//        }
//    }
//
//
//    /**
//     * Test.
//     * @throws CccCheckedException If the test fails.
//     */
//    public void testRenderFolderIgnoresNonVisiblePages()
//    throws CccCheckedException {
//
//        // ARRANGE
//        final Folder f = new Folder("folder");
//        final Page a = new Page(new ResourceName("aaa"), "aaa", null, _rm);
//        final Page z = new Page(new ResourceName("zzz"), "zzz", null, _rm);
//        z.publish(_user);
//        f.add(a);
//        f.add(z);
//
//        // ACT
//        try {
//            _renderer.render(f, new Context());
//            fail("Should throw exception");
//
//        // ASSERT
//        } catch (final RedirectRequiredException e) {
//            assertEquals(
//                z.absolutePath().removeTop().toString(), e.getTarget());
//        }
//    }
//
//
//    /**
//     * Test.
//     * @throws CccCheckedException If the test fails.
//     */
//    public void testRenderFolderIgnoresNonPageResources()
//    throws CccCheckedException {
//
//        // ARRANGE
//        final Folder root = new Folder("root");
//
//        final Template a =
//            new Template(
//                "a",
//                "",
//                "",
//                "",
//                MimeType.HTML,
//                new RevisionMetadata(
//                    new Date(),
//                    User.SYSTEM_USER,
//                    true,
//                    "Created."));
//        final Folder b = new Folder("b");
//        final File c =
//            new File(
//                new ResourceName("c"),
//                "c",
//                "c",
//                new Data(),
//                0,
//                _rm);
//        final Alias d = new Alias("d", a);
//        final Page e = new Page(new ResourceName("page"), "page", null, _rm);
//        a.publish(_user); root.add(a);
//        b.publish(_user); root.add(b);
//        c.publish(_user); root.add(c);
//        d.publish(_user); root.add(d);
//        e.publish(_user); root.add(e);
//
//        // ACT
//        try {
//            _renderer.render(root, new Context());
//            fail("Should throw exception");
//
//        // ASSERT
//        } catch (final RedirectRequiredException rre) {
//            assertEquals(
//                e.absolutePath().removeTop().toString(), rre.getTarget());
//        }
//    }
//
//
//    /**
//     * Test.
//     * @throws CccCheckedException If the command fails.
//     */
//    public void testRenderAlias() throws CccCheckedException {
//
//        // ARRANGE
//        final Page p = new Page(new ResourceName("bar"), "bar", null, _rm);
//        final Alias a = new Alias("foo", p);
//
//        // ACT
//        try {
//            _renderer.render(a, new Context());
//            fail("Should throw exception");
//
//        // ASSERT
//        } catch (final RedirectRequiredException e) {
//            assertEquals(
//                p.absolutePath().removeTop().toString(), e.getTarget());
//        }
//    }
//
//
//    /**
//     * Test.
//     */
//    public void testRenderRespectsIsVisible() {
//
//        // ARRANGE
//        final Renderer rr =
//            new DefaultRenderer(true);
//        final Page p = new Page(new ResourceName("zzz"), "zzz", null, _rm);
//
//        // ACT
//        try {
//            rr.render(p, new Context());
//            fail("Should throw exception");
//
//        // ASSERT
//        } catch (final NotFoundException e) {
//            swallow(e);
//        }
//    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
//        _renderer = new DefaultRenderer(false);

    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
//        _renderer = null;
    }


//    private DefaultRenderer _renderer;
    private final SearchEngine _se = Testing.dummy(SearchEngine.class);
}
