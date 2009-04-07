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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
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
import org.textmining.extraction.word.WordTextExtractorFactory;

import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.services.DataManager;
import ccc.services.ResourceDao;
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
    private static final int TIMEOUT_DELAY_SECS = 60*60*1000;
    private static final int INITIAL_DELAY_SECS = 1;
    private static final String TIMER_NAME = "index_scheduler";
    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    @javax.annotation.Resource private EJBContext _context;
    @EJB(name=ResourceDao.NAME) private ResourceDao _dao;
    @EJB(name=DataManager.NAME) private DataManager _data;
    private SimpleLucene _lucene;

    /** Constructor. */
    @SuppressWarnings("unused") public SearchEngineEJB() {
        _lucene = new SimpleLuceneFS();
    }


    /**
     * Constructor.
     *
     * @param rdao
     * @param dm
     */
    public SearchEngineEJB(final ResourceDao rdao, final DataManager dm) {
        this();
        _dao = rdao;
        _data = dm;
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
    public void index() {
        try {
            _lucene.startUpdate();
            _lucene.clearIndex();
            indexPages();
            indexFiles();
            _lucene.commitUpdate();
        } catch (final Exception e) {
            LOG.error("Error indexing resources.", e);
            _lucene.rollbackUpdate();
        }
    }


    /**
     * Run the scheduled action.
     *
     * @param timer The timer that called this method.
     */
    @Timeout
    public void run(@SuppressWarnings("unused") final Timer timer) {
        index();
    }


    /** {@inheritDoc} */
    @Override
    public void start() {
        LOG.debug("Starting indexer.");
        _context.getTimerService().createTimer(
            INITIAL_DELAY_SECS, TIMEOUT_DELAY_SECS, TIMER_NAME);
        LOG.debug("Started indexer.");
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void stop() {
        LOG.debug("Stopping indexer.");
        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (TIMER_NAME.equals(t.getInfo())) {
                t.cancel();
            }
        }
        LOG.debug("Stopped indexer.");
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public boolean isRunning() {
        final Collection<Timer> c = _context.getTimerService().getTimers();
        for (final Timer t : c) {
            if (TIMER_NAME.equals(t.getInfo())) {
                return true;
            }
        }
        return false;
    }


    private void indexFiles() {
        final List<File> files = _dao.list("allFiles", File.class);
        for (final File f : files) {
            if (f.isVisible() && f.roles().isEmpty()) {
                indexFile(f);
            }
        }
    }


    private void indexPages() {
        final List<Page> pages = _dao.list("allPages", Page.class);
        for (final Page p : pages) {
            if (p.isVisible() && p.roles().isEmpty()) {
                indexPage(p);
            }
        }
    }


    private void indexPage(final Page page) {
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

        _lucene.add(d);

        LOG.info("Indexed: "+page.title());
    }


    private void indexFile(final File file) {
        if (!PredefinedResourceNames.CONTENT.equals(
            file.root().name().toString())) {
            LOG.debug("Skipped indexing for non content file : "+file.title());
            return;
        }

        final Document document = createDocument(file);
        final Data data = file.data();
        final String subType = file.mimeType().getSubType();

        if ("pdf".equalsIgnoreCase(subType)) {
            indexPDF(data, document);
            _lucene.add(document);
            LOG.info("Indexed PDF: "+file.title());

        } else if ("msword".equalsIgnoreCase(subType)) {//no MS2007 support
            indexWord(data, document);
            _lucene.add(document);
            LOG.info("Indexed Word: "+file.title());

        } else {
            LOG.info("Unknown type "+subType);

        }
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


    private void indexPDF(final Data data, final Document d) {
        final PdfLoader pdfLoader = new PdfLoader();

        try {
            _data.retrieve(data, pdfLoader);
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(10);
            String content = stripper.getText(pdfLoader.doc);
            content = cleanUpContent(content);
            d.add(
                new Field(
                    "content",
                    content,
                    Field.Store.YES,
                    Field.Index.ANALYZED));

        } catch (final IOException e) {
            LOG.error("PDF indexing failed.", e);
        } finally {
            try {
                pdfLoader.doc.close();
            } catch (final IOException e) {
                LOG.error("Closing PDF Document failed. ", e);
            }
        }
    }


    private void indexWord(final Data data, final Document d) {
        final WordExtractor we = new WordExtractor();

        try {
            _data.retrieve(data, we);
            String content = we.extractor.getText();
            content = cleanUpContent(content);
            d.add(
                new Field(
                    "content",
                    content,
                    Field.Store.YES,
                    Field.Index.ANALYZED));

        } catch (final IOException e) {
            LOG.error("Exception caught when trying to extract text ", e);
        }
    }


    private String cleanUpContent(final String content) {
        String result = content;
        if (result != null) {
            result = content.replaceAll("[\\x00-\\x1f]", " ");
            result = content.replaceAll("\\<.*?>", ""); // Scrub HTML
        }
        return result;
    }


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


    private static class PdfLoader implements DataManager.StreamAction {
        PDDocument doc;

        /** {@inheritDoc} */
        @Override public void execute(final InputStream is) throws Exception {
            doc = PDDocument.load(is);
        }
    }


    private static class WordExtractor implements DataManager.StreamAction {
        final WordTextExtractorFactory factory = new WordTextExtractorFactory();
        TextExtractor extractor;

        /** {@inheritDoc} */
        @Override public void execute(final InputStream is) throws Exception {
            extractor = factory.textExtractor(is);
        }
    }
}
