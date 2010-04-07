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
package ccc.commons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 * Utility methods for IO processing.
 *
 * @author Civic Computing Ltd.
 */
public final class IO {

    /** BUFFER_SIZE : int. */
    static final int BUFFER_SIZE = 8*1024; //8K

    private IO() { super(); }

    /**
     * Copy from an input stream to an output stream.
     * Optimisation based on:
     * http://java.sun.com/
     * docs/books/performance/1st_edition/html/JPIOPerformance.fm.html
     *
     * @param is The {@link InputStream} to copy from.
     * @param os The {@link OutputStream} to copy to.
     * @throws IOException If a failure occurs with either stream.
     */
    public static void copy(final InputStream is,
                            final OutputStream os) throws IOException {
        final byte[] buffer = new byte[BUFFER_SIZE];

        while (true) {
            final int amountRead = is.read(buffer);
            if (amountRead == -1) {
                break;
            }
            os.write(buffer, 0, amountRead);

        }
    }

    /**
     * Copy an input stream to a string.
     *
     * @param is The {@link InputStream} to copy from.
     * @param encoding The character encoding for the stream.
     * @return The result string.
     * @throws IOException If a failure occurs with either stream.
     */
    public static String toString(final InputStream is,
                                  final Charset encoding) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(is, baos);
        return new String(baos.toByteArray(), encoding);
    }
}
