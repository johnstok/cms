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

import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.PageDelta;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class PageDeltaAction
    extends
        RemotingAction {

    private final UUID _id;

    /**
     * Constructor.
     *
     * @param actionName Name of the action.
     * @param id The UUID.
     */
    public PageDeltaAction(final String actionName, final UUID id) {
        super(actionName);
        _id = id;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/pages/" + _id + "/delta";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final PageDelta delta = new PageDelta(new GwtJson(result));
        execute(delta);
    }

    /**
     * Handle a successful execution.
     *
     * @param delta The delta returned by the server.
     */
    protected abstract void execute(PageDelta delta);
}
