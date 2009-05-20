/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.remoting;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.fileupload.FileItem;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MultipartFormTest extends TestCase {

    /**
     * Test.
     */
    public void testContructor() {

        // ARRANGE
        final FileItem foo = new TestFileItem("foo");
        final List<FileItem> items =
            Arrays.asList(
                new FileItem[]{foo });

        // ACT
        final MultipartForm f = new MultipartForm(items);

        assertSame(foo, f.getFormItem("foo"));

    }

    /**
     * TODO: Add Description for this type.
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

        public InputStream getInputStream() throws IOException {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        public String getName() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        public OutputStream getOutputStream() throws IOException {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        public long getSize() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        public String getString() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        public String getString(final String arg0)
            throws UnsupportedEncodingException {

            throw new UnsupportedOperationException("Method not implemented.");
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
