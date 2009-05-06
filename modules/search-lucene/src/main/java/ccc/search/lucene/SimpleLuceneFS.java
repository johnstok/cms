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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;


/**
 * Implementation of {@link SimpleLucene} operating on a file system index.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleLuceneFS implements SimpleLucene {
    private static final Logger LOG =
        Logger.getLogger(SimpleLuceneFS.class.getName());

    private Properties _properties = new Properties();
    private String _indexPath;

    private IndexWriter _writer;

    /**
     * Constructor.
     * @throws IOException
     *
     */
    public SimpleLuceneFS()  {
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

    /** {@inheritDoc} */
    @Override
    public void add(final Document document) {
        try {
            _writer.addDocument(document);
            LOG.info("Added document.");
        } catch (final IOException e) {
            LOG.warn("Error adding document.", e);
        }
    }


    private IndexWriter createWriter() throws IOException {
        final File f = new File(_indexPath);
        final IndexWriter writer =
            new IndexWriter(
                f,
                new StandardAnalyzer(),
                IndexWriter.MaxFieldLength.UNLIMITED);
        return writer;
    }


    /** {@inheritDoc} */
    @Override
    public void find(final String searchTerms,
                     final String field,
                     final int maxHits,
                     final SearchHandler sh) {

        try {
            final IndexSearcher searcher =
                new IndexSearcher(_indexPath);

            final TopDocs docs =
                searcher.search(
                    new QueryParser(
                        field,
                        new StandardAnalyzer()).parse(searchTerms),
                        maxHits);

            sh.handle(searcher, docs);

            searcher.close();
        } catch (final IOException e) {
            LOG.warn("Error performing query.", e);
        } catch (final ParseException e) {
            LOG.warn("Error performing query.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void clearIndex() throws IOException, ParseException {
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
    public void commitUpdate() throws IOException {
        _writer.optimize();
        _writer.close();
        _writer = null;
        LOG.info("Commited index update.");
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
    public void startUpdate() throws IOException {
        _writer = createWriter();
        LOG.info("Staring index update.");
    }
}
