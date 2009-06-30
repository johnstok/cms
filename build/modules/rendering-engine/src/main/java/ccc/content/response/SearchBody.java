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
package ccc.content.response;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ccc.api.DBC;
import ccc.api.MimeType;
import ccc.commons.SearchResult;
import ccc.content.velocity.VelocityProcessor;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.SearchEngine;
import ccc.services.StatefulReader;
import ccc.snapshots.ResourceSnapshot;


/**
 * The class can write a Search object as the body of a {@link Response}. The
 * body will be written as html text, with the specified character set.
 *
 * @author Civic Computing Ltd.
 */
public class SearchBody
    implements
        Body {

    private final ResourceSnapshot  _search;
    private final StatefulReader _reader;
    private final SearchEngine _searchEngine;
    private final String  _terms;
    private final int _pageNumber;
    private final Template _template;

    /**
     * Constructor.
     *
     * @param search The search to render.
     * @param reader A stateful reader to access other resources.
     * @param searchEngine The engine used to perform the search.
     * @param searchTerms The query terms supplied to the request.
     * @param pageNumber The page of results required.
     * @param t The template to use for this body.
     */
    public SearchBody(final ResourceSnapshot search,
                      final StatefulReader reader,
                      final SearchEngine searchEngine,
                      final String searchTerms,
                      final Template t,
                      final int pageNumber) {
        DBC.require().notNull(search);
        DBC.require().notNull(reader);
        DBC.require().notNull(searchEngine);
        DBC.require().notNull(searchTerms);
        DBC.require().notNull(t);

        _search = search;
        _reader = reader;
        _searchEngine = searchEngine;
        _terms = searchTerms;
        _pageNumber = pageNumber;
        _template = t;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset) {

        final SearchResult result = _searchEngine.find(_terms, 10, _pageNumber);

        final String templateString = _template.body();
        final Writer w = new OutputStreamWriter(os, charset);
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("reader", _reader);
        values.put("result", result);
        values.put("pageNumber", Integer.valueOf(_pageNumber));
        values.put("resource", _search);
        values.put("terms", _terms);
        new VelocityProcessor().render(templateString, w, values);
    }


    /** BUILT_IN_SEARCH_TEMPLATE : Template. */
    static final Template BUILT_IN_SEARCH_TEMPLATE =
        new Template(
            "BUILT_IN_SEARCH_TEMPLATE",
            "BUILT_IN_SEARCH_TEMPLATE",
            "<form name=\"search\" action=\"$resource.name()\">"
            +"<input name=\"q\" autocomplete=\"off\"/>"
            +"<input type=\"submit\" value=\"Search\"  name=\"go\"/>"
            +"</form>Shown Hits: $!result.hits().size() - "
            +"Total: $!result.totalResults()",
            "<fields/>",
            MimeType.HTML,
            new RevisionMetadata(
                new Date(),
                User.SYSTEM_USER,
                true,
                "Created."));
}
