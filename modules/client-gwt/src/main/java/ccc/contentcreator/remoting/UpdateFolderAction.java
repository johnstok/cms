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
package ccc.contentcreator.remoting;

import java.util.List;
import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for folder updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderAction
    extends
        RemotingAction {

    private final UUID _id;
    private final String _sortOrder;
    private final UUID _indexPageId;
    private final List<String> _sortList;


    /**
     * Constructor.
     *
     * @param indexPageId The id of the fodler's index page.
     * @param sortOrder The sort order for the folder.
     * @param id The folder's id.
     * @param sortList The manual order of the resources in the folder.
     */
    public UpdateFolderAction(final UUID id,
                               final String sortOrder,
                               final UUID indexPageId,
                               final List<String> sortList) {
        super(UI_CONSTANTS.folderSortOrder(), RequestBuilder.POST);
        _id = id;
        _sortOrder = sortOrder;
        _indexPageId = indexPageId;
        _sortList = sortList;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders/"+_id;
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = new GwtJson();
        json.set(JsonKeys.SORT_ORDER, _sortOrder);
        json.set(JsonKeys.INDEX_PAGE_ID, _indexPageId);
        json.setStrings(JsonKeys.SORT_LIST, _sortList);
        return json.toString();
    }
}
