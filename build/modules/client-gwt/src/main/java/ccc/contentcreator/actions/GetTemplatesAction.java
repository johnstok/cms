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
package ccc.contentcreator.actions;

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.TemplateSummary;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * Get a list of templates from the server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetTemplatesAction
    extends
        RemotingAction {

    /**
     * Constructor.
     *
     * @param actionName Local-specific name for the action.
     */
    public GetTemplatesAction(final String actionName) {
        super(actionName);
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() { return "/templates"; }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<TemplateSummary> templates =
            new ArrayList<TemplateSummary>();
        for (int i=0; i<result.size(); i++) {
            templates.add(
                new TemplateSummary(new GwtJson(result.get(i).isObject())));
        }

        execute(templates);
    }

    /**
     * Handle the data returned from the server.
     *
     * @param templates The available templates.
     */
    protected abstract void execute(Collection<TemplateSummary> templates);
}