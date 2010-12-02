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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

import ccc.api.core.ACL;
import ccc.api.core.File;
import ccc.api.core.Page;
import ccc.api.exceptions.CCException;
import ccc.api.types.DBC;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;
import ccc.commons.Exceptions;
import ccc.plugins.PluginFactory;
import ccc.plugins.search.TextExtractor;


/**
 * Implementation of {@link SimpleLucene} operating on a file system index.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleLuceneFS
    implements
        SimpleLucene {

    private static final String  SORT_FIELD_PREFIX = "_";
    private static final Version LUCENE_VERSION = Version.LUCENE_30;
    private static final String  DEFAULT_FIELD = "allcontent";
    private static final String  ACL_FIELD = SORT_FIELD_PREFIX+"acl";
    private static final Logger  LOG =
        Logger.getLogger(SimpleLuceneFS.class.getName());

    private final String _indexPath;
    private IndexWriter _writer;
    private final Locale _locale = Locale.US;


    /**
     * Constructor.
     *
     * @param indexPath The path to the index file on disk.
     */
    public SimpleLuceneFS(final String indexPath)  {
        _indexPath = indexPath;
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final int nofOfResultsPerPage,
                             final int pageNo) {
        final int page = pageNo - 1;
        if (searchTerms == null || searchTerms.trim().equals("")) {
            return
                new SearchResult(
                    new HashSet<UUID>(),
                    0,
                    nofOfResultsPerPage,
                    searchTerms,
                    page);
        }

        final int maxHits = (page+1)*nofOfResultsPerPage;
        final CapturingHandler capturingHandler =
            new CapturingHandler(nofOfResultsPerPage, page);

        find(searchTerms, maxHits, null, null, capturingHandler);

        return new SearchResult(
            capturingHandler.getHits(),
            capturingHandler.getTotalResultsCount(),
            nofOfResultsPerPage,
            searchTerms,
            page);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final String sort,
                             final SortOrder order,
                             final int nofOfResultsPerPage,
                             final int pageNo) {
        final int page = pageNo - 1;
        final Sort sorter =
            (null==sort)
                ? null
                : new Sort(
                    new SortField(
                        sort, SortField.STRING_VAL, (SortOrder.DESC==order)));

        if (searchTerms == null || searchTerms.trim().equals("")) {
            return
                new SearchResult(
                    new HashSet<UUID>(),
                    0,
                    nofOfResultsPerPage,
                    searchTerms,
                    page);
        }

        final int maxHits = (page+1)*nofOfResultsPerPage;
        final CapturingHandler capturingHandler =
            new CapturingHandler(nofOfResultsPerPage, page);

        find(searchTerms, maxHits, sorter, null, capturingHandler);

        return new SearchResult(
            capturingHandler.getHits(),
            capturingHandler.getTotalResultsCount(),
            nofOfResultsPerPage,
            searchTerms,
            page);

    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final String sort,
                             final SortOrder order,
                             final ACL userPerms,
                             final int nofOfResultsPerPage,
                             final int pageNo) {
        final int page = pageNo - 1;
        final Sort sorter =
            (null==sort)
                ? null
                : new Sort(
                    new SortField(
                        sort, SortField.STRING_VAL, (SortOrder.DESC==order)));

        if (searchTerms == null || searchTerms.trim().equals("")) {
            return
            new SearchResult(
                new HashSet<UUID>(),
                0,
                nofOfResultsPerPage,
                searchTerms,
                page);
        }

        final int maxHits = (page+1)*nofOfResultsPerPage;
        final CapturingHandler capturingHandler =
            new CapturingHandler(nofOfResultsPerPage, page);


        find(searchTerms, maxHits, sorter, userPerms, capturingHandler);


        return new SearchResult(
            capturingHandler.getHits(),
            capturingHandler.getTotalResultsCount(),
            nofOfResultsPerPage,
            searchTerms,
            page);

    }


    /** {@inheritDoc} */
    @Override
    public SearchResult similar(final String uuid,
                                final int nofOfResultsPerPage,
                                final int pageNo) {
        final int page = pageNo - 1;
        final int maxHits = (page+1)*nofOfResultsPerPage;

        final CapturingHandler capturingHandler =
            new CapturingHandler(nofOfResultsPerPage, page);
        similar(uuid, maxHits, capturingHandler);

        return new SearchResult(
            capturingHandler.getHits(),
            capturingHandler.getTotalResultsCount(),
            nofOfResultsPerPage,
            "uuid",
            page);

    }


    private void find(final String searchTerms,
                      final int maxHits,
                      final Sort sorter,
                      final ACL userPerms,
                      final CapturingHandler sh) {
        IndexSearcher searcher = null;

        try {
            searcher =
                new IndexSearcher(
                    FSDirectory.open(new java.io.File(_indexPath)));

            TopDocs docs;
            if (null==sorter) {
                docs = searcher.search(
                    createParser().parse(searchTerms),
                    new AclFilter(
                        ACL_FIELD,
                        (null==userPerms) ? new ACL() : userPerms),
                    maxHits);
            } else {
                docs = searcher.search(
                    createParser().parse(searchTerms),
                    new AclFilter(
                        ACL_FIELD,
                        (null==userPerms) ? new ACL() : userPerms),
                    maxHits,
                    sorter);
            }

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
            mlt.setFieldNames(new String[] {DEFAULT_FIELD});
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
     * Removes all entries for a specific document id from the lucene index.
     *
     * @param id The document id to clear.
     *
     * @throws IOException If index writing fails.
     */
    private void clearDocuments(final UUID id) throws IOException {
        _writer.deleteDocuments(new TermQuery(new Term("id", id.toString())));
        _writer.expungeDeletes();
        LOG.debug("Deleted all existing documents with id: "+id);
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
        LOG.debug("Deleted all existing documents.");
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
            LOG.debug("Commited index update.");
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
            LOG.debug("Rolled back index update.");
        } catch (final IOException e) {
            _writer = null;
            LOG.error("Error rolling back lucene write.", e);
        }
    }


    /** {@inheritDoc}*/
    @Override
    public void startUpdate() {
        try {
            _writer = createWriter();
            clearIndex();
            LOG.debug("Starting index update.");
        } catch (final IOException e) {
            throw new CCException("Failed to start index update.", e);
        } catch (final ParseException e) {
            throw new CCException("Failed to start index update.", e);
        }
    }


    /** {@inheritDoc}*/
    @Override
    public void startAddition() {
        try {
            _writer = createWriter();
            LOG.debug("Starting index addition.");
        } catch (final IOException e) {
            throw new CCException("Failed to start index update.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void createDocument(final Page p, final Collection<ACL> acl) {
        createDocument(
            p.getId(),
            new ResourcePath(p.getAbsolutePath()),
            p.getName(),
            p.getTitle(),
            p.getTags(),
            extractContent(p),
            p.getParagraphs(),
            p.getType(),
            p.getDateCreated(),
            p.getDateChanged(),
            acl);
    }


    /** {@inheritDoc} */
    @Override
    public void createDocument(final File f, final Collection<ACL> acl) {
        createDocument(
            f.getId(),
            new ResourcePath(f.getAbsolutePath()),
            f.getName(),
            f.getTitle(),
            f.getTags(),
            f.getContent(),
            null,
            f.getType(),
            f.getDateCreated(),
            f.getDateChanged(),
            acl);
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


    /**
     * Add a document to the search index.
     *
     * @param id The resource's ID.
     * @param path The resource's absolute path.
     * @param name The resource's name.
     * @param title The resource's title.
     * @param tags The resource's tags.
     * @param content The document's content.
     * @param paragraphs The paragraphs of the document.
     */
    void createDocument(final UUID id,
                        final ResourcePath path,
                        final ResourceName name,
                        final String title,
                        final Set<String> tags,
                        final String content,
                        final Set<Paragraph> paragraphs) {
        createDocument(
            id,
            path,
            name,
            title,
            tags,
            content,
            paragraphs,
            new ArrayList<ACL>());
    }


    /**
     * Add a document to the search index.
     *
     * @param id         The resource's ID.
     * @param path       The resource's absolute path.
     * @param name       The resource's name.
     * @param title      The resource's title.
     * @param tags       The resource's tags.
     * @param content    The document's content.
     * @param paragraphs The paragraphs of the document.
     * @param acl        The ACL for the document.
     */
    void createDocument(final UUID id,
                        final ResourcePath path,
                        final ResourceName name,
                        final String title,
                        final Set<String> tags,
                        final String content,
                        final Set<Paragraph> paragraphs,
                        final Collection<ACL> acl) {
        createDocument(
            id,
            path,
            name,
            title,
            tags,
            content,
            paragraphs,
            null,
            null,
            null,
            acl);
    }



    private void createDocument(final UUID id,
                                final ResourcePath path,
                                final ResourceName name,
                                final String title,
                                final Set<String> tags,
                                final String content,
                                final Set<Paragraph> paragraphs,
                                final ResourceType type,
                                final Date dateCreated,
                                final Date dateChanged,
                                final Collection<ACL> acl) {
        try {
            clearDocuments(id);

            final Document d = new Document();

            if (paragraphs != null) {
                for (final Paragraph paragraph : paragraphs) {
                    indexParagraph(d, paragraph);
                }
            }


            final byte[] s11nAcl = AclFilter.serialise(acl);
            d.add(
                new Field(
                    ACL_FIELD,
                    s11nAcl,
                    0,
                    s11nAcl.length,
                    Field.Store.YES));
            d.add(
                new Field(
                    DEFAULT_FIELD,
                    content,
                    Field.Store.NO,
                    Field.Index.ANALYZED,
                    Field.TermVector.YES));
            d.add(
                new Field(
                    "id",
                    id.toString(),
                    Field.Store.YES,
                    Field.Index.NOT_ANALYZED));
            d.add(
                new Field(
                    "path",
                    "/content"+path.toString().toLowerCase(_locale),
                    Field.Store.NO,
                    Field.Index.NOT_ANALYZED));
            d.add(
                new Field(
                    "name",
                    name.toString().toLowerCase(_locale),
                    Field.Store.NO,
                    Field.Index.NOT_ANALYZED));
            addEnum(d, "type", type);

            addStringField(d, "title", title);
            addTagsField(d, "tags", tags);
            addDateField(d, "date_created", dateCreated);
            addDateField(d, "date_changed", dateChanged);

            _writer.addDocument(d);
            LOG.debug("Added document.");

        } catch (final IOException e) {
            LOG.warn("Error adding document.", e);
        }
    }


    private void addEnum(final Document d,
                         final String fieldName,
                         final Enum<?> fieldValue) {
        if (null==fieldValue) { return; }
        d.add(
            new Field(
                fieldName,
                fieldValue.name().toLowerCase(_locale),
                Field.Store.NO,
                Field.Index.NOT_ANALYZED));
    }


    private void indexParagraph(final Document d, final Paragraph paragraph) {
        if ((paragraph.getType() == ParagraphType.TEXT
            || paragraph.getType() == ParagraphType.LIST)
            && paragraph.getText() != null) {
            addStringField(
                d,
                paragraph.getName(),
                new PluginFactory().html().cleanUpContent(paragraph.getText()));

        } else if (paragraph.getType() == ParagraphType.NUMBER
                   && paragraph.getNumber() != null) {
            addDoubleField(
                d,
                paragraph.getName(),
                paragraph.getNumber().doubleValue());

        } else if (paragraph.getType() == ParagraphType.DATE
                   && paragraph.getDate() != null) {
           addDateField(
               d,
               paragraph.getName(),
               paragraph.getDate());

        } else if (paragraph.getType() == ParagraphType.BOOLEAN
                   && paragraph.getBoolean() != null) {
            addBooleanField(
                d,
                paragraph.getName(),
                paragraph.getBoolean().booleanValue());
        } else if (paragraph.getType() == ParagraphType.TAXONOMY
                && paragraph.getList() != null) {
           for (final String term : paragraph.getList()) {
               d.add(
                   new Field(
                       paragraph.getName(),
                       term,
                       Field.Store.NO,
                       Field.Index.NOT_ANALYZED));
           }

        }
    }


    private void addTagsField(final Document d,
                              final String fieldName,
                              final Collection<String> fieldValue) {
        for (final String tag : fieldValue) {
            d.add(
                new Field(
                    fieldName,
                    tag,
                    Field.Store.NO,
                    Field.Index.NOT_ANALYZED));
        }
    }


    private void addStringField(final Document d,
                                final String fieldName,
                                final String fieldValue) {
        d.add(
            new Field(
                fieldName,
                fieldValue,
                Field.Store.NO,
                Field.Index.ANALYZED));
        d.add(
            new Field(
                SORT_FIELD_PREFIX+fieldName,
                (null==fieldValue) ? null : fieldValue.toLowerCase(_locale),
                Field.Store.NO,
                Field.Index.NOT_ANALYZED));
    }


    private void addDoubleField(final Document d,
                                final String fieldName,
                                final double fieldValue) {
        final NumericField nf =
            new NumericField(fieldName, Field.Store.NO, true);
        nf.setDoubleValue(fieldValue);
        d.add(nf);
        d.add(
            new Field(
                SORT_FIELD_PREFIX+fieldName,
                NumericUtils.doubleToPrefixCoded(fieldValue),
                Field.Store.NO,
                Field.Index.NOT_ANALYZED));
    }


    private void addDateField(final Document d,
                              final String fieldName,
                              final Date fieldValue) {
        if (null==fieldValue) { return; }
        final NumericField nf =
            new NumericField(fieldName, Field.Store.NO, true);
        nf.setLongValue(fieldValue.getTime());
        d.add(nf);
        d.add(
            new Field(
                SORT_FIELD_PREFIX+fieldName,
                NumericUtils.longToPrefixCoded(fieldValue.getTime()),
                Field.Store.NO,
                Field.Index.NOT_ANALYZED));
    }


    private void addBooleanField(final Document d,
                                 final String fieldName,
                                 final boolean fieldValue) {
        d.add(
            new Field(
                fieldName,
                ""+fieldValue,
                Field.Store.NO,
                Field.Index.ANALYZED));
        d.add(
            new Field(
                SORT_FIELD_PREFIX+fieldName,
                ""+fieldValue,
                Field.Store.NO,
                Field.Index.NOT_ANALYZED));
    }


    private QueryParser createParser() {
        final PerFieldAnalyzerWrapper wrapper =
            new PerFieldAnalyzerWrapper(new StandardAnalyzer(LUCENE_VERSION));

        wrapper.addAnalyzer("id", new KeywordAnalyzer());
        wrapper.addAnalyzer("tag", new KeywordAnalyzer());
        wrapper.addAnalyzer("path", new KeywordAnalyzer());
        wrapper.addAnalyzer("name", new KeywordAnalyzer());

        final QueryParser qp =
            new CCQueryParser(LUCENE_VERSION, DEFAULT_FIELD, wrapper);

        return qp;
    }


    private IndexWriter createWriter() throws IOException {
        final IndexWriter writer =
            new IndexWriter(
                FSDirectory.open(new java.io.File(_indexPath)),
                new StandardAnalyzer(LUCENE_VERSION),
                IndexWriter.MaxFieldLength.UNLIMITED);
        return writer;
    }


    private String extractContent(final Page page) {
        final StringBuilder sb = new StringBuilder(page.getTitle());
        for (final Paragraph p : page.getParagraphs()) {
            if (ParagraphType.TEXT == p.getType() && p.getText() != null) {
                sb.append(" ");
                sb.append(
                    new PluginFactory().html().cleanUpContent(p.getText()));
            }
        }
        return sb.toString();
    }
}
