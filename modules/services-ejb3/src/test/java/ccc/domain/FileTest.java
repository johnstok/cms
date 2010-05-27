/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import ccc.api.types.FilePropertyNames;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


/**
 * Tests for the {@link FileEntity} class.
 *
 * @author Civic Computing Ltd
 */
public class FileTest extends TestCase {

    /**
     * Test.
     */
    public void testGetProperty() {

        // ACT
        final FileEntity f = new FileEntity(
            new ResourceName("name"),
            "title",
            "",
            new Data(),
            0,
            MimeType.HTML,
            new HashMap<String, String>(){{
                put(FilePropertyNames.CHARSET, "UTF-8");
            }},
            _rm);


        // ASSERT
        assertEquals("UTF-8", f.getCharset());
    }

    /**
     * Test.
     */
    public void testImageFilesAreDetected() {

        // ARRANGE
        final FileEntity f = new FileEntity(
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
        final FileEntity f = new FileEntity(
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
    public void testMimeTypeProperty() {

        // ARRANGE
        final FileEntity f =
            new FileEntity(
                new ResourceName("foo"),
                "foo",
                "desc",
                new Data(),
                1,
                new MimeType("foo", "bar"),
                new HashMap<String, String>(),
                _rm);

        // ACT
        final MimeType actual = f.getMimeType();

        // ASSERT
        assertEquals(new MimeType("foo", "bar"), actual);
    }

    /**
     * Test.
     */
    public void testFileSizeProperty() {

        // ARRANGE
        final FileEntity f =
            new FileEntity(new ResourceName("foo"),
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
            new FileEntity(
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
        new RevisionMetadata(
            new Date(), UserEntity.SYSTEM_USER, true, "Created.");
}
