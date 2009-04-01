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
import java.util.Collection;
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
import org.textmining.extraction.TextExtractor;
import org.textmining.extraction.word.PasswordProtectedException;
import org.textmining.extraction.word.WordTextExtractorFactory;

import ccc.domain.File;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.services.SearchEngine;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Local(SearchEngine.class)
public class SearchEngineEJB  implements SearchEngine {

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
    public void update(final Resource r) {
        if ("PAGE".equals(r.type().toString())) {
            delete(r);
            add(r.as(Page.class));
        }
    }


    /** {@inheritDoc} */
    @Override
    public void add(final Page page) {
        if (!page.isPublished()) {
            LOG.debug("Skipped indexing for unpublished page : "+page.title());
            return;
        }
        final Document d = indexPage(page);
        _lucene.add(d);
        LOG.info("Indexed: "+page.title());
    }


    private Document indexPage(final Page page) {

        final Document d = createDocument(page);

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
                Field.Store.YES,
                Field.Index.ANALYZED));
        return d;
    }

    private Document createDocument(final Resource resource) {

        final Document d = new Document();
        d.add(
            new Field(
                "id",
                resource.id().toString(),
                Field.Store.YES,
                Field.Index.NOT_ANALYZED));

        final StringBuffer roles = new StringBuffer();
        final Collection<String> rroles = resource.computeRoles();
        if (rroles != null && rroles.size() > 0) {
            for (final String role : resource.computeRoles()) {
                if (roles.length()!=0) {
                    roles.append(",");
                }
                roles.append(role);
            }
        } else {
            roles.append("anonymous");
        }


        d.add(new Field("roles",
            roles.toString(),
            Field.Store.YES,
            Field.Index.NOT_ANALYZED));
        return d;
    }


    /** {@inheritDoc} */
    @Override
    public void add(final File file, final InputStream input) {
        if (!PredefinedResourceNames.CONTENT.equals(
            file.root().name().toString())) {
            LOG.debug("Skipped indexing for non content file : "+file.title());
            return;
        }
        final Document d = createDocument(file);

        final String subType = file.mimeType().getSubType();
        String content = null;
        if ("pdf".equalsIgnoreCase(subType)) {
            content = indexPDF(input, d);
            LOG.info("Indexed PDF: "+file.title());
        } else if ("msword".equalsIgnoreCase(subType)) {//no MS2007 support
            content = indexWord(input, d);
            LOG.info("Indexed Word: "+file.title());
        } else {
            LOG.info("Unknown type "+subType);
        }
        if (content != null) {
            _lucene.add(d);
            LOG.info("Added to Lucene document.");
        }
    }

    private String indexPDF(final InputStream input, final Document d) {
        String content = null;
        PDDocument doc = null;
        try {
            doc = PDDocument.load(input);
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(10);
            content = stripper.getText(doc);
            content = cleanUpContent(content);
            d.add(
                new Field(
                    "content",
                    content,
                    Field.Store.YES,
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
        return content;

    }

    private String indexWord(final InputStream input, final Document d) {
        String content = null;
        final WordTextExtractorFactory factory = new WordTextExtractorFactory();
        try {
            final TextExtractor extractor = factory.textExtractor(input);
            content = extractor.getText();
            content = cleanUpContent(content);
            d.add(
                new Field(
                    "content",
                    content,
                    Field.Store.YES,
                    Field.Index.ANALYZED));

        } catch (final PasswordProtectedException ppe) {
            LOG.error("File is password protected; not indexable");
        } catch (final Exception e) {
            LOG.error("Exception caught when trying to extract text ", e);
        }
        return content;
    }

    /** {@inheritDoc} */
    @Override
    public void update(final File file, final InputStream input) {
        delete(file);
        add(file, input);
    }

    private void delete(final Resource r) {
        _lucene.remove(r.id().toString(), "id");
    }

    private String cleanUpContent(final String content) {
        String result = content;
        if (result != null) {
            result = content.replaceAll("[\\x00-\\x1f]", " ");
            result = content.replaceAll("\\<.*?>", ""); // Scrub HTML
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void updateRoles(final Resource r) {
        _lucene.updateRolesField(r.id().toString(), r.roles());
    }
}
