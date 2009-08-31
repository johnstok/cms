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
