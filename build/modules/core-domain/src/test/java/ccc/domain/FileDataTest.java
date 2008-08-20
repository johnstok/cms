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

import org.hibernate.lob.BlobImpl;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class FileDataTest extends TestCase {

    /**
     * Test.
     *
     */
    public void testRejectNullParameters() {

        // ARRANGE

        // ACT
        try {
            new FileData(null);
            fail("The constructor should reject a NULL file data.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }


    /**
     * Test.
     *
     */
    public void testRejectTooBigFileData() {

        // ARRANGE
        final byte[] fatData = new byte[FileData.MAX_FILE_SIZE+1];

        // ACT
        try {
            @SuppressWarnings("unused")
            final FileData fileData = new FileData(new BlobImpl(fatData));
            fail("Creation of file data over 32MB should fail");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Data size must be under 33554432",
                e.getMessage());
        }

    }
}
