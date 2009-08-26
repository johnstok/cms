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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import ccc.commons.Exceptions;
import ccc.types.MimeType;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;


/**
 * Tests for the {@link Folder} class.
 *
 * @author Civic Computing Ltd
 */
public final class FolderTest extends TestCase {

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testEntrySorting() throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder();
        f.add(new Page("k", _rm));
        f.add(new Page("a", _rm));
        f.add(new Page("z", _rm));

        // ACT

        // ASSERT
        assertEquals("k", f.entries(3, 1, "MANUAL").get(0).title());
        assertEquals("a", f.entries(3, 1, "MANUAL").get(1).title());
        assertEquals("z", f.entries(3, 1, "MANUAL").get(2).title());

        assertEquals("a", f.entries(3, 1, "NAME_ALPHANUM_ASC").get(0).title());
        assertEquals("k", f.entries(3, 1, "NAME_ALPHANUM_ASC").get(1).title());
        assertEquals("z", f.entries(3, 1, "NAME_ALPHANUM_ASC").get(2).title());

    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testEntryPaging() throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder();
        f.add(new Page("a", _rm));
        f.add(new Page("b", _rm));
        f.add(new Page("c", _rm));
        f.add(new Page("d", _rm));
        f.add(new Page("e", _rm));

        // ACT

