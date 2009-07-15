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

import ccc.api.ID;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ReorderFolderAction
    extends
        RemotingAction {

    private final List<String> _folderOrder;
    private final ID _folderId;


    /**
     * Constructor.
     * @param folderOrder The order of the folders.
     * @param folderId The folder to update.
     */
    public ReorderFolderAction(final ID folderId,
                               final List<String> folderOrder) {
        super(GLOBALS.uiConstants().folderSortOrder(), RequestBuilder.POST);
        _folderOrder = folderOrder;
        _folderId = folderId;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders/"+_folderId+"/order";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final JSONArray a = new JSONArray();
        for (int i=0; i<_folderOrder.size(); i++) {
            a.set(i, new JSONString(_folderOrder.get(i)));
        }
        return a.toString();
    }
}
