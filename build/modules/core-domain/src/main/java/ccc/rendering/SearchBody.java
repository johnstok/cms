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
package ccc.rendering;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import ccc.commons.Exceptions;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.search.SearchEngine;
import ccc.search.SearchResult;
import ccc.snapshots.SearchSnapshot;
import ccc.types.DBC;
import ccc.types.MimeType;


/**
 * The class can write a Search object as the body of a {@link Response}. The
 * body will be written as html text, with the specified character set.
 *
 * @author Civic Computing Ltd.
 */
public class SearchBody
    implements
        Body {

    private static final int DEFAULT_FIRST_PAGE = 0;
	private static final int DEFAULT_MINIMUM_SEARCH_RESULTS = 10;
	private final StatefulReader _reader;
    private final SearchEngine _searchEngine;
    private final Template _template;
    private final Map<String, String[]> _parameters;
    private final SearchSnapshot _searchResource;

    /**
     * Constructor.
     *
     * @param reader A stateful reader to access other resources.
     * @param searchEngine The engine used to perform the search.
     * @param searchResource The search this body will render.
     * @param parameters The request parameters.
     * @param t The template to use for this body.
     */
    public SearchBody(final StatefulReader reader,
                      final SearchEngine searchEngine,
                      SearchSnapshot searchResource, 
                      final Template t,
                      final Map<String, String[]> parameters) {
        DBC.require().notNull(reader);
        DBC.require().notNull(searchEngine);
        DBC.require().notNull(parameters);
        DBC.require().notNull(t);

        _reader = reader;
        _searchEngine = searchEngine;
        _parameters = parameters;
        _template = t;
        _searchResource = searchResource;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final User user,
                      final TextProcessor processor) {

        String searchQuery = "";
        final String[] qParams = _parameters.get("q");
        if (qParams != null && qParams.length != 0) {
            searchQuery = qParams[0];
        }

        int pageNumber = getScalarInt("p", DEFAULT_FIRST_PAGE);
        int noOfResultsPerPage = getScalarInt("r", DEFAULT_MINIMUM_SEARCH_RESULTS);

        final SearchResult result =
            _searchEngine.find(searchQuery, noOfResultsPerPage, pageNumber);

        final String templateString = _template.body();
        final Writer w = new OutputStreamWriter(os, charset);
        final Context context = new Context(_reader, _searchResource, _parameters);
        context.add("result", result);
        context.add("user", user);

        processor.render(templateString, w, context);
    }

    
	private int getScalarInt(String paramName, int defaultValue) {
		int scalarInt = defaultValue;
        final String[] pParams = _parameters.get(paramName);
        if (pParams != null && pParams.length != 0) {
            try {
                scalarInt = Integer.parseInt(pParams[0]);
            } catch (final NumberFormatException e) {
                Exceptions.swallow(e);
            }
        }
		return scalarInt;
	}


    /** BUILT_IN_SEARCH_TEMPLATE : Template. */
    public static final Template BUILT_IN_SEARCH_TEMPLATE =
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
