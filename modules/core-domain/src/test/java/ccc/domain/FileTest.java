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

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Tests for the {@link File} class.
 *
 * @author Civic Computing Ltd
 */
public class FileTest extends TestCase {

    /**
     * Test.
     * @throws MimeTypeParseException  For invalid mime type.
     * @throws JSONException If the JSON is invalid.
     */
    public void testSnapshot() throws MimeTypeParseException, JSONException {

        // ARRANGE
        final Data data = new Data();
        final File f =
            new File(new ResourceName("foo"),
                "foo",
                "desc",
                data,
                1L,
                new MimeType("foo/bar"));

        // ACT
        final JSONObject o = new JSONObject(f.createSnapshot().getDetail());

        // ASSERT
        assertEquals("foo", o.get("title"));
        assertEquals("desc", o.get("description"));
        assertEquals("foo/bar", o.get("mimetype"));
        assertEquals(1, o.getLong("size"));
        assertEquals(data.id().toString(), o.get("data"));
    }

    /**
     * Test.
     * @throws MimeTypeParseException For invalid mime type.
     */
    public void testDataPropertyCanBeUpdated() throws MimeTypeParseException {

        // ARRANGE
        final Data newData = new Data();
        final File f =
            new File(new ResourceName("foo"),
                "foo",
                "desc",
                new Data(),
                1L,
                new MimeType("foo/bar"));

        // ACT
        f.data(newData);

        // ASSERT
        assertEquals(newData, f.data());
    }

    /**
     * Test.
     * @throws MimeTypeParseException For invalid mime type.
     */
    public void testMimeTypeProperty() throws MimeTypeParseException {

        // ARRANGE
        final File f =
            new File(new ResourceName("foo"),
                "foo",
                "desc",
                new Data(),
                1L,
                new MimeType("foo/bar"));

        // ACT
        final MimeType actual = f.mimeType();

        // ASSERT
        assertTrue(
            "Mime types should match",
            new MimeType("foo", "bar").match(actual));
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
                1L);

        // ACT
        final long actual = f.size();

        // ASSERT
        assertEquals(1L, actual);
    }

    /**
     * Test.
     *
     */
    public void testRejectMissingData() {

        // ARRANGE
        final Data data = null;

        // ACT
        try {
            new File(
                new ResourceName("file"), "title", "desc", data, 0L);
            fail("The constructor should reject a NULL file data.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
