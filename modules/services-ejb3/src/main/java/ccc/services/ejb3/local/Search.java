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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;

import ccc.domain.Page;
import ccc.services.ISearch;


/**
 * Lucene Implementation of the {@link ISearch} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="Search")
@TransactionAttribute(REQUIRED)
@Local(ISearch.class)
public class Search  implements ISearch {

    private static final Logger LOG = Logger.getLogger(Search.class.getName());

    /** Constructor. */
    @SuppressWarnings("unused") public Search() { super(); }


    /** {@inheritDoc} */
    @Override
    public Set<UUID> find(final String searchTerms) {
        try {
            final IndexSearcher searcher =
                new IndexSearcher("C:\\WINDOWS\\Temp\\lucene"); // FIXME
            final TopDocs docs =
                searcher.search(
                    new QueryParser(
                        "title",
                        new StandardAnalyzer()).parse(searchTerms),
                        1000);

            final Set<UUID> hits = new HashSet<UUID>();
            for (final ScoreDoc doc : docs.scoreDocs) {
                hits.add(
                    UUID.fromString(
                        searcher.doc(doc.doc).getField("id").stringValue()));
            }

            searcher.close();

            return hits;

        } catch (final CorruptIndexException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void index(final Page page) {
        try {
            final File f = new File("C:\\WINDOWS\\Temp\\lucene"); // FIXME
            final IndexWriter writer =
                new IndexWriter(
                    f,
                    new StandardAnalyzer(),
                    IndexWriter.MaxFieldLength.LIMITED);
            final Document d = new Document();
            d.add(
                new Field(
                    "id",
                    page.id().toString(),
                    Field.Store.YES,
                    Field.Index.ANALYZED));
            d.add(
                new Field(
                    "title",
                    page.title(),
                    Field.Store.YES,
                    Field.Index.ANALYZED));
            writer.addDocument(d);
            writer.optimize();
            writer.close();
            LOG.info("Indexed: "+page.title());
        } catch (final CorruptIndexException e) {
            throw new RuntimeException(e);
        } catch (final LockObtainFailedException e) {
            throw new RuntimeException(e);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
