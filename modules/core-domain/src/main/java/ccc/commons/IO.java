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
        final int bufferSize = 8*1024; //8K
        final byte[] buffer = new byte[bufferSize];

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
