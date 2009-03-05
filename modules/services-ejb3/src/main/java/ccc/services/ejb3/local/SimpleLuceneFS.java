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

import java.io.File;
import java.io.IOException;

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

    private final String _indexPath = "C:\\WINDOWS\\Temp\\lucene"; // FIXME


    /** {@inheritDoc} */
    @Override
    public void add(final Document document) {
        try {
            final IndexWriter writer = createWriter();
            writer.addDocument(document);
            writer.optimize();
            writer.close();
        } catch (final IOException e) {
            LOG.warn("Error adding document.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void remove(final String searchTerms,
                       final String field) {
        try {
            final IndexWriter writer = createWriter();
            writer.deleteDocuments(
                new QueryParser(
                    field,
                    new StandardAnalyzer())
                .parse(searchTerms));
            writer.expungeDeletes();
            writer.optimize();
            writer.close();
        } catch (final IOException e) {
            LOG.warn("Error removing document.", e);
        } catch (final ParseException e) {
            LOG.warn("Error removing document.", e);
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
}
