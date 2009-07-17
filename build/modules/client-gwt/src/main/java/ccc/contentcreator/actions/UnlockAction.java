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
 * Unlock a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UnlockAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public UnlockAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.unlock(), RequestBuilder.POST);
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_selectionModel.tableSelection().getId()+"/unlock";
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        item.setLocked(null);
        _selectionModel.update(item);
    }
}
