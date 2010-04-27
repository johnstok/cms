/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

package ccc.commons.streams;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import ccc.api.core.StreamAction;
import ccc.commons.IO;

/**
 * A stream action that can read a raw bytes into a string.
 *
 * @author Civic Computing Ltd.
 */
public final class ReadToStringAction
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
    public ReadToStringAction(final StringBuilder sb,
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
}
