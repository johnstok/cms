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
package ccc.search.lucene;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;

import ccc.persistence.FileRepository;
import ccc.search.AbstractIndexer;
import ccc.search.SearchException;
import ccc.search.SearchResult;


/**
 * Implementation of {@link SimpleLucene} operating on a file system index.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleLuceneFS
    extends
        AbstractIndexer
    implements
        SimpleLucene {

    private static final Logger LOG =
        Logger.getLogger(SimpleLuceneFS.class.getName());

    private final Properties _properties = new Properties();
    private final String _indexPath;
    private IndexWriter _writer;


    /**
     * Constructor.
     */
    public SimpleLuceneFS(final FileRepository dm)  {
        super(dm);
        try {
            final InputStream inputStream =
                this.getClass().getClassLoader().
                getResourceAsStream("lucene.properties");
            _properties.load(inputStream);
        } catch (final IOException e) {
           LOG.error("Loading lucene.properties failed.");
        }
        _indexPath = _properties.getProperty("indexPath");
    }


    private IndexWriter createWriter() throws IOException {
        final java.io.File f = new java.io.File(_indexPath);
        final IndexWriter writer =
            new IndexWriter(
                f,
                new StandardAnalyzer(),
                IndexWriter.MaxFieldLength.UNLIMITED);
        return writer;
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final int resultCount,
                             final int page) {
        if (searchTerms == null || searchTerms.trim().equals("")) {
            return new SearchResult(new HashSet<UUID>(), 0, searchTerms, page);
        }

        final String field = "content";
        final int maxHits = (page+1)*resultCount;
        final CapturingHandler sh = new CapturingHandler(resultCount, page);

        find(searchTerms, field, maxHits, sh);

        return new SearchResult(
            sh.getHits(), sh.getResultCount(), searchTerms, page);
    }


    private void find(final String searchTerms,
                      final String field,
                      final int maxHits,
                      final CapturingHandler sh) {
        IndexSearcher searcher = null;
        try {
            searcher =
                new IndexSearcher(_indexPath);

            final TopDocs docs =
                searcher.search(
                    new QueryParser(
                        field,
                        new StandardAnalyzer()).parse(searchTerms),
                        maxHits);

            sh.handle(searcher, docs);
        } catch (final IOException e) {
            LOG.warn("Error performing query.", e);
        } catch (final ParseException e) {
            LOG.warn("Error performing query.", e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (final IOException e) {
                    // nothing we can do.
                }
            }
        }
    }


    /**
     * Removes all entries from the lucene index.
     *
     * @throws ParseException If the document query fails.
     * @throws IOException If index writing fails.
     */
    private void clearIndex() throws IOException, ParseException {
        _writer.deleteDocuments(
            new QueryParser(
                "*",
                new StandardAnalyzer())
            .parse("*"));
        _writer.expungeDeletes();
        LOG.info("Deleted all existing documents.");
    }

    /** {@inheritDoc} */
    @Override
    public void commitUpdate() {
        try {
            _writer.optimize();
        } catch (final IOException e) {
            LOG.error("Failed to optimize index.", e);
        }
        try {
            _writer.close();
            LOG.info("Commited index update.");
        } catch (final IOException e) {
            LOG.error("Failed to close index writer.", e);
        }
        _writer = null;
    }

    /** {@inheritDoc} */
    @Override
    public void rollbackUpdate() {
        try {
            _writer.rollback();
            LOG.info("Rolled back index update.");
        } catch (final IOException e) {
            _writer = null;
            LOG.error("Error rolling back lucene write.", e);
        }
    }

    /** {@inheritDoc}*/
    @Override
    public void startUpdate() throws SearchException {
        try {
            _writer = createWriter();
            clearIndex();
            LOG.info("Staring index update.");
        } catch (final IOException e) {
            throw new SearchException(e);
        } catch (final ParseException e) {
            throw new SearchException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void createDocument(final UUID id, final String content) {
        try {
            final Document d = new Document();
            d.add(
                new Field(
                    "id",
                    id.toString(),
                    Field.Store.YES,
                    Field.Index.NOT_ANALYZED));
            d.add(
                new Field(
                    "content",
                    content,
                    Field.Store.NO,
                    Field.Index.ANALYZED));
            _writer.addDocument(d);
            LOG.info("Added document.");

        } catch (final IOException e) {
            LOG.warn("Error adding document.", e);
        }
    }
}
