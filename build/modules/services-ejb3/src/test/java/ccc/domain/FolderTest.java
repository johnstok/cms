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

package ccc.domain;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import ccc.api.exceptions.CycleDetectedException;
import ccc.api.exceptions.ResourceExistsException;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.commons.Exceptions;


/**
 * Tests for the {@link FolderEntity} class.
 *
 * @author Civic Computing Ltd
 */
public final class FolderTest extends TestCase {

    /**
     * Test.
     */
    public void testEntrySorting() {

        // ARRANGE
        final FolderEntity f = new FolderEntity();
        f.add(new PageEntity("k", _rm));
        f.add(new PageEntity("a", _rm));
        f.add(new PageEntity("z", _rm));

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
        final FolderEntity f = new FolderEntity();
        f.add(new PageEntity("a", _rm));
        f.add(new PageEntity("b", _rm));
        f.add(new PageEntity("c", _rm));
        f.add(new PageEntity("d", _rm));
        f.add(new PageEntity("e", _rm));

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
        final FolderEntity a = new FolderEntity();
        final FolderEntity b = new FolderEntity();
        final FolderEntity c = new FolderEntity();

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
        final FolderEntity p = new FolderEntity();
        final FolderEntity c = new FolderEntity();
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
        final FolderEntity a = new FolderEntity();
        final FolderEntity b = new FolderEntity();
        final FolderEntity c = new FolderEntity();
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
        final FolderEntity a = new FolderEntity();

        // ACT
        try {
            a.add(a);
            fail();

            // ASSERT
        } catch (final CycleDetectedException e) {
            Exceptions.swallow(e);
        }
    }

//    /**
//     * Test.
//     */
//    public void testChangeSortOrder() {
//
//        // ARRANGE
//        final FolderEntity f = new FolderEntity();
//        assertEquals(ResourceOrder.MANUAL, f.getSortOrder());
//
//        // ACT
//        f.setSortOrder(ResourceOrder.NAME_ALPHANUM_ASC);
//
//        // ASSERT
//        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, f.getSortOrder());
//
//    }

    /**
     * Test.
     */
    public void testCreateFolderWithTitle() {

        // ACT
        final FolderEntity actual = new FolderEntity("foo");

        // ASSERT
        assertEquals("foo", actual.getTitle());
        assertEquals(new ResourceName("foo"), actual.getName());
    }

