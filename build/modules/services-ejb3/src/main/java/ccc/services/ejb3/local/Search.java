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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import ccc.domain.Page;
import ccc.domain.Paragraph;
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


    private static final Logger LOG = Logger.getLogger(Search.class.getName());

    private SimpleLucene _lucene;

    /** Constructor. */
    @SuppressWarnings("unused") public Search() {
        _lucene = new SimpleLuceneFS();
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
    public void update(final Page page) {
        delete(page);
        add(page);
    }


    private void delete(final Page page) {
        _lucene.remove(page.id().toString(), "id");
    }


    /** {@inheritDoc} */
    @Override
    public void add(final Page page) {
        final Document d = createDocument(page);
        _lucene.add(d);
        LOG.info("Indexed: "+page.title());
    }


    private Document createDocument(final Page page) {

        final Document d = new Document();
        d.add(
            new Field(
                "id",
                page.id().toString(),
                Field.Store.YES,
                Field.Index.NOT_ANALYZED));
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
                Field.Store.NO,
                Field.Index.ANALYZED));
        return d;
    }
}
