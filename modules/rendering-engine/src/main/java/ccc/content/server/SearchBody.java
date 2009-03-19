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
package ccc.content.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.commons.DBC;
import ccc.commons.VelocityProcessor;
import ccc.domain.Search;
import ccc.domain.Template;
import ccc.services.ISearch;
import ccc.services.StatefulReader;


/**
 * The class can write a Search object as the body of a {@link Response}. The
 * body will be written as html text, with the specified character set.
 *
 * @author Civic Computing Ltd.
 */
public class SearchBody
    implements
        Body {
    private static final Logger LOG = Logger.getLogger(SearchBody.class);

    private final Search  _search;
    private final Charset _charset;
    private final ISearch _searchEngine;
    private final String  _terms;

    /**
     * Constructor.
     *
     * @param s The search to render.
     * @param charset The character set used when writing the page to an
     *  {@link OutputStream}.
     * @param searchEngine The engine used to perform the search.
     */
    public SearchBody(final Search s,
                      final Charset charset,
                      final ISearch searchEngine,
                      final String searchTerms) {
        DBC.require().notNull(s);
        DBC.require().notNull(charset);
        DBC.require().notNull(searchEngine);
        DBC.require().notNull(searchTerms);

        _search = s;
        _charset = charset;
        _searchEngine = searchEngine;
        _terms = searchTerms;
    }

    /** {@inheritDoc} */
    @Override
    public Search getResource() {
        return _search;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final StatefulReader reader) throws IOException {

        final Set<UUID> hits = _searchEngine.find(_terms);

        final String t = _search.computeTemplate(BUILT_IN_PAGE_TEMPLATE).body();
        final Writer w = new OutputStreamWriter(os, _charset);
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("reader", reader);
        values.put("hits", hits);
        new VelocityProcessor().render(t, w, values);
    }

    private static final Template BUILT_IN_PAGE_TEMPLATE =
        new Template(
            "BUILT_IN_SEARCH_TEMPLATE",
            "BUILT_IN_SEARCH_TEMPLATE",
            "Hits: $resource.size()",
            "<fields/>");
}
