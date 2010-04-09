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
package ccc.plugins.search.lucene;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import ccc.api.SearchResult;
import ccc.api.types.DBC;
import ccc.api.types.MimeType;
import ccc.commons.Exceptions;
import ccc.plugins.search.SearchException;
import ccc.plugins.search.TextExtractor;


/**
 * Implementation of {@link SimpleLucene} operating on a file system index.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleLuceneFS
    implements
        SimpleLucene {

    private static final Version LUCENE_VERSION = Version.LUCENE_CURRENT;
    private static final Logger LOG =
        Logger.getLogger(SimpleLuceneFS.class.getName());

    private final String _indexPath;
    private IndexWriter _writer;


    /**
     * Constructor.
     *
     * @param indexPath The path to the index file on disk.
     */
    public SimpleLuceneFS(final String indexPath)  {
        _indexPath = indexPath;
    }


    private IndexWriter createWriter() throws IOException {
        final IndexWriter writer =
            new IndexWriter(
                FSDirectory.open(new java.io.File(_indexPath)),
                new StandardAnalyzer(LUCENE_VERSION),
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

    /** {@inheritDoc} */
    @Override
    public SearchResult similar(final String uuid,
                                final int nofOfResultsPerPage,
                                final int pageNo) {

        final int maxHits = (pageNo+1)*nofOfResultsPerPage;

        final CapturingHandler capturingHandler =
            new CapturingHandler(nofOfResultsPerPage, pageNo);
        similar(uuid, maxHits, capturingHandler);

        return new SearchResult(
            capturingHandler.getHits(),
            capturingHandler.getTotalResultsCount(),
            nofOfResultsPerPage,
            "uuid",
            pageNo);

    }


    private void find(final String searchTerms,
                      final String field,
                      final int maxHits,
                      final CapturingHandler sh) {
        IndexSearcher searcher = null;
        try {
            searcher =
                new IndexSearcher(
                    FSDirectory.open(new java.io.File(_indexPath)));

            final TopDocs docs =
                searcher.search(
                    new QueryParser(
                        LUCENE_VERSION,
                        field,
                        new StandardAnalyzer(LUCENE_VERSION))
                    .parse(searchTerms),
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


    private void similar(final String uuid,
                         final int maxHits,
                         final CapturingHandler ch) {
        if(uuid == null) {
            return;
        }
        IndexReader ir = null;
        IndexSearcher searcher = null;
        try {
            ir = IndexReader.open(
                FSDirectory.open(new java.io.File(_indexPath)));
            searcher = new IndexSearcher(ir);
            final int docNum = docNumber(uuid, searcher);

            if (docNum == -1) {
                return;
            }
            final MoreLikeThis mlt = new MoreLikeThis(ir);
            mlt.setFieldNames(new String[] {"content"});
            mlt.setMinDocFreq(2);
            final Query query = mlt.like(docNum);
            ch.handle(searcher, searcher.search(query, maxHits));
        } catch (final IOException e) {
            LOG.warn("Error performing query.", e);
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (final IOException e) {
                    Exceptions.swallow(e);
                }
            }
            if (ir != null) {
                try {
                    ir.close();
                } catch (final IOException e) {
                    Exceptions.swallow(e);
                }
            }
        }
    }

    /**
     * Retrieves lucene document for given page.
     *
     * @param uuid UUID to search.
     * @param searcher IndexSearcher object.
     * @return Document number.
     * @throws IOException If search fails
     */
    private int docNumber(final String uuid, final IndexSearcher searcher)
    throws IOException {
        final Query q = new TermQuery(new Term("id", uuid));
        final TopDocs hits = searcher.search(q, 1);

        if (hits.scoreDocs.length < 1) {
            return -1;
        }
        return hits.scoreDocs[0].doc;
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
                LUCENE_VERSION,
                "*",
                new StandardAnalyzer(LUCENE_VERSION))
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
            if (null!=_writer) { _writer.rollback(); }
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
    public void createDocument(final UUID id, final String content) {
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
                    Field.Index.ANALYZED,
                    Field.TermVector.YES));
            _writer.addDocument(d);
            LOG.debug("Added document.");

        } catch (final IOException e) {
            LOG.warn("Error adding document.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public TextExtractor createExtractor(final MimeType mimeType) {
        DBC.require().notNull(mimeType);

        final String primaryType = mimeType.getPrimaryType();
        final String subType     = mimeType.getSubType();

        if ("pdf".equalsIgnoreCase(subType)) {
            return new PdfLoader();

        } else if ("msword".equalsIgnoreCase(subType)) {//no MS2007 support
            return new WordExtractor();

        } else if ("text".equalsIgnoreCase(primaryType)) {
            return new TxtExtractor();

        } else {
            return null;

        }
    }
}
