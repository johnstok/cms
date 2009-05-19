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

import org.json.JSONException;
import org.json.JSONObject;

import ccc.api.MimeType;


/**
 * Tests for the {@link File} class.
 *
 * @author Civic Computing Ltd
 */
public class FileTest extends TestCase {

    /**
     * Test.
     * @throws JSONException If the JSON is invalid.
     */
    public void testSnapshot() throws JSONException {

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
        final JSONObject o = new JSONObject(f.createSnapshot().getDetail());

        // ASSERT
        assertEquals("foo", o.get("title"));
        assertEquals("desc", o.get("description"));
        assertEquals(
            "{\"primary-type\":\"foo\",\"sub-type\":\"bar\"}",
            o.get("mimetype").toString());
        assertEquals(1, o.getLong("size"));
        assertEquals(data.id().toString(), o.get("data"));
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
