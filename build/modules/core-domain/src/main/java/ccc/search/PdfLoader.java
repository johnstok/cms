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

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.pdfbox.pdmodel.PDDocument;

import ccc.rest.StreamAction;

/**
 * A StreamAction that can load a PDF file into memory.
 *
 * @author Civic Computing Ltd.
 */
public class PdfLoader implements StreamAction {
    private static final Logger LOG = Logger.getLogger(PdfLoader.class);

    private PDDocument   _document;
    private final String _fileName;

    /**
     * Constructor.
     *
     * @param fileName The name of the PDF file.
     */
    public PdfLoader(final String fileName) {
        _fileName = fileName;
    }

    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) {
        try {
            _document = PDDocument.load(is);
        } catch (final IOException e) {
            LOG.error("PDF loading failed for file: "+_fileName);
        }
    }

    /**
     * Accessor.
     *
     * @return The loaded document.
     */
    public PDDocument getDocument() {
        return _document;
    }
}
