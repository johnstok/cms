/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import ccc.api.exceptions.CycleDetectedException;
import ccc.api.exceptions.ResourceExistsException;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceOrder;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.commons.Exceptions;


/**
 * Tests for the {@link Folder} class.
 *
 * @author Civic Computing Ltd
 */
public final class FolderTest extends TestCase {

    /**
     * Test.
     */
    public void testEntrySorting() {

        // ARRANGE
        final Folder f = new Folder();
        f.add(new Page("k", _rm));
        f.add(new Page("a", _rm));
        f.add(new Page("z", _rm));

        // ACT

        // ASSERT
        assertEquals("k", f.getEntries(3, 1, "MANUAL").get(0).getTitle());
        assertEquals("a", f.getEntries(3, 1, "MANUAL").get(1).getTitle());
        assertEquals("z", f.getEntries(3, 1, "MANUAL").get(2).getTitle());

        assertEquals(
            "a", f.getEntries(3, 1, "NAME_ALPHANUM_ASC").get(0).getTitle());
        assertEquals(
            "k", f.getEntries(3, 1, "NAME_ALPHANUM_ASC").get(1).getTitle());
        assertEquals(
            "z", f.getEntries(3, 1, "NAME_ALPHANUM_ASC").get(2).getTitle());

    }

    /**
     * Test.
     */
    public void testEntryPaging() {

        // ARRANGE
        final Folder f = new Folder();
        f.add(new Page("a", _rm));
        f.add(new Page("b", _rm));
        f.add(new Page("c", _rm));
        f.add(new Page("d", _rm));
        f.add(new Page("e", _rm));

        // ACT

        // ASSERT
        assertEquals(1,   f.getEntries(1, 1, "MANUAL").size());
        assertEquals(5,   f.getEntries(9, 1, "MANUAL").size());
        assertEquals(1,   f.getEntries(4, 2, "MANUAL").size());
        assertEquals(0,   f.getEntries(5, 2, "MANUAL").size());
        assertEquals("a", f.getEntries(1, 1, "MANUAL").get(0).getTitle());
        assertEquals("e", f.getEntries(5, 1, "MANUAL").get(4).getTitle());

    }

    /**
     * Test.
     */
    public void testIsAncestorOrThis() {

        // ARRANGE
        final Folder a = new Folder();
        final Folder b = new Folder();
        final Folder c = new Folder();

        // ACT
        a.add(b);
        b.add(c);

        // ASSERT
        assertTrue(a.isAncestorOf(a));
        assertTrue(a.isAncestorOf(b));
        assertTrue(a.isAncestorOf(c));
        assertTrue(b.isAncestorOf(c));
        assertFalse(b.isAncestorOf(a));
        assertFalse(c.isAncestorOf(a));
    }

    /**
     * Test.
     */
    public void testDirectCircularDependency() {

        // ARRANGE
        final Folder p = new Folder();
        final Folder c = new Folder();
        p.add(c);

        // ACT
        try {
            c.add(p);
            fail();

        // ASSERT
        } catch (final CycleDetectedException e) {
            Exceptions.swallow(e);
        }
    }

    /**
     * Test.
     */
    public void testIndirectCircularDependency() {

        // ARRANGE
        final Folder a = new Folder();
        final Folder b = new Folder();
        final Folder c = new Folder();
        a.add(b);
        b.add(c);

        // ACT
        try {
            c.add(a);
            fail();

            // ASSERT
        } catch (final CycleDetectedException e) {
            Exceptions.swallow(e);
        }
    }

    /**
     * Test.
     */
    public void testCantAddFolderToSelf() {

        // ARRANGE
        final Folder a = new Folder();

        // ACT
        try {
            a.add(a);
            fail();

            // ASSERT
        } catch (final CycleDetectedException e) {
            Exceptions.swallow(e);
        }
    }

    /**
     * Test.
     */
    public void testChangeSortOrder() {

        // ARRANGE
        final Folder f = new Folder();
        assertEquals(ResourceOrder.MANUAL, f.getSortOrder());

        // ACT
        f.setSortOrder(ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, f.getSortOrder());

    }

    /**
     * Test.
     */
    public void testCreateFolderWithTitle() {

        // ACT
        final Folder actual = new Folder("foo");

        // ASSERT
        assertEquals("foo", actual.getTitle());
        assertEquals(new ResourceName("foo"), actual.getName());
    }

