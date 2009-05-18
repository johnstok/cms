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
import java.util.List;

import junit.framework.TestCase;
import ccc.api.MimeType;
import ccc.api.ResourceType;


/**
 * Tests for the {@link Folder} class.
 *
 * @author Civic Computing Ltd
 */
public final class FolderTest extends TestCase {

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
     * @throws ResourceExistsException
     */
    public void testFolders() throws ResourceExistsException {

            // ARRANGE
            final Folder f = new Folder("f");
            final Folder p = new Folder("p");
            final Folder q = new Folder("q");
            final Template r =
                new Template("r", "desc", "body", "<fields/>", MimeType.HTML);
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
     * @throws ResourceExistsException
     */
    public void testPages() throws ResourceExistsException {

            // ARRANGE
            final Folder f = new Folder("f");
            final Page p = new Page("p");
            final Page q = new Page("q");
            final Template r =
                new Template("r", "desc", "body", "<fields/>", MimeType.HTML);
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
     * @throws ResourceExistsException
     */
    public void testFirstPage() throws ResourceExistsException {

        // ARRANGE
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab");
        final Page cd = new Page("cd");
        pages.add(ab);
        pages.add(cd);

        // ACT
        final Page firstChild = pages.firstPage();

        // ASSERT
        assertEquals(ab, firstChild);
    }

    /**
     * Test.
     * @throws ResourceExistsException
     */
    public void testHasPages() throws ResourceExistsException {

        // ARRANGE
        final Folder noPages = new Folder("content");
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab");
        final Page cd = new Page("cd");
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
     * @throws ResourceExistsException
     */
    public void testTypedEntries() throws ResourceExistsException {

        // ARRANGE
        final Folder content = new Folder("content");
        final Page ab = new Page("ab");
        final Page cd = new Page("cd");
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
     * @throws ResourceExistsException
     */
    public void testFolderCount() throws ResourceExistsException {

        // ARRANGE
        final Folder content = new Folder("content");
        final Folder ab = new Folder("ab");
        final Page cd = new Page("cd");
        content.add(cd);
        content.add(ab);

        // ACT
        final int folderCount = content.folderCount();

        // ASSERT
        assertEquals(1, folderCount);
    }

    /**
     * Test.
     * @throws ResourceExistsException
     */
    public void testNullContentCannotBeAddedToFolders() throws ResourceExistsException {

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
     * @throws ResourceExistsException
     */
    public void testAddPageToFolder() throws ResourceExistsException {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Page page = new Page("Name");

        // ACT
        folder.add(page);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(page), folder.entries());
        assertEquals(folder, page.parent());
    }

    /**
     * Test.
     * @throws ResourceExistsException
     */
    public void testRemovePageFromFolder() throws ResourceExistsException {

        // ARRANGE
        final Folder folder = new Folder("foo");
        final Page page = new Page("Name");
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
     * @throws ResourceExistsException
     */
    public void testAddFolderToFolder() throws ResourceExistsException {

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
        foo.entries().add(new Page("bar"));

         // ASSERT
        assertEquals(0, foo.entries().size());
    }

    /**
     * Test.
     * @throws ResourceExistsException
     */
    public void testFindEntryByUrl() throws ResourceExistsException {

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
     * @throws ResourceExistsException
     */
    public void testNavigateToContent() throws ResourceExistsException {

        // ARRANGE
        final Folder content = new Folder("content");
        final Folder ab = new Folder("ab");
        final Page cd = new Page("cd");
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
     * @throws ResourceExistsException
     */
    public void testNavigateToFolder() throws ResourceExistsException {

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
     * @throws ResourceExistsException
     */
    public void testNavigateToEmptyPath() throws ResourceExistsException {

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
     * @throws ResourceExistsException
     */
    public void testHasEntryWithName() throws ResourceExistsException {

        // ARRANGE
        final Page p = new Page("page");
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
     * @throws ResourceExistsException
     */
    public void testAddRejectsResourcesWithExistingNames()
    throws ResourceExistsException {

        // ARRANGE
        final Page p = new Page("page");
        final Folder f = new Folder("folder");
        f.add(p);

        // ACT
        try {
            f.add(new Page("page"));
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
     * @throws ResourceExistsException
     */
    public void testHasAliases() throws ResourceExistsException {

        // ARRANGE
        final Folder noAliases = new Folder("content");
        final Folder withAliases = new Folder("content");
        final Page ab = new Page("ab");
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
     * @throws ResourceExistsException
     */
    public void testFirstAlias() throws ResourceExistsException {

        // ARRANGE
        final Folder pages = new Folder("content");
        final Page ab = new Page("ab");
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
     * @throws ResourceExistsException
     */
    public void testReorderWithOriginalOrder() throws ResourceExistsException {

        // ARRANGE
        final Folder f = new Folder("testFolder");
        final Page foo = new Page("foo");
        final Page bar = new Page("bar");
        final Page baz = new Page("baz");
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
     * @throws ResourceExistsException
     */
    public void testReorderWithNewOrder() throws ResourceExistsException {

        // ARRANGE
        final Folder f = new Folder("testFolder");
        final Page foo = new Page("foo");
        final Page bar = new Page("bar");
        final Page baz = new Page("baz");
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

}
