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
    public void testFolderTypeIsFolder() {

        // ACT
        Resource resource = new Folder(new ResourceName("foo"));

        // ASSERT
        assertEquals(ResourceType.FOLDER, resource.type());
    }

    /**
     * Test.
     */
    public void testResourceCanCastToFolder() {

        // ACT
        Resource resource = new Folder(new ResourceName("foo"));

        // ASSERT
        assertEquals(Folder.class, resource.asFolder().getClass());
    }

    /**
     * Test.
     */
    public void testEmptyFolderHasSizeZero() {

        // ACT
        int size = new Folder(new ResourceName("foo")).size();

        // ASSERT
        assertEquals(0, size);
    }

    /**
     * Test.
     */
    public void testAddContentToFolder() {

        // ARRANGE
        Folder folder = new Folder(new ResourceName("foo"));
        final Content content = new Content(new ResourceName("Name"));

        // ACT
        folder.add(content);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(content), folder.entries());
    }

    /**
     * Test.
     */
    public void testAddFolderToFolder() {

        // ARRANGE
        Folder folder = new Folder(new ResourceName("foo"));
        final Folder entry = new Folder(new ResourceName("bar"));

        // ACT
        folder.add(entry);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(entry), folder.entries());
    }

    public void testFolderEntriesCollectionIsUnmodifiable() {

        // ARRANGE
        Folder foo = new Folder(new ResourceName("foo"));
        // ACT
        try {
            foo.entries().add(new Content(new ResourceName("bar")));
            fail("A folder's entries collection should be unmodifiable.");

         // ASSERT
        } catch (UnsupportedOperationException e) {
            ; /* TODO: Add an assert for the error message? */
        }
    }

    public void testFindEntryByUrl() {

        // ARRANGE
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder bar = new Folder(new ResourceName("bar"));
        foo.add(bar);

        // ACT
        Resource expected = foo.findEntryByName(new ResourceName("bar"));

        // ASSERT
        assertSame(expected, bar);
    }

    public void testNavigateToContent() {

        // ARRANGE
        Folder content = new Folder(new ResourceName("content"));
        Folder ab = new Folder(new ResourceName("ab"));
        Content cd = new Content(new ResourceName("cd"));
        ab.add(cd);
        content.add(ab);
        ResourcePath path = new ResourcePath("/ab/cd");

        // ACT
        Content expectedContent = content.navigateTo(path);

        // ASSERT
        assertSame(cd, expectedContent);
    }

    public void testNavigateToFolder() {

        // ARRANGE
        Folder content = new Folder(new ResourceName("content"));
        Folder ab = new Folder(new ResourceName("ab"));
        Folder cd = new Folder(new ResourceName("cd"));
        ab.add(cd);
        content.add(ab);
        ResourcePath path = new ResourcePath("/ab/cd");

        // ACT
        Folder expectedFolder = content.navigateTo(path);

        // ASSERT
        assertSame(cd, expectedFolder);
    }
}