    /**
     * Test.
     */
    public void testFolders() {

            // ARRANGE
            final Folder f = new Folder("f");
            final Folder p = new Folder("p");
            final Folder q = new Folder("q");
            final Template r =
                new Template(
                    "r",
                    "desc",
                    "body",
                    "<fields/>",
                    MimeType.HTML,
                    _rm);
            f.add(p);
            f.add(q);
            f.add(r);

            // ACT
            final List<Folder> actual = f.getFolders();

            // ASSERT
            assertEquals(2, actual.size());
            assertSame(p, actual.get(0));
            assertSame(q, actual.get(1));
    }

    /**
     * Test.
     */
    public void testPages() {

            // ARRANGE
            final Folder f = new Folder("f");
            final Page p = new Page("p", _rm);
            final Page q = new Page("q", _rm);
            final Template r =
                new Template(
                    "r",
                    "desc",
                    "body",
                    "<fields/>",
                    MimeType.HTML,
                    _rm);
            f.add(p);
            f.add(q);
            f.add(r);

            // ACT
            final List<Page> actual = f.getPages();

            // ASSERT
            assertEquals(2, actual.size());
            assertSame(p, actual.get(0));
            assertSame(q, actual.get(1));
    }

    /**
     * Test.
     */
    public void testPagesRespectSortOrder() {

            // ARRANGE
            final Folder f = new Folder("f");
            final Page x = new Page("x", _rm);
            final Page a = new Page("a", _rm);
            final Template r =
                new Template(
                    "r",
                    "desc",
                    "body",
                    "<fields/>",
                    MimeType.HTML,
                    _rm);
            f.add(x);
            f.add(a);
            f.add(r);

            f.setSortOrder(ResourceOrder.NAME_ALPHANUM_ASC);

            // ACT
            final List<Page> actual = f.getPages();

            // ASSERT
            assertEquals(2, actual.size());
            assertSame(a, actual.get(0));
            assertSame(x, actual.get(1));
    }

    /**
     * Test.
     */
    public void testFirstPage() {

        // ARRANGE
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Page cd = new Page("cd", _rm);
        pages.add(ab);
        pages.add(cd);

        // ACT
        final Page firstChild = pages.getFirstPage();

        // ASSERT
        assertEquals(ab, firstChild);
    }

    /**
     * Test.
     */
    public void testHasPages() {

        // ARRANGE
        final Folder noPages = new Folder("content");
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Page cd = new Page("cd", _rm);
        noPages.add(pages);
        pages.add(ab);
        pages.add(cd);

        // ACT
        final boolean hasPages = pages.hasPages();
        final boolean hasNoPages = !noPages.hasPages();

        // ASSERT
        assertTrue("hasPages should be true", hasPages);
        assertTrue("hasNoPages should be true", hasNoPages);
    }

    /**
     * Test.
     */
    public void testTypedEntries() {

        // ARRANGE
        final Folder content = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Page cd = new Page("cd", _rm);
        content.add(cd);
        content.add(ab);

        // ACT
        final List<Page> pages = content.getEntries(Page.class);

        // ASSERT
        assertEquals(2, pages.size());
        assertEquals(content.getEntries(), pages);
    }

    /**
     * Test.
     */
    public void testFolderCount() {

        // ARRANGE
        final Folder content = new Folder("content");
        final Folder ab = new Folder("ab");
        final Page cd = new Page("cd", _rm);
        content.add(cd);
        content.add(ab);

        // ACT
        final int folderCount = content.getFolderCount();

        // ASSERT
        assertEquals(1, folderCount);
    }

    /**
     * Test.
     */
    public void testNullContentCannotBeAddedToFolders() {

        // ARRANGE
        final Folder f = new Folder("foo");

        // ACT
        try {
            f.add(null);
            fail("The add() method should reject a NULL resource.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testFolderTypeIsFolder() {

        // ACT
        final Resource resource = new Folder("foo");

        // ASSERT
        assertEquals(ResourceType.FOLDER, resource.getType());
    }

    /**
     * Test.
     */
    public void testResourceCanCastToFolder() {

        // ACT
        final Resource resource = new Folder("foo");

        // ASSERT
        assertEquals(Folder.class, resource.as(Folder.class).getClass());
    }

    /**
     * Test.
     */
    public void testEmptyFolderHasSizeZero() {

        // ACT
        final int size = new Folder("foo").size();

        // ASSERT
        assertEquals(0, size);
    }

    /**
     * Test.
     */
    public void testAddPageToFolder() {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Page page = new Page("Name", _rm);

        // ACT
        folder.add(page);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(page), folder.getEntries());
        assertEquals(folder, page.getParent());
    }

    /**
     * Test.
     */
    public void testRemovePageFromFolder() {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Page page = new Page("Name", _rm);
        folder.add(page);

        // ACT
        folder.remove(page);

        // ASSERT
        assertEquals(0, folder.size());
        assertEquals(Collections.emptyList(), folder.getEntries());
        assertNull("Should be null.", page.getParent());
    }

    /**
     * Test.
     */
    public void testAddFolderToFolder() {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Folder entry = new Folder("bar");

        // ACT
        folder.add(entry);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(entry), folder.getEntries());
        assertEquals(folder, entry.getParent());
    }

