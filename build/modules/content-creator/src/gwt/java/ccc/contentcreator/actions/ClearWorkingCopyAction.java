package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model for this action.
     */
    public ClearWorkingCopyAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData page = _selectionModel.tableSelection();
        _commands.clearWorkingCopy(
            page.getId(),
            new ErrorReportingCallback<Void>(){
                public void onSuccess(final Void arg0) {
                    _selectionModel.tableSelection().setWorkingCopy(false);
                    _selectionModel.update(page);
                }
            }
        );
    }
}
