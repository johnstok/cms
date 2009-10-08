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

import javax.servlet.http.HttpServletRequest;

import ccc.commons.Exceptions;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.search.SearchEngine;
import ccc.search.SearchResult;
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

    private final Template _template;


    /**
     * Constructor.
     *
     * @param t The template to use for this body.
     */
    public SearchBody(final Template t) {
        DBC.require().notNull(t);
        _template = t;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final TextProcessor processor) {

        String searchQuery = "";
        final HttpServletRequest request =
            context.get("request", HttpServletRequest.class);
        final SearchEngine searchEngine =
            context.get("search", SearchEngine.class);

        final String[] qParams = request.getParameterValues("q");
        if (qParams != null && qParams.length != 0) {
            searchQuery = qParams[0];
        }

        final int pageNumber =
            getScalarInt(
                request.getParameterValues("p"),
                DEFAULT_FIRST_PAGE);
        final int noOfResultsPerPage =
            getScalarInt(
                request.getParameterValues("r"),
                DEFAULT_MINIMUM_SEARCH_RESULTS);

        final SearchResult result =
            searchEngine.find(searchQuery, noOfResultsPerPage, pageNumber);
        context.add("result", result);

        final String templateString = _template.body();
        final Writer w = new OutputStreamWriter(os, charset);

        processor.render(templateString, w, context);
    }


    private int getScalarInt(final String[] pParams, final int defaultValue) {
        int scalarInt = defaultValue;
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
