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

import org.apache.log4j.Logger;
import org.textmining.extraction.word.WordTextExtractorFactory;

import ccc.plugins.search.TextExtractor;

/**
 * A text extractor for MS Word documents.
 *
 * @author Civic Computing Ltd.
 */
public class WordExtractor
    implements
        TextExtractor {
    private static final Logger LOG = Logger.getLogger(WordExtractor.class);

    private String _text = "";


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) {
        try {
            final WordTextExtractorFactory factory =
                new WordTextExtractorFactory();
            final org.textmining.extraction.TextExtractor extractor =
                factory.textExtractor(is);
            _text = extractor.getText();

        } catch (final Exception e) {
            LOG.warn("Word document extraction failed.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String getText() { return _text; }
}
