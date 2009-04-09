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

import org.apache.log4j.Logger;

import ccc.commons.DBC;
import ccc.commons.SearchResult;
import ccc.commons.VelocityProcessor;
import ccc.domain.Search;
import ccc.domain.Template;
import ccc.services.SearchEngine;
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
    private final SearchEngine _searchEngine;
    private final String  _terms;
    private final int _pageNumber;

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
                      final SearchEngine searchEngine,
                      final String searchTerms,
                      final int pageNumber) {
        DBC.require().notNull(s);
        DBC.require().notNull(charset);
        DBC.require().notNull(searchEngine);
        DBC.require().notNull(searchTerms);

        _search = s;
        _charset = charset;
        _searchEngine = searchEngine;
        _terms = searchTerms;
        _pageNumber = pageNumber;
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

        final SearchResult result = _searchEngine.find(_terms, 10, _pageNumber);

        final String t = _search.computeTemplate(BUILT_IN_PAGE_TEMPLATE).body();
        final Writer w = new OutputStreamWriter(os, _charset);
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("reader", reader);
        values.put("result", result);
        values.put("pageNumber", _pageNumber);
        values.put("resource", _search);
        values.put("terms", _terms);
        new VelocityProcessor().render(t, w, values);
    }

    private static final Template BUILT_IN_PAGE_TEMPLATE =
        new Template(
            "BUILT_IN_SEARCH_TEMPLATE",
            "BUILT_IN_SEARCH_TEMPLATE",
            "<form name=\"search\" action=\"$resource.name()\">"
            +"<input name=\"q\" autocomplete=\"off\"/>"
            +"<input type=\"submit\" value=\"Search\"  name=\"go\"/>"
            +"</form>Shown Hits: $!result.hits().size() - Total: $!result.totalResults()",
            "<fields/>");
}
