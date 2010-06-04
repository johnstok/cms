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

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import ccc.plugins.search.TextExtractor;

/**
 * A text extractor for PDF files.
 *
 * @author Civic Computing Ltd.
 */
public class PdfLoader
    implements
        TextExtractor {
    private static final Logger LOG = Logger.getLogger(PdfLoader.class);

    private String _text = "";


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) {
        try {
            final PDDocument doc = PDDocument.load(is);
            if (doc == null) { return; }
            extractText(doc);

        } catch (final Throwable e) {
            LOG.warn("PDF file extraction failed: "+e.getMessage());
        }
    }


    private void extractText(final PDDocument doc) throws IOException {
        try {
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(MAX_PAGES_TO_INDEX);
            _text = stripper.getText(doc);
        } finally {
            safelyClose(doc);
        }
    }


    private void safelyClose(final PDDocument doc) {
        try {
            if (null!=doc) { doc.close(); }
        } catch (final IOException e) {
            LOG.debug("Closing PDF Document failed.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String getText() { return _text; }
}
