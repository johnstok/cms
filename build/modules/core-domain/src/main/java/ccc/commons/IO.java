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
import java.io.UnsupportedEncodingException;

import ccc.domain.CCCException;


/**
 * Utility methods for IO processing.
 *
 * @author Civic Computing Ltd.
 */
public final class IO {

    private static String encoding = "UTF-8";

    private IO() { super(); }

    /**
     * Copy from an input stream to an output stream.
     * Optimisation based on:
     * http://java.sun.com/
     * docs/books/performance/1st_edition/html/JPIOPerformance.fm.html
     *
     * @param is The {@link InputStream} to copy from.
     * @param os The {@link OutputStream} to copy to.
     */
    public static void copy(final InputStream is, final OutputStream os) {
        final int bufferSize = 8*1024; //8K
        final byte[] buffer = new byte[bufferSize];
        try {
            while (true) {
                final int amountRead = is.read(buffer);
                if (amountRead == -1) {
                   break;
                }
                os.write(buffer, 0, amountRead);

             }
        } catch (final IOException e) {
            throw new CCCException("Error copying data.", e);
        }
    }

    /**
     * Copy an input stream to a string.
     *
     * @param is The {@link InputStream} to copy from.
     * @return The result string.
     */
    public static String toString(final InputStream is) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String result = null;
        copy(is, baos);
        try {
            result = baos.toString(encoding);
        } catch (final UnsupportedEncodingException e) {
            throw new CCCException("Error copying data.", e);
        }
        return result;

    }
}
