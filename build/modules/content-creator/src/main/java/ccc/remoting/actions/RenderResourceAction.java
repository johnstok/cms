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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.ScriptRunner;
import ccc.domain.File;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.persistence.DataRepository;
import ccc.persistence.streams.ReadToStringAction;
import ccc.remoting.RequestScopeServiceLocator;
import ccc.rendering.Context;
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
    private final SearchEngine _search; // FIXME: Already attached to the request!

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

        final DataRepository data = getDataManager(request);
        final StatefulReader reader = getStatefulReader(request);
        final User currentUser = getCurrentUser(request);
        final Resource rs = getResource(request);

        if (rs instanceof File) {
            final File f = (File) rs;

            if (f.isText() && f.isExecutable()) {
                final String fileContent = ReadToStringAction.read(data, f);
                invokeScript(request, response, fileContent);

            } else {
                renderResource(
                    request, response, data, reader, currentUser, rs);
            }
        } else {
            renderResource(request, response, data, reader, currentUser, rs);
        }


    }


    private void renderResource(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final DataRepository data,
                                final StatefulReader reader,
                                final User currentUser,
                                final Resource rs) throws IOException {

        final Context context = new Context();
        context.add("user", currentUser);
        context.add("request",  request);
        context.add("response", response);
        context.add("search", _search);
        context.add("data", data);
        context.add("reader", reader);
        context.add("services", new RequestScopeServiceLocator(request));

        final Response r = prepareResponse(request, rs, context);

        if (rs.roles().size()>0) { // Dont'cache secure pages.
            r.setExpiry(null);
        }

        r.write(response, context, new VelocityProcessor());
    }


    @SuppressWarnings("unchecked")
    private Response prepareResponse(final HttpServletRequest request,
                                     final Resource rs,
                                     final Context context) {

        final Map<String, String[]> parameters = request.getParameterMap();
        final Renderer renderer = new DefaultRenderer(_respectVisiblity);

        final Response r;
        if (parameters.keySet().contains("wc")) {
            r = renderer.renderWorkingCopy(rs, context);
        } else if (parameters.keySet().contains("v")) {
            r = renderer.renderHistoricalVersion(
                rs, request.getParameter("v"), context);
        } else {
            r = renderer.render(rs, context);
        }
        return r;
    }


    private void disableCaching(final HttpServletResponse resp) {
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-store, must-revalidate, max-age=0");
        resp.setDateHeader("Expires", new Date(0).getTime());
    }


    private void invokeScript(final HttpServletRequest req,
                              final HttpServletResponse resp,
                              final String script) {
        try {
            disableCaching(resp);

            final Map<String, Object> context = new HashMap<String, Object>();
            context.put("request",  req);
            context.put("response", resp);
            context.put("services", new RequestScopeServiceLocator(req));
            context.put("user", getCurrentUser(req));

            new ScriptRunner().eval(script, context, resp.getWriter());

        } catch (final ScriptException e) {
            throw new RuntimeException("Error invoking script.", e);
        } catch (final IOException e) {
            throw new RuntimeException("Error invoking script.", e);
        }
    }
}
