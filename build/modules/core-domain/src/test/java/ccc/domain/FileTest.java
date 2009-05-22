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

import junit.framework.TestCase;
import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.api.MimeType;


/**
 * Tests for the {@link File} class.
 *
 * @author Civic Computing Ltd
 */
public class FileTest extends TestCase {

    /**
     * Test.
     */
    public void testImageFilesAreDetected() {

        // ARRANGE
        final File f = new File(
            new ResourceName("name"),
            "title",
            "desc",
            new Data(),
            0,
            new MimeType("image", "jpeg"));

        // ACT
        final boolean isImage = f.isImage();

        // ASSERT
        assertTrue(isImage);
    }

    /**
     * Test.
     */
    public void testNonImageFilesAreDetected() {

        // ARRANGE
        final File f = new File(
            new ResourceName("name"),
            "title",
            "desc",
            new Data(),
            0,
            new MimeType("text", "plain"));

        // ACT
        final boolean isImage = f.isImage();

        // ASSERT
        assertFalse(isImage);
    }

    /**
     * Test.
     */
    public void testSnapshot() {

        // ARRANGE
        final Data data = new Data();
        final File f =
            new File(new ResourceName("foo"),
                "foo",
                "desc",
                data,
                1,
                new MimeType("foo", "bar"));

        // ACT
        final FileDelta o = f.createSnapshot();

        // ASSERT
        assertEquals("foo", o.getTitle());
        assertEquals("desc", o.getDescription());
        assertEquals(new MimeType("foo", "bar"), o.getMimeType());
        assertEquals(1, o.getSize());
        assertEquals(new ID(data.id().toString()), o.getData());
    }

    /**
     * Test.
     */
    public void testDataPropertyCanBeUpdated() {

        // ARRANGE
        final Data newData = new Data();
        final File f =
            new File(new ResourceName("foo"),
                "foo",
                "desc",
                new Data(),
                1,
                new MimeType("foo", "bar"));

        // ACT
        f.data(newData);

        // ASSERT
        assertEquals(newData, f.data());
    }

    /**
     * Test.
     */
    public void testMimeTypeProperty() {

        // ARRANGE
        final File f =
            new File(new ResourceName("foo"),
                "foo",
                "desc",
                new Data(),
                1,
                new MimeType("foo", "bar"));

        // ACT
        final MimeType actual = f.mimeType();

        // ASSERT
        assertEquals(new MimeType("foo", "bar"), actual);
    }

    /**
     * Test.
     */
    public void testFileSizeProperty() {

        // ARRANGE
        final File f =
            new File(new ResourceName("foo"),
                "foo",
                "desc",
                new Data(),
                1);

        // ACT
        final long actual = f.size();

        // ASSERT
        assertEquals(1L, actual);
    }

    /**
     * Test.
     */
    public void testRejectMissingData() {

        // ARRANGE
        final Data data = null;

        // ACT
        try {
            new File(
                new ResourceName("file"), "title", "desc", data, 0);
            fail("The constructor should reject a NULL file data.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
