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

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.SingleSelectionModel;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Lock a resource.
 *
 * @author Civic Computing Ltd.
 */
public class LockAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public LockAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.lock(), RequestBuilder.POST);
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_selectionModel.tableSelection().getId()+"/lock";
    }

    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        item.setLocked(
            GLOBALS.currentUser().getUsername());
        _selectionModel.update(item);
    }
}
