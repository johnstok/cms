package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dialogs.HistoryDialog;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class CreateWorkingCopyFromHistoricalVersionAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final HistoryDialog _dialog;

    /**
     * Constructor.
     *
     * @param dialog The selection model for this action.
     */
    public CreateWorkingCopyFromHistoricalVersionAction(final HistoryDialog dialog) {
        _dialog = dialog;
    }

    /** {@inheritDoc} */
    public void execute() {
        final LogEntrySummaryModelData selected = _dialog.selectedItem();

        _commands.createWorkingCopy(
            _dialog.getResourceId(),
            selected.getIndex(),
            new ErrorReportingCallback<Void>(UI_CONSTANTS.revert()){
                public void onSuccess(final Void arg0) {
                    _dialog.workingCopyCreated();
                    _dialog.close();
                }
            }
        );
    }
}
