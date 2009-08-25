/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.domain.File;
import ccc.domain.Page;
import ccc.services.DataManager;
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

    protected final DataManager _data;

    /**
     * Constructor.
     *
     * @param dm The data manager.
     */
    public AbstractIndexer(final DataManager dm) {
        DBC.require().notNull(dm);
        _data = dm;
    }

    /** {@inheritDoc} */
    @Override
    public void indexFile(final ccc.domain.File file) {
        if (!PredefinedResourceNames.CONTENT.equals(
            file.root().name().toString())) {
            LOG.debug("Skipped indexing for non content file : "+file.title());
            return;
        }

        String content = null;
        final String primaryType = file.mimeType().getPrimaryType();
        final String subType = file.mimeType().getSubType();

        if ("pdf".equalsIgnoreCase(subType)) {
            content = indexPDF(file);
            LOG.info("Indexed PDF: "+file.title());

        } else if ("msword".equalsIgnoreCase(subType)) {//no MS2007 support
            content = indexWord(file);
            LOG.info("Indexed Word: "+file.title());

        } else if ("text".equalsIgnoreCase(primaryType)) {
            content = indexText(file);
            LOG.info("Indexed text: "+file.title());

        } else {
            LOG.info("Unknown type "+subType);

        }

        if (null!=content) {
            createDocument(file.id(), content);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void indexPage(final Page page) {
        final StringBuilder sb = new StringBuilder(page.title());
        for (final Paragraph p : page.currentRevision().paragraphs()) {
            if (ParagraphType.TEXT == p.type() && p.text() != null) {
                sb.append(" ");
                sb.append(cleanUpContent(p.text()));
            }
        }
        createDocument(page.id(), sb.toString());
        LOG.info("Indexed: "+page.title());
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
        final PdfLoader pdfLoader = new PdfLoader(file.title());
        PDDocument doc = null;

        try {
            _data.retrieve(file.data(), pdfLoader);
            doc = pdfLoader.getDocument();
            if (doc == null) {
                return null;
            }
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(MAX_PAGES_TO_INDEX);
            final StringBuilder sb = new StringBuilder(file.title());
            sb.append(" ");
            final String content = stripper.getText(doc);
            sb.append(cleanUpContent(content));
            return sb.toString();

        } catch (final Exception e) {
            LOG.error("PDF indexing failed.", e);
            return null;

        } finally {
            try {
                if (doc != null) {
                    doc.close();
                }
            } catch (final IOException e) {
                LOG.error("Closing PDF Document failed. ", e);
            }
        }
    }


    private String indexWord(final File file) {
        final WordExtractor we = new WordExtractor();

        try {
            _data.retrieve(file.data(), we);
            final StringBuilder sb = new StringBuilder(file.title());
            sb.append(" ");
            final String content = we.getExtractor().getText();
            sb.append(cleanUpContent(content));
            return sb.toString();

        } catch (final Exception e) {
            LOG.error("Exception caught when trying to extract text ", e);
            return null;
        }
    }


    private String indexText(final File file) {
        final TxtExtractor te = new TxtExtractor();
        _data.retrieve(file.data(), te);
        final StringBuilder sb = new StringBuilder(file.title());
        sb.append(" ");
        final String content = te.getContent();
        sb.append(cleanUpContent(content));
        return sb.toString();
    }


    private String cleanUpContent(final String content) {
        String result = content;
        if (result != null) {
            result = result.replaceAll("[\\x00-\\x1f]", " ");
            result = result.replaceAll("\\<.*?>", ""); // Scrub HTML
        }
        return result;
    }

}