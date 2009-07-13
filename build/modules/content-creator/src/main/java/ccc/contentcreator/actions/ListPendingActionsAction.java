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

import ccc.api.ActionSummary;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListPendingActionsAction
    extends
        RemotingAction {

    /**
     * Constructor.
     *
     * @param actionName
     */
    public ListPendingActionsAction(final String actionName) {
        super(actionName);
    }

    /** {@inheritDoc} */
    @Override protected String getPath() { return "/actions/pending"; }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<ActionSummary> actions =
            new ArrayList<ActionSummary>();
        for (int i=0; i<result.size(); i++) {
            actions.add(
                new ActionSummary(new GwtJson(result.get(i).isObject())));
        }
        execute(actions);
    }

    protected abstract void execute(Collection<ActionSummary> result);
}
