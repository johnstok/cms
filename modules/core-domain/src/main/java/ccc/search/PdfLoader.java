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

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.pdfbox.pdmodel.PDDocument;

import ccc.services.DataManager;

/**
 * A StreamAction that can load a PDF file into memory.
 *
 * @author Civic Computing Ltd.
 */
public class PdfLoader implements DataManager.StreamAction {
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
