/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import java.io.InputStream;

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
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
