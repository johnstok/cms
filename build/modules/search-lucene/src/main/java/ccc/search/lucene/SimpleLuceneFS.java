/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.search.lucene;

import java.io.IOException;
import java.util.HashSet;
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

import ccc.commons.Exceptions;
import ccc.persistence.DataRepository;
import ccc.rest.SearchResult;
import ccc.search.AbstractIndexer;
import ccc.search.SearchException;


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

    private final String _indexPath;
    private IndexWriter _writer;


    /**
     * Constructor.
     *
     * @param dm The file repository used to read file resources.
     * @param indexPath The path to the index file on disk.
     */
    public SimpleLuceneFS(final DataRepository dm,
                          final String indexPath)  {
        super(dm);
        _indexPath = indexPath;
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
                             final int nofOfResultsPerPage,
                             final int pageNo) {
        if (searchTerms == null || searchTerms.trim().equals("")) {
            return
                new SearchResult(
                    new HashSet<UUID>(),
                    0,
                    nofOfResultsPerPage,
                    searchTerms,
                    pageNo);
        }

        final String field = "content";
        final int maxHits = (pageNo+1)*nofOfResultsPerPage;
        final CapturingHandler capturingHandler =
            new CapturingHandler(nofOfResultsPerPage, pageNo);

        find(searchTerms, field, maxHits, capturingHandler);

        return new SearchResult(
            capturingHandler.getHits(),
            capturingHandler.getTotalResultsCount(),
            nofOfResultsPerPage,
            searchTerms,
            pageNo);
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
                    Exceptions.swallow(e);
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
