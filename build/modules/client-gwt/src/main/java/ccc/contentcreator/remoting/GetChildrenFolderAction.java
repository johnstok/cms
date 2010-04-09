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
 * Revision      $Rev: 2246 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2009-12-07 17:46:54 +0000 (Mon, 07 Dec 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.remoting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import ccc.api.dto.ResourceSummary;
import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * Remote action for children of folder type resource fetching.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetChildrenFolderAction
    extends
        RemotingAction {

    private final UUID _parentId;

    /**
     * Constructor.
     *
     * @param actionName The name of the action.
     * @param parentId The id of the parent folder.
     */
    public GetChildrenFolderAction(final String actionName,
                                   final UUID parentId) {
        super(actionName);
        _parentId = parentId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders/"+_parentId+"/folder-children";
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

    /**
     * Handle the result of a successful call.
     *
     * @param children The collection of folder children.
     */
    protected abstract void execute(Collection<ResourceSummary> children);
}
