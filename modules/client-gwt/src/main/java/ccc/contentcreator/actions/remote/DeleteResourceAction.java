/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions.remote;

import ccc.contentcreator.actions.RemotingAction;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.SingleSelectionModel;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class DeleteResourceAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public DeleteResourceAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.delete(), RequestBuilder.POST);
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/resources/"+_selectionModel.tableSelection().getId()+"/delete";
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _selectionModel.delete(item);
    }


    /** {@inheritDoc} */
    @Override
    protected boolean beforeExecute() {
        return GLOBALS.confirm(
            "Are sure you want to delete the selected resource?");
    }
}
