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
import java.util.List;

import junit.framework.TestCase;


/**
 * Tests for the {@link Folder} class.
 *
 * @author Civic Computing Ltd
 */
public final class FolderTest extends TestCase {

    /**
     * Test.
     */
    public void testFirstPage() {

        // ARRANGE
        final Folder pages = new Folder(new ResourceName("content"));
        final Page ab = new Page(new ResourceName("ab"));
        final Page cd = new Page(new ResourceName("cd"));
        pages.add(ab);
        pages.add(cd);

        // ACT
        final Page firstChild = pages.firstPage();

        // ASSERT
        assertEquals(ab, firstChild);
    }

    /**
     * Test.
     */
    public void testHasPages() {

        // ARRANGE
        final Folder noPages = new Folder(new ResourceName("content"));
        final Folder pages = new Folder(new ResourceName("content"));
        final Page ab = new Page(new ResourceName("ab"));
        final Page cd = new Page(new ResourceName("cd"));
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
        final Folder content = new Folder(new ResourceName("content"));
        final Page ab = new Page(new ResourceName("ab"));
        final Page cd = new Page(new ResourceName("cd"));
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
     */
    public void testFolderCount() {

        // ARRANGE
        final Folder content = new Folder(new ResourceName("content"));
        final Folder ab = new Folder(new ResourceName("ab"));
        final Page cd = new Page(new ResourceName("cd"));
        content.add(cd);
        content.add(ab);

        // ACT
        final int folderCount = content.folderCount();

        // ASSERT
        assertEquals(1, folderCount);
    }

    /**
     * Test.
     */
    public void testEntryReferencesAccessor() {

        // ARRANGE
        final Folder content = new Folder(new ResourceName("content"));
        final Folder ab = new Folder(new ResourceName("ab"));
        final Page cd = new Page(new ResourceName("cd"));
        final Page ef = new Page(new ResourceName("ef"));
        content.add(cd);
        content.add(ab);
        ab.add(ef);

        // ACT
        final List<ResourceRef> resourceRefs = content.entryReferences();

        // ASSERT
        assertEquals(2, resourceRefs.size());

        assertEquals("cd", resourceRefs.get(0).name().toString());
        assertEquals(cd.id(), resourceRefs.get(0).id());
        assertEquals(
            null,
            resourceRefs.get(0).metadata().get("folder-count"));

        assertEquals("ab", resourceRefs.get(1).name().toString());
        assertEquals(ab.id(), resourceRefs.get(1).id());
        assertEquals("0", resourceRefs.get(1).metadata().get("folder-count"));
    }

    /**
     * Test.
     */
    public void testNullContentCannotBeAddedToFolders() {

        // ARRANGE
        final Folder f = new Folder(new ResourceName("foo"));

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
        final Resource resource = new Folder(new ResourceName("foo"));

        // ASSERT
        assertEquals(ResourceType.FOLDER, resource.type());
    }

    /**
     * Test.
     */
    public void testResourceCanCastToFolder() {

        // ACT
        final Resource resource = new Folder(new ResourceName("foo"));

        // ASSERT
        assertEquals(Folder.class, resource.as(Folder.class).getClass());
    }

    /**
     * Test.
     */
    public void testEmptyFolderHasSizeZero() {

        // ACT
        final int size = new Folder(new ResourceName("foo")).size();

        // ASSERT
        assertEquals(0, size);
    }

    /**
     * Test.
     */
    public void testAddPageToFolder() {

        // ARRANGE
        final Folder folder = new Folder(new ResourceName("foo"));
        final Page page = new Page(new ResourceName("Name"));

        // ACT
        folder.add(page);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(page), folder.entries());
        assertEquals(folder, page.parent());
    }

    /**
     * Test.
     */
    public void testRemovePageFromFolder() {

        // ARRANGE
        final Folder folder = new Folder(new ResourceName("foo"));
        final Page page = new Page(new ResourceName("Name"));
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
     */
    public void testAddFolderToFolder() {

        // ARRANGE
        final Folder folder = new Folder(new ResourceName("foo"));
        final Folder entry = new Folder(new ResourceName("bar"));

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
        final Folder foo = new Folder(new ResourceName("foo"));
        // ACT
        try {
            foo.entries().add(new Page(new ResourceName("bar")));
            fail("A folder's entries collection should be unmodifiable.");

         // ASSERT
        } catch (final UnsupportedOperationException e) {
            assertNull(e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testFindEntryByUrl() {

        // ARRANGE
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder bar = new Folder(new ResourceName("bar"));
        foo.add(bar);

        // ACT
        final Resource expected = foo.findEntryByName(new ResourceName("bar"));

        // ASSERT
        assertSame(expected, bar);
    }

    /**
     * Test.
     */
    public void testNavigateToContent() {

        // ARRANGE
        final Folder content = new Folder(new ResourceName("content"));
        final Folder ab = new Folder(new ResourceName("ab"));
        final Page cd = new Page(new ResourceName("cd"));
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/ab/cd/");

        // ACT
        final Page expectedContent = content.navigateTo(path);

        // ASSERT
        assertSame(cd, expectedContent);
    }

    /**
     * Test.
     */
    public void testNavigateToFolder() {

        // ARRANGE
        final Folder content = new Folder(new ResourceName("content"));
        final Folder ab = new Folder(new ResourceName("ab"));
        final Folder cd = new Folder(new ResourceName("cd"));
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/ab/cd/");

        // ACT
        final Folder expectedFolder = content.navigateTo(path);

        // ASSERT
        assertSame(cd, expectedFolder);
    }

    /**
     * Test.
     */
    public void testNavigateToEmptyPath() {

        // ARRANGE
        final Folder content = new Folder(new ResourceName("content"));
        final Folder ab = new Folder(new ResourceName("ab"));
        final Folder cd = new Folder(new ResourceName("cd"));
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/");

        // ACT
        final Folder expectedFolder = content.navigateTo(path);

        // ASSERT
        assertSame(content, expectedFolder);
    }

    /**
     * Test.
     */
    public void testHasEntryWithName() {

        // ARRANGE
        final Page p = new Page(new ResourceName("page"));
        final Folder f = new Folder(new ResourceName("folder"));
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
        final Page p = new Page(new ResourceName("page"));
        final Folder f = new Folder(new ResourceName("folder"));
        f.add(p);

        // ACT
        try {
            f.add(new Page(new ResourceName("page")));
            fail("Resources with existing names should be rejected.");

        // ASSERT
        } catch (final CCCException e) {
            assertEquals(
                "Folder already contains a resource with name 'page'.",
                e.getMessage());
        }
    }
}
