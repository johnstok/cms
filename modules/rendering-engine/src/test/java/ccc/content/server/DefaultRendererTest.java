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

import java.util.Collections;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.domain.Alias;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.DataManager;


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
            new DefaultRenderer(_dm, true);

        final Page p = new Page("foo");
        p.publish(new User("aaaa"));
        p.addParagraph(Paragraph.fromText("bar", "baz"));
        p.createWorkingCopy();
        p.workingCopy().set(
            "paragraphs",
            Collections.singletonList(
                Paragraph.fromText("some", "other value").createSnapshot()));

        // ACT
        rr.renderWorkingCopy(p);

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
        _renderer.renderWorkingCopy(p);

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
            _renderer.render(t);
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
            _renderer.render(null);

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
            _renderer.render((Resource) null);

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
        final Response r = _renderer.render(p);

        // ASSERT
        assertEquals(Long.valueOf(0), r.getExpiry());
        assertEquals("UTF-8", r.getCharSet());
        assertEquals("text/html", r.getMimeType());
        assertNotNull(r.getBody());
        assertEquals(PageBody.class, r.getBody().getClass());
    }


    /**
     * Test.
     * @throws MimeTypeParseException For invalid mime type.
     */
    public void testRenderFile() throws MimeTypeParseException {

        // ARRANGE
        final File f =
            new File(new ResourceName("meh"),
                "meh",
                "meh",
                new Data(),
                0,
                new MimeType("text", "html"));

        // ACT
        final Response r = _renderer.render(f);

        // ASSERT
        assertEquals("meh", r.getDescription());
        assertEquals("inline; filename=\""+f.name()+"\"", r.getDisposition());
        assertEquals("text/html", r.getMimeType());
        assertEquals(Long.valueOf(0), r.getExpiry());
        assertEquals(Long.valueOf(0), r.getLength());
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
            _renderer.render(f);
            fail("Should throw exception");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /**
     * Test.
     */
    public void testRenderFolderWithPagesThrowsRedirectException() {

        // ARRANGE
        final Folder f = new Folder("folder");
        final Page p = new Page("page");
        f.add(p);

        // ACT
        try {
            _renderer.render(f);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(p, e.getResource());
        }
    }


    /**
     * Test.
     */
    public void testRenderFolderWithAliasesThrowsRedirectException() {

        // ARRANGE
        final Folder f = new Folder("folder");
        final Page p = new Page("page");
        final Alias a = new Alias("alias", p);
        f.add(p);
        f.add(a);

        // ACT
        try {
            _renderer.render(f);
            fail("Should throw exception");

        // ASSERT
        } catch (final RedirectRequiredException e) {
            assertEquals(a, e.getResource());
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
            _renderer.render(a);
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
            new DefaultRenderer(_dm, true);
        final Page p = new Page("private page");

        // ACT
        try {
            rr.render(p);
            fail("Should throw exception");

        // ASSERT
        } catch (final NotFoundException e) {
            swallow(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _renderer = new DefaultRenderer(_dm, false);

    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _renderer = null;
    }


    private DefaultRenderer _renderer;
    private final DataManager _dm = Testing.dummy(DataManager.class);
}
