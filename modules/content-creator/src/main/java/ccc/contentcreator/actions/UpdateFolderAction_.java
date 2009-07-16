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

import ccc.api.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderAction_
    extends
        RemotingAction {

    private final ID _id;
    private final String _sortOrder;
    private final ID _indexPageId;


    /**
     * Constructor.
     *
     * @param indexPageId The id of the fodler's index page.
     * @param sortOrder The sort order for the folder.
     * @param id The folder's id.
     */
    public UpdateFolderAction_(final ID id,
                               final String sortOrder,
                               final ID indexPageId) {
        super(UI_CONSTANTS.folderSortOrder(), RequestBuilder.POST);
        _id = id;
        _sortOrder = sortOrder;
        _indexPageId = indexPageId;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() { // FIXME: Escape!!
        return "/folders/"+_id+"?s="+_sortOrder+"&i="+_indexPageId;
    }
}
