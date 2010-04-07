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
package ccc.search;

import java.io.InputStream;
import java.nio.charset.Charset;

import ccc.commons.IO;
import ccc.rest.StreamAction;

/**
 * A stream action that reads a input stream into a string.
 *
 * @author Civic Computing Ltd.
 */
public class TxtExtractor implements StreamAction {

    private String _content;

    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) throws Exception {
        // Assume files have come from windows.
        _content = IO.toString(is, Charset.forName("windows-1252"));
    }

    /**
     * Accessor.
     *
     * @return The contents of the stream, as a string.
     */
    public String getContent() {
        return _content;
    }
}