    /**
     * Test.
     */
    public void testFolderEntriesCollectionIsUnmodifiable() {

        // ARRANGE
        final Folder foo = new Folder("foo");

        // ACT
        foo.getEntries().add(new Page("bar", _rm));

         // ASSERT
        assertEquals(0, foo.getEntries().size());
    }

    /**
     * Test.
     */
    public void testFindEntryByUrl() {

        // ARRANGE
        final Folder foo = new Folder("foo");
        final Folder bar = new Folder("bar");
        foo.add(bar);

        // ACT
        final Resource expected = foo.getEntryWithName2(new ResourceName("bar"));

        // ASSERT
        assertSame(expected, bar);
    }

    /**
     * Test.
     */
    public void testNavigateToContent() {

        // ARRANGE
        final Folder content = new Folder("content");
        final Folder ab = new Folder("ab");
        final Page cd = new Page("cd", _rm);
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/ab/cd");

        // ACT
        final Page expectedContent = content.navigateTo(path).as(Page.class);

        // ASSERT
        assertSame(cd, expectedContent);
    }

    /**
     * Test.
     */
    public void testNavigateToFolder() {

        // ARRANGE
        final Folder content = new Folder("content");
        final Folder ab = new Folder("ab");
        final Folder cd = new Folder("cd");
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/ab/cd");

        // ACT
        final Folder expectedFolder = content.navigateTo(path).as(Folder.class);

        // ASSERT
        assertSame(cd, expectedFolder);
    }

    /**
     * Test.
     */
    public void testNavigateToEmptyPath() {

        // ARRANGE
        final Folder content = new Folder("content");
        final Folder ab = new Folder("ab");
        final Folder cd = new Folder("cd");
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("");

        // ACT
        final Folder expectedFolder = content.navigateTo(path).as(Folder.class);

        // ASSERT
        assertSame(content, expectedFolder);
    }

    /**
     * Test.
     */
    public void testHasEntryWithName() {

        // ARRANGE
        final Page p = new Page("page", _rm);
        final Folder f = new Folder("folder");
        f.add(p);

        // ACT
        final boolean expectTrue =
            f.hasEntryWithName(new ResourceName("page"));
        final boolean expectFalse =
            f.hasEntryWithName(new ResourceName("missing"));

        // ASSERT
        assertTrue(
            "folder.hasEntryWithName() should return true.",
            expectTrue);
        assertFalse(
            "folder.hasEntryWithName() should return false.",
            expectFalse);
    }

    /**
     * Test.
     */
    public void testAddRejectsResourcesWithExistingNames() {

        // ARRANGE
        final Page p = new Page("page", _rm);
        final Folder f = new Folder("folder");
        f.add(p);

        // ACT
        try {
            f.add(new Page("page", _rm));
            fail("Resources with existing names should be rejected.");

        // ASSERT
        } catch (final ResourceExistsException e) {
            assertEquals(
                "Folder already contains a resource with name 'page'.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testHasAliases() {

        // ARRANGE
        final Folder noAliases = new Folder("content");
        final Folder withAliases = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Alias cd = new Alias("cd", ab);
        noAliases.add(ab);
        withAliases.add(cd);

        // ACT
        final boolean hasAliases = withAliases.hasAliases();
        final boolean hasNoAliases = !noAliases.hasAliases();

        // ASSERT
        assertTrue("hasAliases should be true", hasAliases);
        assertTrue("hasNoAliases should be true", hasNoAliases);
    }

    /**
     * Test.
     */
    public void testFirstAlias() {

        // ARRANGE
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Alias cd = new Alias("cd", ab);
        final Alias de = new Alias("de", ab);
        pages.add(ab);
        pages.add(cd);
        pages.add(de);

        // ACT
        final Alias firstChild = pages.getFirstAlias();

        // ASSERT
        assertEquals(cd, firstChild);
    }


    /**
     * Test.
     */
    public void testIndexPage() {
        // ARRANGE
        final Folder f = new Folder("testFolder");
        final Page baz = new Page("baz", _rm);
        final Page foo = new Page("foo", _rm);

        f.add(baz);
        f.add(foo);

        // ACT
        f.setIndexPage(foo);

        // ASSERT
        assertEquals(foo, f.getIndexPage());
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
