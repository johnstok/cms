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

import ccc.contentcreator.binding.ActionSummaryModelData;
import ccc.contentcreator.client.ActionTable;
import ccc.types.ActionStatus;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Cancels a CCC action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionAction
    extends
        RemotingAction {

    private final ActionTable _table;


    /**
     * Constructor.
     *
     * @param table The action table to work with.
     */
    public CancelActionAction(final ActionTable table) {
        super(UI_CONSTANTS.cancel(), RequestBuilder.POST);
        _table = table;
    }


    /** {@inheritDoc} */
    @Override public void execute() {
        final ActionSummaryModelData action = _table.getSelectedItem();
        if (null==action) {
            GLOBALS.alert(UI_CONSTANTS.pleaseChooseAnAction());
            return;
        } else if (ActionStatus.SCHEDULED!=action.getStatus()) {
            GLOBALS.alert(UI_CONSTANTS.thisActionHasAlreadyCompleted());
            return;
        } else {
            super.execute();
        }
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/actions/"+_table.getSelectedItem().getId()+"/cancel";
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        final ActionSummaryModelData action = _table.getSelectedItem();
        action.setStatus(ActionStatus.CANCELLED);
        _table.update(action);
    }
}
