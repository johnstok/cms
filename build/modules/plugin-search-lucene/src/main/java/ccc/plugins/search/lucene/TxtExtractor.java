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
package ccc.plugins.search.lucene;

import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import ccc.commons.IO;
import ccc.plugins.search.TextExtractor;

/**
 * A text extractor for plain text files.
 *
 * @author Civic Computing Ltd.
 */
public class TxtExtractor
    implements
        TextExtractor {
    private static final Logger LOG = Logger.getLogger(TxtExtractor.class);

    private String _text = "";


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) {
        try {
            // Assume files are UTF-8.
            // TODO Add icu4j character-set conversion
            _text = IO.toString(is, Charset.forName("UTF-8"));

        } catch (final Throwable e) {
            LOG.warn("Text file extraction failed: "+e.getMessage());
        }
    }


    /** {@inheritDoc} */
    @Override
    public String getText() { return _text; }
}
