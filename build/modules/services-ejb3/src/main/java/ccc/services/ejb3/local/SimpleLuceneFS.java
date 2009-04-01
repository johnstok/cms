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
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
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

            final BooleanQuery bq = new BooleanQuery();

            final Query qf = new TermQuery(new Term(field, searchTerms));
            final Query ql = new TermQuery(new Term("roles", "anonymous"));

            bq.add(qf, BooleanClause.Occur.MUST);
            bq.add(ql, BooleanClause.Occur.MUST);

            final TopDocs docs =
                searcher.search(
                    bq,
                        maxHits);

            sh.handle(searcher, docs);

            searcher.close();
        } catch (final IOException e) {
            LOG.warn("Error performing query.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateRolesField(final String id,
                                 final Collection<String> roles) {

        try {
            final IndexSearcher searcher =
                new IndexSearcher(_indexPath);
            final String[] queryp = {id};
            final String[] fields = {"id"};
            final BooleanClause.Occur[] flags = {BooleanClause.Occur.MUST};
            final Query query = MultiFieldQueryParser.parse(
                queryp,
                fields,
                flags,
                new StandardAnalyzer());

            final TopDocs td = searcher.search(query, 1);
            final ScoreDoc[] sd = td.scoreDocs;
            if (sd != null && sd.length == 1){
                final Document doc = searcher.doc(sd[0].doc);
                doc.removeField("roles");

                final StringBuffer rolesField = new StringBuffer();
                if (roles != null && roles.size() > 0) {
                    for (final String role : roles) {
                        if (rolesField.length()!=0) {
                            rolesField.append(",");
                        }
                        rolesField.append(role);
                    }
                } else {
                    rolesField.append("anonymous");
                }
                doc.add(new Field("roles",
                    rolesField.toString(),
                    Field.Store.YES,
                    Field.Index.NOT_ANALYZED));

                remove(id, "id");
                add(doc);
            }
            searcher.close();
        } catch (final IOException e) {
            LOG.warn("Error removing document.", e);
        } catch (final ParseException e) {
            LOG.warn("Error removing document.", e);
        }
    }
}
