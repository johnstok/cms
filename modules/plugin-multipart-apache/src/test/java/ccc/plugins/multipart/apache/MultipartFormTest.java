/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.plugins.multipart.apache;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.fileupload.FileItem;


/**
 * Test for {@link MultipartForm}.
 *
 * @author Civic Computing Ltd.
 */
public class MultipartFormTest extends TestCase {


    /**
     * Test.
     */
    public void testNonMultipartRequestIsRejected() {

        // ARRANGE

        // ACT
        try {
            new MultipartForm(
                "UTF-8",
                1,
                "text/plain",
                new ByteArrayInputStream(new byte[] {1}));
            fail();

        // ASSERT
        } catch (final RuntimeException e) {
            assertEquals("Not a multipart.", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testMaxInMemorySizeIsConfigurable() {

        // ARRANGE
        final int kb256 = 256*1024;

        // ACT

        // ASSERT
        assertEquals(kb256, MultipartForm.maxInMemorySize());
    }


    /**
     * Test.
     */
    public void testMaxFileSizeIsConfigurable() {

        // ARRANGE
        final int mb32 = 16*1024*1024;

        // ACT

        // ASSERT
        assertEquals(mb32, MultipartForm.maxFileSize());
    }


    /**
     * Test.
     */
    public void testParse() {

        // ARRANGE
        final String charEncoding = null;
        final int contentLength = 949;
        final String contentType =
            "multipart/form-data;"
            + "boundary=\"---------------------------"
            + "1145133423187368413760068182\"";


        // ACT
        final MultipartForm f =
            new MultipartForm(
                charEncoding,
                contentLength,
                contentType,
                getClass().getResourceAsStream("/simple.multipart"));

        // ASSERT
        assertEquals(
            "Created.", f.getString("comment"));
        assertEquals(
            "", f.getString("majorEdit"));
        assertEquals(
            "4acbac13-679c-4abc-8d51-d6c013db74d2", f.getString("path"));
        assertEquals(
            "Hello world!", f.getString("file"));
        assertEquals(
            "simple.txt", f.getString("fileName"));
        assertEquals(
            Arrays.asList(new String[] {
                "file",
                "path",
                "fileName",
                "comment",
                "majorEdit"}),
            f.getFormItemNames());
    }


    /**
     * Test.
     */
    public void testParseUnicode() {

        // ARRANGE
        final String charEncoding = null;
        final int contentLength = 1068;
        final String contentType =
            "multipart/form-data;"
            + "boundary=\"---------------------------"
            + "1145133423187368413760068182\"";


        // ACT
        final MultipartForm f =
            new MultipartForm(
                charEncoding,
                contentLength,
                contentType,
                getClass().getResourceAsStream("/unicode.multipart"));

        // ASSERT
        assertEquals(
            "案例学习", f.getString("‡"));
        assertEquals(
            "Created.", f.getString("comment"));
        assertEquals(
            "", f.getString("majorEdit"));
        assertEquals(
            "4acbac13-679c-4abc-8d51-d6c013db74d2", f.getString("path"));
        assertEquals(
            "Hello world!", f.getString("file"));
        assertEquals(
            "simple.txt", f.getString("fileName"));
        assertEquals(
            Arrays.asList(new String[] {
                "‡",
                "file",
                "path",
                "fileName",
                "comment",
            "majorEdit"}),
            f.getFormItemNames());
    }

    /**
     * Test.
     */
    public void testContructor() {

        // ARRANGE
        final FileItem foo = new TestFileItem("foo");
        final List<FileItem> items =
            Arrays.asList(
                new FileItem[]{foo});

        // ACT
        final MultipartForm f = new MultipartForm(items, "UTF-8");

        assertSame("foo", f.getString("foo"));

    }

    /**
     * Implementation of {@link FileItem} for testing.
     *
     * @author Civic Computing Ltd.
     */
    private static final class TestFileItem implements FileItem {

        private String _fieldName;

        /**
         * Constructor.
         *
         * @param string
         */
        public TestFileItem(final String string) {
            setFieldName(string);
        }

        public void delete() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public byte[] get() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public String getContentType() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public String getFieldName() {
            return _fieldName;
        }

        public InputStream getInputStream() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public String getName() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public OutputStream getOutputStream() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public long getSize() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public String getString() {
            return _fieldName;
        }

        public String getString(final String arg0) {
            return _fieldName;
        }

        public boolean isFormField() {
            return true;
        }

        public boolean isInMemory() {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public void setFieldName(final String arg0) {
            _fieldName = arg0;
        }

        public void setFormField(final boolean arg0) {
            throw new UnsupportedOperationException("Method not implemented.");
        }

        public void write(final File arg0) throws Exception {
            throw new UnsupportedOperationException("Method not implemented.");
        }
    }

}
