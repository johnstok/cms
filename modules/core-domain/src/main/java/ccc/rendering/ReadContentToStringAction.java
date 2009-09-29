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

package ccc.rendering;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import ccc.domain.File;
import ccc.persistence.FileRepository;
import ccc.persistence.StreamAction;
import ccc.serialization.IO;

/**
 * A stream action that can read a raw bytes into a string.
 *
 * @author Civic Computing Ltd.
 */
public final class ReadContentToStringAction
    implements
        StreamAction {

    private final StringBuilder _sb;
    private final String        _charset;

    /**
     * Constructor.
     *
     * @param sb The string builder to read into.
     * @param charset The character set to use.
     */
    public ReadContentToStringAction(final StringBuilder sb,
                                     final String charset) {
        _sb = sb;
        _charset = charset;
    }

    /** {@inheritDoc} */
    @Override public void execute(final InputStream is)
                                          throws Exception {
        final ByteArrayOutputStream os =
            new ByteArrayOutputStream();
        IO.copy(is, os);
        _sb.append(
            new String(
                os.toByteArray(),
                Charset.forName((null==_charset ? "UTF-8" : _charset))));
    }

    /**
     * Helper method that reads a file's contents into a string.
     *
     * @param dm The file repository.
     * @param file The file to read.
     *
     * @return The file's contents as a string.
     */
    public static String read(final FileRepository dm, final File file) {
        final StringBuilder sb = new StringBuilder();
        dm.retrieve(
            file.data(), new ReadContentToStringAction(sb, file.charset()));
        return sb.toString();
    }
}
