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
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import org.textmining.extraction.TextExtractor;
import org.textmining.extraction.word.WordTextExtractorFactory;

import ccc.commons.IO;
import ccc.commons.SearchResult;
import ccc.domain.File;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.persistence.jpa.BaseDao;
import ccc.persistence.jpa.FsCoreData;
import ccc.search.lucene.SearchHandler;
import ccc.search.lucene.SimpleLucene;
import ccc.search.lucene.SimpleLuceneFS;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.Dao;
import ccc.services.DataManager;
import ccc.services.DataManagerEJB;
import ccc.services.QueryNames;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
import ccc.services.Scheduler;
import ccc.services.SearchEngine;
import ccc.services.api.ParagraphType;


/**
 * Lucene Implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=SearchEngine.NAME)
@TransactionAttribute(REQUIRED)
@Remote(Scheduler.class)
@Local(SearchEngine.class)
public class SearchEngineEJB  implements SearchEngine, Scheduler {
    private static final int TIMEOUT_DELAY_SECS = 60*60*1000;
    private static final int INITIAL_DELAY_SECS = 1;
    private static final String TIMER_NAME = "index_scheduler";
    private static final Logger LOG =
        Logger.getLogger(SearchEngineEJB.class.getName());

    @javax.annotation.Resource private EJBContext _context;
    @PersistenceContext private EntityManager _em;

    private DataManager _data;
    private ResourceDao _dao;
    private SimpleLucene _lucene;

    /** Constructor. */
    public SearchEngineEJB() {
        _lucene = new SimpleLuceneFS();
    }


    /**
     * Constructor.
     *
     * @param rdao The ResourceDao
     * @param dm The DataManager
     */
    public SearchEngineEJB(final ResourceDao rdao, final DataManager dm) {
        this();
        _dao = rdao;
        _data = dm;
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                          final int resultCount,
                          final int page) {

        final SearchResult sr = new SearchResult();
        if (searchTerms == null || searchTerms.trim().equals("")) {
            return sr;
        }

        final String field = "content";
        final int maxHits = (page+1)*resultCount;
        final CapturingHandler sh = new CapturingHandler(resultCount, page);

        _lucene.find(searchTerms, field, maxHits, sh);
        sr.totalResults(sh._searchResultCount);
        sr.hits(sh._hits);

        return sr;
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
        final List<File> files = _dao.list(QueryNames.ALL_FILES, File.class);
        for (final File f : files) {
            if (f.isVisible() && f.roles().isEmpty()) {
                indexFile(f);
            }
        }
    }


    private void indexPages() {
        final List<Page> pages = _dao.list(QueryNames.ALL_PAGES, Page.class);
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
            if (ParagraphType.TEXT == p.type()) {
                sb.append(" ");
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
        final String primaryType = file.mimeType().getPrimaryType();
        final String subType = file.mimeType().getSubType();

        if ("pdf".equalsIgnoreCase(subType)) {
            indexPDF(file, document);
            _lucene.add(document);
            LOG.info("Indexed PDF: "+file.title());
        } else if ("msword".equalsIgnoreCase(subType)) {//no MS2007 support
            indexWord(file, document);
            _lucene.add(document);
            LOG.info("Indexed Word: "+file.title());
        } else if ("text".equalsIgnoreCase(primaryType)) {
            indexText(file, document);
            _lucene.add(document);
            LOG.info("Indexed text: "+file.title());
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

        return d;
    }


    private void indexPDF(final File file, final Document d) {
        final PdfLoader pdfLoader = new PdfLoader();
        pdfLoader.name = file.title();
        try {
            _data.retrieve(file.data(), pdfLoader);
            if (pdfLoader.doc == null) {
                return;
            }
            final PDFTextStripper stripper = new PDFTextStripper();
            stripper.setEndPage(10);
            final StringBuilder sb = new StringBuilder(file.title());
            sb.append(" ");
            final String content = stripper.getText(pdfLoader.doc);
            sb.append(cleanUpContent(content));
            d.add(
                new Field(
                    "content",
                    sb.toString(),
                    Field.Store.NO,
                    Field.Index.ANALYZED));

        } catch (final Exception e) {
            LOG.error("PDF indexing failed.", e);
        } finally {
            try {
                if (pdfLoader.doc != null) {
                    pdfLoader.doc.close();
                }
            } catch (final IOException e) {
                LOG.error("Closing PDF Document failed. ", e);
            }
        }
    }


    private void indexWord(final File file, final Document d) {
        final WordExtractor we = new WordExtractor();

        try {
            _data.retrieve(file.data(), we);
            final StringBuilder sb = new StringBuilder(file.title());
            sb.append(" ");
            final String content = we.extractor.getText();
            sb.append(cleanUpContent(content));
            d.add(
                new Field(
                    "content",
                    sb.toString(),
                    Field.Store.NO,
                    Field.Index.ANALYZED));

        } catch (final Exception e) {
            LOG.error("Exception caught when trying to extract text ", e);
        }
    }

    private void indexText(final File file, final Document d) {
        final TxtExtractor te = new TxtExtractor();

        _data.retrieve(file.data(), te);
        final StringBuilder sb = new StringBuilder(file.title());
        sb.append(" ");
        final String content = te.content;
        sb.append(cleanUpContent(content));
        d.add(
            new Field(
                "content",
                sb.toString(),
                Field.Store.NO,
                Field.Index.ANALYZED));

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
    static class CapturingHandler extends SearchHandler {

        /** _hits : Set of UUID. */
        protected final Set<UUID> _hits = new HashSet<UUID>();
        /** _searchResultCount : int. */
        protected int _searchResultCount = 0;
        private final int _resultCount;
        private final int _page;

        /**
         * Constructor.
         *
         * @param resultCount
         * @param page
         */
        public CapturingHandler(final int resultCount, final int page) {
            _resultCount = resultCount;
            _page = page;
        }

        /** {@inheritDoc} */
        @Override
        public void handle(final IndexSearcher searcher,
                           final TopDocs docs) throws IOException {
            final int firstResultIndex = _page*_resultCount;
            final int lastResultIndex = (_page+1)*_resultCount;
            _searchResultCount = docs.totalHits;

            for (int i=firstResultIndex;
            i<lastResultIndex && i<docs.scoreDocs.length;
            i++) {
                final int docId = docs.scoreDocs[i].doc;
                _hits.add(lookupResourceId(searcher, docId));
            }
        }

        UUID lookupResourceId(final IndexSearcher searcher,
                              final int docId) throws IOException {
            return UUID.fromString(
                searcher.doc(docId).getField("id").stringValue()
            );
        }
    }


    private static class PdfLoader implements DataManager.StreamAction {
        PDDocument doc;
        String name = "";

        /** {@inheritDoc} */
        @Override public void execute(final InputStream is) throws Exception {
            try {
                doc = PDDocument.load(is);
            } catch (final IOException e) {
                LOG.error("PDF loading failed for file: "+name);
            }
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

    private static class TxtExtractor implements DataManager.StreamAction {
        String content;

        /** {@inheritDoc} */
        @Override public void execute(final InputStream is) throws Exception {
            // Assume files have come from windows.
            content = IO.toString(is, Charset.forName("windows-1252"));
        }
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        final Dao bdao = new BaseDao(_em);
        final AuditLog audit = new AuditLogEJB(bdao);
        _dao = new ResourceDaoImpl(audit, bdao);
        _data = new DataManagerEJB(new FsCoreData(), bdao);
    }
}
