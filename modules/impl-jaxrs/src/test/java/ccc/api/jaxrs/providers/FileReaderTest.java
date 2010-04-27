/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.api.jaxrs.providers;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.UUID;

import javax.ws.rs.core.MediaType;

import junit.framework.TestCase;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

import ccc.api.core.File;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


/**
 * Tests for the {@link FileReader} class.
 *
 * @author Civic Computing Ltd.
 */
public class FileReaderTest
    extends
        TestCase {


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testWriteTo() throws Exception {

        // ARRANGE
        final DummyOutputStream es = new DummyOutputStream();
        final FileReader fr = new FileReader();
        final File f =
            new File(
                MimeType.BINARY_DATA,
                null,
                null,
                new ResourceName("foo"),
                "foo",
                new HashMap<String, String>());
        f.setDescription("foo");
        f.setComment("test");
        f.setParent(UUID.randomUUID());

        // ACT
        fr.writeTo(
            f,
            File.class,
            File.class,
            new Annotation[]{},
            MediaType.MULTIPART_FORM_DATA_TYPE,
            new MultivaluedMapImpl<String, Object>(),
            es);

        // ASSERT
        assertTrue(es.isClosed());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testWriteToAlwaysClosesStream() throws Exception {

        // ARRANGE
        final DummyOutputStream es = new DummyOutputStream();
        final FileReader fr = new FileReader();

        // ACT
        try {
            fr.writeTo(
                null,
                File.class,
                File.class,
                new Annotation[]{},
                MediaType.MULTIPART_FORM_DATA_TYPE,
                new MultivaluedMapImpl<String, Object>(),
                es);
            fail();

        } catch (final NullPointerException e) { /* NO OP */ }

        // ASSERT
        assertTrue(es.isClosed());
    }


    static class DummyOutputStream
        extends
            OutputStream {

        private boolean _closed = false;

        /** {@inheritDoc} */
        @Override public void write(final int b) { /* NO OP */ }

        /** {@inheritDoc} */
        @Override public void close() { _closed  = true; }

        /**
         * Accessor.
         *
         * @return True if the stream was closed; false otherwise.
         */
        public boolean isClosed() { return _closed; }
    }
}