        // ASSERT
        assertEquals(1,   f.entries(1, 1, "MANUAL").size());
        assertEquals(5,   f.entries(9, 1, "MANUAL").size());
        assertEquals(1,   f.entries(4, 2, "MANUAL").size());
        assertEquals(0,   f.entries(5, 2, "MANUAL").size());
        assertEquals("a", f.entries(1, 1, "MANUAL").get(0).title());
        assertEquals("e", f.entries(5, 1, "MANUAL").get(4).title());

    }

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testIsAncestorOrThis() throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testDirectCircularDependency() throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testIndirectCircularDependency() throws CccCheckedException {

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
     * @throws CccCheckedException If the test fails.
     */
    public void testCantAddFolderToSelf() throws CccCheckedException {

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
        assertEquals(ResourceOrder.MANUAL, f.sortOrder());

        // ACT
        f.sortOrder(ResourceOrder.NAME_ALPHANUM_ASC);

        // ASSERT
        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, f.sortOrder());

    }

    /**
     * Test.
     */
    public void testCreateFolderWithTitle() {

        // ACT
        final Folder actual = new Folder("foo");

        // ASSERT
        assertEquals("foo", actual.title());
        assertEquals(new ResourceName("foo"), actual.name());
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testFolders() throws CccCheckedException {

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
            final List<Folder> actual = f.folders();

            // ASSERT
            assertEquals(2, actual.size());
            assertSame(p, actual.get(0));
            assertSame(q, actual.get(1));
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testPages() throws CccCheckedException {

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
            final List<Page> actual = f.pages();

            // ASSERT
            assertEquals(2, actual.size());
            assertSame(p, actual.get(0));
            assertSame(q, actual.get(1));
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testPagesRespectSortOrder() throws CccCheckedException {

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

            f.sortOrder(ResourceOrder.NAME_ALPHANUM_ASC);

            // ACT
            final List<Page> actual = f.pages();

            // ASSERT
            assertEquals(2, actual.size());
            assertSame(a, actual.get(0));
            assertSame(x, actual.get(1));
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testFirstPage() throws CccCheckedException {

        // ARRANGE
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Page cd = new Page("cd", _rm);
        pages.add(ab);
        pages.add(cd);

        // ACT
        final Page firstChild = pages.firstPage();

        // ASSERT
        assertEquals(ab, firstChild);
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testHasPages() throws CccCheckedException {

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
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testTypedEntries() throws CccCheckedException {

        // ARRANGE
        final Folder content = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Page cd = new Page("cd", _rm);
        content.add(cd);
        content.add(ab);

        // ACT
        final List<Page> pages = content.entries(Page.class);

        // ASSERT
        assertEquals(2, pages.size());
        assertEquals(content.entries(), pages);
    }

    /**
     * Test.
     *
     * @@throws RemoteExceptionSupport If the test fails.
     */
    public void testFolderCount() throws CccCheckedException {

        // ARRANGE
        final Folder content = new Folder("content");
        final Folder ab = new Folder("ab");
        final Page cd = new Page("cd", _rm);
        content.add(cd);
        content.add(ab);

        // ACT
        final int folderCount = content.folderCount();

        // ASSERT
        assertEquals(1, folderCount);
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testNullContentCannotBeAddedToFolders()
    throws CccCheckedException {

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
        assertEquals(ResourceType.FOLDER, resource.type());
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
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testAddPageToFolder() throws CccCheckedException {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Page page = new Page("Name", _rm);

        // ACT
        folder.add(page);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(page), folder.entries());
        assertEquals(folder, page.parent());
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testRemovePageFromFolder() throws CccCheckedException {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Page page = new Page("Name", _rm);
        folder.add(page);

        // ACT
        folder.remove(page);

        // ASSERT
        assertEquals(0, folder.size());
        assertEquals(Collections.emptyList(), folder.entries());
        assertNull("Should be null.", page.parent());
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testAddFolderToFolder() throws CccCheckedException {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Folder entry = new Folder("bar");

        // ACT
        folder.add(entry);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(entry), folder.entries());
        assertEquals(folder, entry.parent());
    }

    /**
     * Test.
     */
    public void testFolderEntriesCollectionIsUnmodifiable() {

        // ARRANGE
        final Folder foo = new Folder("foo");

        // ACT
        foo.entries().add(new Page("bar", _rm));

         // ASSERT
        assertEquals(0, foo.entries().size());
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testFindEntryByUrl() throws CccCheckedException {

        // ARRANGE
        final Folder foo = new Folder("foo");
        final Folder bar = new Folder("bar");
        foo.add(bar);

        // ACT
        final Resource expected = foo.findEntryByName(new ResourceName("bar"));

        // ASSERT
        assertSame(expected, bar);
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testNavigateToContent() throws CccCheckedException {

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
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testNavigateToFolder() throws CccCheckedException {

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
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testNavigateToEmptyPath() throws CccCheckedException {

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
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testHasEntryWithName() throws CccCheckedException {

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
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testAddRejectsResourcesWithExistingNames()
    throws CccCheckedException {

        // ARRANGE
        final Page p = new Page("page", _rm);
        final Folder f = new Folder("folder");
        f.add(p);

        // ACT
        try {
            f.add(new Page("page", _rm));
            fail("Resources with existing names should be rejected.");

        // ASSERT
        } catch (final CccCheckedException e) {
            assertEquals(
                "Folder already contains a resource with name 'page'.",
                e.getMessage());
        }
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testHasAliases() throws CccCheckedException {

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
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testFirstAlias() throws CccCheckedException {

        // ARRANGE
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab", _rm);
        final Alias cd = new Alias("cd", ab);
        pages.add(ab);
        pages.add(cd);

        // ACT
        final Alias firstChild = pages.firstAlias();

        // ASSERT
        assertEquals(cd, firstChild);
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testReorderWithOriginalOrder() throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder("testFolder");
        final Page foo = new Page("foo", _rm);
        final Page bar = new Page("bar", _rm);
        final Page baz = new Page("baz", _rm);
        f.add(foo);
        f.add(bar);
        f.add(baz);

        // ACT
        f.reorder(f.entries());

        // ASSERT
        final List<Resource> pages = f.entries();
        assertEquals(foo, pages.get(0));
        assertEquals(bar, pages.get(1));
        assertEquals(baz, pages.get(2));
    }

    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testReorderWithNewOrder() throws CccCheckedException {

        // ARRANGE
        final Folder f = new Folder("testFolder");
        final Page foo = new Page("foo", _rm);
        final Page bar = new Page("bar", _rm);
        final Page baz = new Page("baz", _rm);
        f.add(foo);
        f.add(bar);
        f.add(baz);

        final List<Resource> newOrder = new ArrayList<Resource>();
        newOrder.add(bar);
        newOrder.add(baz);
        newOrder.add(foo);

        // ACT
        f.reorder(newOrder);

        // ASSERT
        final List<Resource> pages = f.entries();
        assertEquals(bar, pages.get(0));
        assertEquals(baz, pages.get(1));
        assertEquals(foo, pages.get(2));
    }


    /**
     * Test.
     *
     * @throws CccCheckedException If the test fails.
     */
    public void testIndexPage() throws CccCheckedException {
        // ARRANGE
        final Folder f = new Folder("testFolder");
        final Page baz = new Page("baz", _rm);
        final Page foo = new Page("foo", _rm);

        f.add(baz);
        f.add(foo);

        // ACT
        f.indexPage(foo);

        // ASSERT
        assertEquals(foo, f.indexPage());
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
