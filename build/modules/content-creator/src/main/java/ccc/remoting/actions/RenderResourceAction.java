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
package ccc.remoting.actions;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.FileRepository;
import ccc.rendering.DefaultRenderer;
import ccc.rendering.Renderer;
import ccc.rendering.Response;
import ccc.rendering.StatefulReader;
import ccc.rendering.velocity.VelocityProcessor;
import ccc.search.SearchEngine;


/**
 * A servlet action that renders a resource to servlet response.
 *
 * @author Civic Computing Ltd.
 */
public class RenderResourceAction
    extends
        AbstractServletAction {

    private final boolean _respectVisiblity;
    private final SearchEngine _search;

    /**
     * Constructor.
     *
     * @param respectVisiblity Should we respect the visibility of resources,
     *  as specified by their published status.
     * @param search The search engine to use.
     */
    public RenderResourceAction(final boolean respectVisiblity,
                                final SearchEngine search) {
        _respectVisiblity = respectVisiblity;
        _search = search;
    }


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest request,
                        final HttpServletResponse response) throws IOException {
        final FileRepository data = getDataManager(request);
        final StatefulReader reader = getStatefulReader(request);
        final User currentUser = getCurrentUser(request);

        final Resource rs = getResource(request);

        final Response r =
            prepareResponse(request, reader, data, _search, rs);

        if (rs.roles().size()>0) { // Dont'cache secure pages.
            r.setExpiry(null);
        }

        r.write(response, currentUser, new VelocityProcessor());
    }


    @SuppressWarnings("unchecked")
    private Response prepareResponse(final HttpServletRequest request,
                                     final StatefulReader reader,
                                     final FileRepository dataMgr,
                                     final SearchEngine searchEngine,
                                     final Resource rs) {

        final Map<String, String[]> parameters = request.getParameterMap();
        final Renderer renderer =
            new DefaultRenderer(
                dataMgr,
                searchEngine,
                reader,
                _respectVisiblity);

        final Response r;
        if (parameters.keySet().contains("wc")) {
            r = renderer.renderWorkingCopy(rs, parameters);
        } else if (parameters.keySet().contains("v")) {
            r = renderer.renderHistoricalVersion(rs, parameters);
        } else {
            r = renderer.render(rs, parameters);
        }
        return r;
    }
}
