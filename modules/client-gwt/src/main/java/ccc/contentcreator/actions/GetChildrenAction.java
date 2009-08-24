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

import ccc.api.ID;
import ccc.api.ResourceSummary;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * Remote action for children resource fetching.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetChildrenAction
    extends
        RemotingAction {

    private final ID _parentId;

    /**
     * Constructor.
     *
     * @param actionName The name of the action.
     * @param parentId The id of the parent folder.
     */
    public GetChildrenAction(final String actionName, final ID parentId) {
        super(actionName);
        _parentId = parentId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders/"+_parentId+"/children";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<ResourceSummary> children =
            new ArrayList<ResourceSummary>();
        for (int i=0; i<result.size(); i++) {
            children.add(
                new ResourceSummary(new GwtJson(result.get(i).isObject())));
        }

        execute(children);
    }

    protected abstract void execute(Collection<ResourceSummary> children);
}
