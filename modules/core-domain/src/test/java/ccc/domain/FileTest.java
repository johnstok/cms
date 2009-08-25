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

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import ccc.api.FileDelta;
import ccc.api.MimeType;
import ccc.entities.ResourceName;
import ccc.types.ID;


/**
 * Tests for the {@link File} class.
 *
 * @author Civic Computing Ltd
 */
public class FileTest extends TestCase {

    /**
     * Test.
     */
    public void testGetProperty() {

        // ACT
        final File f = new File(
            new ResourceName("name"),
            "title",
            "",
            new Data(),
            0,
            MimeType.HTML,
            new HashMap<String, String>(){{
                put(File.CHARSET, "UTF-8");
            }},
            _rm);


        // ASSERT
        assertEquals("UTF-8", f.charset());
    }

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
            new MimeType("image", "jpeg"),
            new HashMap<String, String>(),
            _rm);


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
            new MimeType("text", "plain"),
            new HashMap<String, String>(),
            _rm);

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
            new File(
                new ResourceName("foo"),
                "foo",
                "desc",
                data,
                1,
                new MimeType("foo", "bar"),
                new HashMap<String, String>(),
                _rm);

        // ACT
        final FileDelta o = f.createSnapshot();

        // ASSERT
        assertEquals(new MimeType("foo", "bar"), o.getMimeType());
        assertEquals(1, o.getSize());
        assertEquals(new ID(data.id().toString()), o.getData());
    }

    /**
     * Test.
     */
    public void testMimeTypeProperty() {

        // ARRANGE
        final File f =
            new File(
                new ResourceName("foo"),
                "foo",
                "desc",
                new Data(),
                1,
                new MimeType("foo", "bar"),
                new HashMap<String, String>(),
                _rm);

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
                1,
                _rm);

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
                new ResourceName("file"),
                "title",
                "desc",
                data,
                0,
                _rm);
            fail("The constructor should reject a NULL file data.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
