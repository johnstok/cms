/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.persistence;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import junit.framework.TestCase;
import ccc.commons.Resources;
import ccc.domain.Data;
import ccc.rest.StreamAction;


/**
 * Tests for the {@link FsCoreData} class.
 *
 * @author Civic Computing Ltd.
 */
public class FsCoreDataTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testStoreRejectsMissingPath() {

        // ARRANGE

        // ACT
        try {
            new FsCoreData(MISSING.getAbsolutePath());
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Path does not exist: "+MISSING.getAbsolutePath(),
                e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testStoreRejectsPathToFile() {

        // ARRANGE

        // ACT
        try {
            new FsCoreData(EXISTING.getAbsolutePath());
            fail();

            // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Path is not a directory: "+EXISTING.getAbsolutePath(),
                e.getMessage());
        }
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testStoreWritesToDisk() throws Exception {

        // ARRANGE
        final FsCoreData cd = new FsCoreData(FILESTORE.getAbsolutePath());

        // ACT
        final Data d =
            cd.create(
                new ByteArrayInputStream(HELLO_WORLD), HELLO_WORLD.length);

        // ASSERT
        final char c0 = d.getId().toString().charAt(0);
        final char c1 = d.getId().toString().charAt(1);
        final char c2 = d.getId().toString().charAt(2);
        final File dataFile =
            new File(FILESTORE, c0+SEP+c1+SEP+c2+SEP+d.getId().toString());

        assertTrue(dataFile.exists());
        assertEquals(
            "Hello World!",
            Resources.readIntoString(dataFile.toURI().toURL(), UTF8));
    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testStoreReadsFromDisk() throws Exception {

        // ARRANGE
        final FsCoreData cd = new FsCoreData(FILESTORE.getAbsolutePath());
        final Data d = new Data();

        final char c0 = d.getId().toString().charAt(0);
        final char c1 = d.getId().toString().charAt(1);
        final char c2 = d.getId().toString().charAt(2);
        final File dataFolder =
            new File(FILESTORE, c0+SEP+c1+SEP+c2);

        dataFolder.mkdirs();
        final FileOutputStream fos =
            new FileOutputStream(new File(dataFolder, d.getId().toString()));
        fos.write(HELLO_WORLD);
        fos.close();

        // ACT
        cd.retrieve(d, new StreamAction(){
            @Override public void execute(final InputStream is) {

        // ASSERT
                assertEquals(
                    "Hello World!", Resources.readIntoString(is, UTF8));
            }
        });


    }


    /** UTF8 : Charset. */
    static final Charset UTF8 = Charset.forName("UTF-8");

    private static final byte[]  HELLO_WORLD = "Hello World!".getBytes(UTF8);
    private static final String  SEP = File.separator;
    private static final File    MISSING = new File("target"+SEP+"missing");
    private static final File    EXISTING = new File("target"+SEP+"existing");
    private static final File    FILESTORE = new File("target"+SEP+"filestore");


    static {
        try {
            EXISTING.createNewFile();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        FILESTORE.mkdir();
    }
}
