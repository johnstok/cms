package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.dialogs.HistoryDialog;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class CreateWorkingCopyFromHistoricalVersionAction
    implements
        Action {

    private final HistoryDialog _dialog;
    private IGlobals _globals = new IGlobalsImpl();
    private CommandServiceAsync _cs = _globals.commandService();

    /**
     * Constructor.
     *
     * @param dialog The selection model for this action.
     */
    public CreateWorkingCopyFromHistoricalVersionAction(
                                                  final HistoryDialog dialog) {
        _dialog = dialog;
    }

    /** {@inheritDoc} */
    public void execute() {
        final LogEntrySummaryModelData selected = _dialog.selectedItem();

        _cs.createWorkingCopy(
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
