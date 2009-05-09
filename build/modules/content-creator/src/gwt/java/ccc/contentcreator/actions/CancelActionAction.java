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

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.binding.ActionSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.ActionTable;
import ccc.contentcreator.client.Globals;
import ccc.services.api.ActionStatus;


/**
 * Cancels a CCC action.
 *
 * @author Civic Computing Ltd.
 */
public class CancelActionAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();
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
            Globals.alert("Please select an action."); // FIXME: I18n.
            return;
        } else if (ActionStatus.Scheduled!=action.getStatus()) {
            Globals.alert("This action has already been completed."); // FIXME: I18n.
            return;
        } else {
            _commands.cancelAction(
                action.getId(),
                new ErrorReportingCallback<Void>(){
                    public void onSuccess(final Void arg0) {
                        action.setStatus(ActionStatus.Cancelled);
                        _table.update(action);
                    }
                }
            );
        }
    }
}
