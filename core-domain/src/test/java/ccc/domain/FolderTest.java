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
    public void testEntryReferencesAccessor() {

        // ARRANGE
        final Folder content = new Folder(new ResourceName("content"));
        final Folder ab = new Folder(new ResourceName("ab"));
        final Content cd = new Content(new ResourceName("cd"));
        content.add(cd);
        content.add(ab);

        // ACT
        final List<ResourceRef> resourceRefs = content.entryReferences();

        // ASSERT
        assertEquals(2, resourceRefs.size());
        assertEquals("cd", resourceRefs.get(0).name().toString());
        assertEquals(cd.id(), resourceRefs.get(0).id());
        assertEquals("ab", resourceRefs.get(1).name().toString());
        assertEquals(ab.id(), resourceRefs.get(1).id());
    }

    /**
     * Test.
     */
    public void testToJsonWithNoChildren() {

        // ARRANGE
        final Folder folder = new Folder(new ResourceName("foo"));

        // ACT
        final String json = folder.toJSON();

        // ASSERT
        assertEquals("{\"name\": \"foo\",\"entries\": []}", json);
    }

    /**
     * Test.
     */
    public void testToJsonWithChildren() {

        // ARRANGE
        final Folder parent = new Folder(new ResourceName("foo"));
        final Folder child = new Folder(new ResourceName("bar"));
        parent.add(child);
        // ACT
        final String json = parent.toJSON();

        // ASSERT
        assertEquals(
            "{" +
            "\"name\": \"foo\"," +
            "\"entries\": [" +
                "{\"name\": \"bar\"," +
                "\"id\": \""+child.id().toString()+"\"," +
                "\"type\": \"FOLDER\"}]}", json);
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
        assertEquals(Folder.class, resource.asFolder().getClass());
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
    public void testAddContentToFolder() {

        // ARRANGE
        final Folder folder = new Folder(new ResourceName("foo"));
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
        final Folder folder = new Folder(new ResourceName("foo"));
        final Folder entry = new Folder(new ResourceName("bar"));

        // ACT
        folder.add(entry);

        // ASSERT
        assertEquals(1, folder.size());
        assertEquals(Collections.singletonList(entry), folder.entries());
    }

    public void testFolderEntriesCollectionIsUnmodifiable() {

        // ARRANGE
        final Folder foo = new Folder(new ResourceName("foo"));
        // ACT
        try {
            foo.entries().add(new Content(new ResourceName("bar")));
            fail("A folder's entries collection should be unmodifiable.");

         // ASSERT
        } catch (final UnsupportedOperationException e) {
            ; /* TODO: Add an assert for the error message? */
        }
    }

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

    public void testNavigateToContent() {

        // ARRANGE
        final Folder content = new Folder(new ResourceName("content"));
        final Folder ab = new Folder(new ResourceName("ab"));
        final Content cd = new Content(new ResourceName("cd"));
        ab.add(cd);
        content.add(ab);
        final ResourcePath path = new ResourcePath("/ab/cd/");

        // ACT
        final Content expectedContent = content.navigateTo(path);

        // ASSERT
        assertSame(cd, expectedContent);
    }

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
}
