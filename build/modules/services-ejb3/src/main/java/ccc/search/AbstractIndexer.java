/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.search;

import java.io.IOException;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import ccc.commons.XHTML;
import ccc.domain.File;
import ccc.domain.Page;
import ccc.persistence.DataRepository;
import ccc.types.DBC;
import ccc.types.Paragraph;
import ccc.types.ParagraphType;
import ccc.types.PredefinedResourceNames;

/**
 * Abstract helper class for implementing search indexers.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractIndexer
    implements
        Indexer {

    private static final int MAX_PAGES_TO_INDEX = 10;
    private static final Logger LOG =
        Logger.getLogger(AbstractIndexer.class.getName());

    private final DataRepository _data;

    /**
     * Constructor.
     *
     * @param dm The data manager.
     */
    public AbstractIndexer(final DataRepository dm) {
        DBC.require().notNull(dm);
        _data = dm;
    }

    /** {@inheritDoc} */
    @Override
    public void indexFile(final ccc.domain.File file) {
        if (!PredefinedResourceNames.CONTENT.equals(
            file.getRoot().getName().toString())) {
            LOG.debug(
                "Skipped indexing for non content file : "+file.getTitle());
            return;
        }

        String content = null;
        final String primaryType = file.getMimeType().getPrimaryType();
        final String subType = file.getMimeType().getSubType();

        if ("pdf".equalsIgnoreCase(subType)) {
            content = indexPDF(file);
            LOG.debug("Indexed PDF: "+file.getTitle());

        } else if ("msword".equalsIgnoreCase(subType)) {//no MS2007 support
            content = indexWord(file);
            LOG.debug("Indexed Word: "+file.getTitle());

        } else if ("text".equalsIgnoreCase(primaryType)) {
            content = indexText(file);
            LOG.debug("Indexed text: "+file.getTitle());

        } else {
            LOG.debug("Ignored unknown type: "+subType);

        }

        if (null!=content) {
            createDocument(file.getId(), content);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void indexPage(final Page page) {
        final StringBuilder sb = new StringBuilder(page.getTitle());
        for (final Paragraph p : page.currentRevision().getParagraphs()) {
            if (ParagraphType.TEXT == p.getType() && p.getText() != null) {
                sb.append(" ");
                sb.append(XHTML.cleanUpContent(p.getText()));
            }
        }
        createDocument(page.getId(), sb.toString());
        LOG.debug("Indexed page: "+page.getTitle());
    }


    /**
     * Add a document to the lucene index.
     *
     * @param id      The resource's id.
     * @param content The resource's content.
     */
    protected abstract void createDocument(final UUID id,
                                           final String content);


    private String indexPDF(final File file) {
        final PdfLoader pdfLoader = new PdfLoader(file.getTitle());
        PDDocument doc = null;

        try {
            _data.retrieve(file.getData(), pdfLoader);
            doc = pdfLoader.getDocument();
            if (doc == null) {
                return null;
            }
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(MAX_PAGES_TO_INDEX);
            final StringBuilder sb = new StringBuilder(file.getTitle());
            sb.append(" ");
            final String content = stripper.getText(doc);
            sb.append(XHTML.cleanUpContent(content));
            return sb.toString();

        } catch (final Exception e) {
            LOG.warn("PDF indexing failed: "+e.getMessage());
            return null;

        } finally {
            try {
                if (doc != null) {
                    doc.close();
                }
            } catch (final IOException e) {
                LOG.debug("Closing PDF Document failed. ", e);
            }
        }
    }


    private String indexWord(final File file) {
        final WordExtractor we = new WordExtractor();

        try {
            _data.retrieve(file.getData(), we);
            final StringBuilder sb = new StringBuilder(file.getTitle());
            sb.append(" ");
            final String content = we.getExtractor().getText();
            sb.append(XHTML.cleanUpContent(content));
            return sb.toString();

        } catch (final Exception e) {
            LOG.warn("Word document indexing failed: "+e.getMessage());
            return null;
        }
    }


    private String indexText(final File file) {
        final TxtExtractor te = new TxtExtractor();
        _data.retrieve(file.getData(), te);
        final StringBuilder sb = new StringBuilder(file.getTitle());
        sb.append(" ");
        final String content = te.getContent();
        sb.append(XHTML.cleanUpContent(content));
        return sb.toString();
    }

}
