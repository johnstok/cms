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
package ccc.web.rendering;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import ccc.api.SearchEngine;
import ccc.api.SearchResult;
import ccc.api.ServiceLocator;
import ccc.commons.Exceptions;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.Script;
import ccc.plugins.scripting.TextProcessor;
import ccc.types.DBC;


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

    private final Script _template;


    /**
     * Constructor.
     *
     * @param t The template to use for this body.
     */
    public SearchBody(final Script t) {
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
        final ServiceLocator sl = context.get("services", ServiceLocator.class);
        final SearchEngine searchEngine = sl.getSearch();

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

        final Script templateString = _template;
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


//    /** BUILT_IN_SEARCH_TEMPLATE : Template. */
//    public static final Template BUILT_IN_SEARCH_TEMPLATE =
//        new Template(
//            "BUILT_IN_SEARCH_TEMPLATE",
//            "BUILT_IN_SEARCH_TEMPLATE",
//            "<form name=\"search\" action=\"$resource.name()\">"
//            +"<input name=\"q\" autocomplete=\"off\"/>"
//            +"<input type=\"submit\" value=\"Search\"  name=\"go\"/>"
//            +"</form>Shown Hits: $!result.hits().size() - "
//            +"Total: $!result.totalResults()",
//            "<fields/>",
//            MimeType.HTML,
//            new RevisionMetadata(
//                new Date(),
//                User.SYSTEM_USER,
//                true,
//                "Created."));
}