    /**
     * Test.
     */
    public void testFolders() {

            // ARRANGE
            final FolderEntity f = new FolderEntity("f");
            final FolderEntity p = new FolderEntity("p");
            final FolderEntity q = new FolderEntity("q");
            final TemplateEntity r =
                new TemplateEntity(
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
            final List<FolderEntity> actual = f.getFolders();

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
            final FolderEntity f = new FolderEntity("f");
            final PageEntity p = new PageEntity("p", _rm);
            final PageEntity q = new PageEntity("q", _rm);
            final TemplateEntity r =
                new TemplateEntity(
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
            final List<PageEntity> actual = f.getPages();

            // ASSERT
            assertEquals(2, actual.size());
            assertSame(p, actual.get(0));
            assertSame(q, actual.get(1));
    }

//    /**
//     * Test.
//     */
//    public void testPagesRespectSortOrder() {
//
//            // ARRANGE
//            final FolderEntity f = new FolderEntity("f");
//            final PageEntity x = new PageEntity("x", _rm);
//            final PageEntity a = new PageEntity("a", _rm);
//            final TemplateEntity r =
//                new TemplateEntity(
//                    "r",
//                    "desc",
//                    "body",
//                    "<fields/>",
//                    MimeType.HTML,
//                    _rm);
//            f.add(x);
//            f.add(a);
//            f.add(r);
//
//            f.setSortOrder(ResourceOrder.NAME_ALPHANUM_ASC);
//
//            // ACT
//            final List<PageEntity> actual = f.getPages();
//
//            // ASSERT
//            assertEquals(2, actual.size());
//            assertSame(a, actual.get(0));
//            assertSame(x, actual.get(1));
//    }

    /**
     * Test.
     */
    public void testFirstPage() {

        // ARRANGE
        final FolderEntity pages = new FolderEntity("content");
        final PageEntity ab = new PageEntity("ab", _rm);
        final PageEntity cd = new PageEntity("cd", _rm);
        pages.add(ab);
        pages.add(cd);

        // ACT
        final PageEntity firstChild = pages.getFirstPage();

        // ASSERT
        assertEquals(ab, firstChild);
    }

    /**
     * Test.
     */
    public void testHasPages() {

        // ARRANGE
        final FolderEntity noPages = new FolderEntity("content");
        final FolderEntity pages = new FolderEntity("content");
        final PageEntity ab = new PageEntity("ab", _rm);
        final PageEntity cd = new PageEntity("cd", _rm);
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
        final FolderEntity content = new FolderEntity("content");
        final PageEntity ab = new PageEntity("ab", _rm);
        final PageEntity cd = new PageEntity("cd", _rm);
        content.add(cd);
        content.add(ab);

        // ACT
        final List<PageEntity> pages = content.getEntries(PageEntity.class);

        // ASSERT
        assertEquals(2, pages.size());
        assertEquals(content.getEntries(), pages);
    }

    /**
     * Test.
     */
    public void testFolderCount() {

        // ARRANGE
        final FolderEntity content = new FolderEntity("content");
        final FolderEntity ab = new FolderEntity("ab");
        final PageEntity cd = new PageEntity("cd", _rm);
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
        final FolderEntity f = new FolderEntity("foo");

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
        final ResourceEntity resource = new FolderEntity("foo");

        // ASSERT
        assertEquals(ResourceType.FOLDER, resource.getType());
    }

    /**
     * Test.
     */
    public void testResourceCanCastToFolder() {

        // ACT
        final ResourceEntity resource = new FolderEntity("foo");

        // ASSERT
        assertEquals(FolderEntity.class, resource.as(FolderEntity.class).getClass());
    }

    /**
     * Test.
     */
    public void testEmptyFolderHasSizeZero() {

        // ACT
        final int size = new FolderEntity("foo").size();

        // ASSERT
        assertEquals(0, size);
    }

    /**
     * Test.
     */
    public void testAddPageToFolder() {

        // ARRANGE
        final FolderEntity folder = new FolderEntity("foo");
        final PageEntity page = new PageEntity("Name", _rm);

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
        final FolderEntity folder = new FolderEntity("foo");
        final PageEntity page = new PageEntity("Name", _rm);
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
        final FolderEntity folder = new FolderEntity("foo");
        final FolderEntity entry = new FolderEntity("bar");

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
        final FolderEntity foo = new FolderEntity("foo");

        // ACT
        foo.getEntries().add(new PageEntity("bar", _rm));

         // ASSERT
        assertEquals(0, foo.getEntries().size());
    }

    /**
     * Test.
     */
    public void testFindEntryByUrl() {

        // ARRANGE
        final FolderEntity foo = new FolderEntity("foo");
        final FolderEntity bar = new FolderEntity("bar");
        foo.add(bar);

        // ACT
        final ResourceEntity expected = foo.getEntryWithName2(new ResourceName("bar"));

        // ASSERT
        assertSame(expected, bar);
    }

    /**
     * Test.
     */
    public void testNavigateToContent() {

        // ARRANGE
        final FolderEntity content = new FolderEntity("content");
        final FolderEntity ab = new FolderEntity("ab");
        final PageEntity cd = new PageEntity("cd", _rm);
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/ab/cd");

        // ACT
        final PageEntity expectedContent = content.navigateTo(path).as(PageEntity.class);

        // ASSERT
        assertSame(cd, expectedContent);
    }

    /**
     * Test.
     */
    public void testNavigateToFolder() {

        // ARRANGE
        final FolderEntity content = new FolderEntity("content");
        final FolderEntity ab = new FolderEntity("ab");
        final FolderEntity cd = new FolderEntity("cd");
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/ab/cd");

        // ACT
        final FolderEntity expectedFolder = content.navigateTo(path).as(FolderEntity.class);

        // ASSERT
        assertSame(cd, expectedFolder);
    }

    /**
     * Test.
     */
    public void testNavigateToEmptyPath() {

        // ARRANGE
        final FolderEntity content = new FolderEntity("content");
        final FolderEntity ab = new FolderEntity("ab");
        final FolderEntity cd = new FolderEntity("cd");
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("");

        // ACT
        final FolderEntity expectedFolder = content.navigateTo(path).as(FolderEntity.class);

        // ASSERT
        assertSame(content, expectedFolder);
    }

    /**
     * Test.
     */
    public void testHasEntryWithName() {

        // ARRANGE
        final PageEntity p = new PageEntity("page", _rm);
        final FolderEntity f = new FolderEntity("folder");
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
        final PageEntity p = new PageEntity("page", _rm);
        final FolderEntity f = new FolderEntity("folder");
        f.add(p);

        // ACT
        try {
            f.add(new PageEntity("page", _rm));
            fail("Resources with existing names should be rejected.");

        // ASSERT
        } catch (final ResourceExistsException e) {
            assertEquals(
                "Folder already contains a resource "
                    + p.getId()
                    + " with name 'page'.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testHasAliases() {

        // ARRANGE
        final FolderEntity noAliases = new FolderEntity("content");
        final FolderEntity withAliases = new FolderEntity("content");
        final PageEntity ab = new PageEntity("ab", _rm);
        final AliasEntity cd = new AliasEntity("cd", ab);
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
        final FolderEntity pages = new FolderEntity("content");
        final PageEntity ab = new PageEntity("ab", _rm);
        final AliasEntity cd = new AliasEntity("cd", ab);
        final AliasEntity de = new AliasEntity("de", ab);
        pages.add(ab);
        pages.add(cd);
        pages.add(de);

        // ACT
        final AliasEntity firstChild = pages.getFirstAlias();

        // ASSERT
        assertEquals(cd, firstChild);
    }


    /**
     * Test.
     */
    public void testIndexPage() {
        // ARRANGE
        final FolderEntity f = new FolderEntity("testFolder");
        final PageEntity baz = new PageEntity("baz", _rm);
        final PageEntity foo = new PageEntity("foo", _rm);

        f.add(baz);
        f.add(foo);

        // ACT
        f.setIndexPage(foo);

        // ASSERT
        assertEquals(foo, f.getIndexPage());
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), UserEntity.SYSTEM_USER, true, "Created.");
}
