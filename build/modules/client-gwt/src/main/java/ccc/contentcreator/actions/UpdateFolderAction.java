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

import java.util.List;

import ccc.contentcreator.client.GwtJson;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for folder updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderAction
    extends
        RemotingAction {

    private final ID _id;
    private final String _sortOrder;
    private final ID _indexPageId;
    private final List<String> _sortList;


    /**
     * Constructor.
     *
     * @param indexPageId The id of the fodler's index page.
     * @param sortOrder The sort order for the folder.
     * @param id The folder's id.
     */
    public UpdateFolderAction(final ID id,
                               final String sortOrder,
                               final ID indexPageId,
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
