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
import ccc.rest.dto.ResourceSummary;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetRootsAction
    extends
        RemotingAction {

    /**
     * Constructor.
     */
    public GetRootsAction() {
        super(GLOBALS.userActions().internalAction());
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray results =
            JSONParser.parse(response.getText()).isArray();
        final Collection<ResourceSummary> roots =
            new ArrayList<ResourceSummary>();
        for (int i=0; i<results.size(); i++) {
            roots.add(
                new ResourceSummary(
                    new GwtJson(results.get(i).isObject())));
        }
        onSuccess(roots);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/roots";
    }


    /**
     * Execute this action.
     *
     * @param roots The root resources returned by the server.
     */
    protected abstract void onSuccess(final Collection<ResourceSummary> roots);
}
