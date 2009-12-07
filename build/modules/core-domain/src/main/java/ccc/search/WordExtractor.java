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

import org.textmining.extraction.TextExtractor;
import org.textmining.extraction.word.WordTextExtractorFactory;

import ccc.persistence.StreamAction;

/**
 * A stream action that interprets a stream as a MS Word document.
 *
 * @author Civic Computing Ltd.
 */
public class WordExtractor implements StreamAction {
    private final WordTextExtractorFactory _factory =
        new WordTextExtractorFactory();
    private TextExtractor _extractor;

    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) throws Exception {
        _extractor = _factory.textExtractor(is);
    }

    /**
     * Accessor.
     *
     * @return The text from a MS Word document, as a text extractor.
     */
    public TextExtractor getExtractor() {
        return _extractor;
    }
}
