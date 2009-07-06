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

import ccc.api.ActionStatus;
import ccc.contentcreator.binding.ActionSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.ActionTable;


/**
 * Cancels a CCC action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionAction
    implements
        Action {

    private final ActionTable _table;
    /**
     * Constructor.
     *
     * @param table The action table to work with.
     */
    public CancelActionAction(final ActionTable table) {
        _table = table;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ActionSummaryModelData action = _table.getSelectedItem();
        if (null==action) {
            GLOBALS.alert(UI_CONSTANTS.pleaseChooseAnAction());
            return;
        } else if (ActionStatus.Scheduled!=action.getStatus()) {
            GLOBALS.alert(UI_CONSTANTS.thisActionHasAlreadyCompleted());
            return;
        } else {
            _cs.cancelAction(
                action.getId(),
                new ErrorReportingCallback<Void>(UI_CONSTANTS.cancel()){
                    public void onSuccess(final Void arg0) {
                        action.setStatus(ActionStatus.Cancelled);
                        _table.update(action);
                    }
                }
            );
        }
    }
}