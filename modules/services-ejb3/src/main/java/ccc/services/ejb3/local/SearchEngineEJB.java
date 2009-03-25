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
package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.domain.File;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.ServiceNames;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Local(SearchEngine.class)
public class SearchEngineEJB  implements SearchEngine {

    private final Registry _registry = new JNDI();

    /**
     * TODO: Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    private static final class CapturingHandler
        extends
            SearchHandler {

        /** _hits : Set of UUID. */
        protected final Set<UUID> _hits = new HashSet<UUID>();

        /** {@inheritDoc} */
        @Override
        public void handle(final IndexSearcher searcher,
                           final TopDocs docs) throws IOException {
            for (final ScoreDoc doc : docs.scoreDocs) {
                _hits.add(
                    UUID.fromString(
                        searcher.doc(doc.doc)
                        .getField("id")
                        .stringValue()
                    )
                );
            }
        }
    }


    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    private SimpleLucene _lucene;

    /** Constructor. */
    @SuppressWarnings("unused") public SearchEngineEJB() {
        _lucene = new SimpleLuceneFS();
    }


    /** {@inheritDoc} */
    @Override
    public Set<UUID> find(final String searchTerms) {
        final String field = "content";
        final int maxHits = 100;
        final CapturingHandler sh = new CapturingHandler();

        _lucene.find(searchTerms, field, maxHits, sh);

        return sh._hits;
    }


    /** {@inheritDoc} */
    @Override
    public void update(final Page page) {
        delete(page);
        add(page);
    }


    private void delete(final Page page) {
        _lucene.remove(page.id().toString(), "id");
    }


    /** {@inheritDoc} */
    @Override
    public void add(final Page page) {
        final Document d = createDocument(page);
        _lucene.add(d);
        LOG.info("Indexed: "+page.title());
    }


    private Document createDocument(final Page page) {

        final Document d = new Document();
        d.add(
            new Field(
                "id",
                page.id().toString(),
                Field.Store.YES,
                Field.Index.NOT_ANALYZED));
        final StringBuilder sb = new StringBuilder(page.title());
        for (final Paragraph p : page.paragraphs()) {
            if (Paragraph.Type.TEXT == p.type()) {
                final String nohtml =
                    p.text().replaceAll("\\<.*?>", ""); // Scrub HTML
                sb.append(nohtml);
            }
        }
        d.add(
            new Field(
                "content",
                sb.toString(),
                Field.Store.NO,
                Field.Index.ANALYZED));
        return d;
    }


    /** {@inheritDoc} */
    @Override
    public void add(final File file) {
        final Document d = new Document();
        d.add(
            new Field(
                "id",
                file.id().toString(),
                Field.Store.YES,
                Field.Index.NOT_ANALYZED));

        final String subType = file.mimeType().getSubType();
        if ("pdf".equalsIgnoreCase(subType)) {
            indexPDF(file, d);
            _lucene.add(d);
            LOG.info("Indexed: "+file.title());
        }

    }

    private void indexPDF(final File file, final Document d) {

        final InputStream input = dataManager().retrieve(file.data());
        PDDocument doc = null;
        try {
            doc = PDDocument.load(input);
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(10);
            final String text = stripper.getText(doc);
            d.add(
                new Field(
                    "content",
                    text,
                    Field.Store.NO,
                    Field.Index.ANALYZED));

        } catch (final IOException e) {
            LOG.error("PDF indexing failed. "+e.getMessage(), e);
        } finally {
            try {
                doc.close();
            } catch (final IOException e) {
                LOG.error("Closing PDDocument failed. "+e.getMessage());
            }
        }
    }


    /** {@inheritDoc} */
    @Override
    public void update(final File file) {
        delete(file);
        add(file);
    }

    private void delete(final File file) {
        _lucene.remove(file.id().toString(), "id");
    }

    DataManager dataManager() {
        return _registry.get(ServiceNames.DATA_MANAGER_LOCAL);
    }
}
