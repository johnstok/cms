/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.io.InputStream;

import junit.framework.TestCase;


/**
 * Tests for the {@link ImageMetadataStreamAction} class.
 *
 * @author Civic Computing Ltd.
 */
public class ImageMetadataStreamActionTest
    extends
        TestCase {

    /**
     * Test.
     * @throws Exception If stream reading fails.
     */
    public void testReadsJpegMetadata() throws Exception {

        // ARRANGE
        final InputStream fis =
            getClass().getResourceAsStream("/images/flowers.jpg");
        final ImageMetadataStreamAction sa = new ImageMetadataStreamAction();

        // ACT
        try {
            sa.execute(fis);
        } finally {
            fis.close();
        }

        // ASSERT
        assertEquals("683", sa.getMetadata().get("image.height"));
        assertEquals("1024", sa.getMetadata().get("image.width"));
    }

    /**
     * Test.
     * @throws Exception If stream reading fails.
     */
    public void testMissingCodecsHandledGracefully() throws Exception {

        // ARRANGE
        final InputStream fis =
            getClass().getResourceAsStream("/images/bug.tif");
        final ImageMetadataStreamAction sa = new ImageMetadataStreamAction();

        // ACT
        try {
            sa.execute(fis);
        } finally {
            fis.close();
        }

        // ASSERT
        assertEquals(0, sa.getMetadata().size());
    }
}
