package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model for this action.
     */
    public ClearWorkingCopyAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData page = _selectionModel.tableSelection();
        _commands.clearWorkingCopy(
            page.<String>get("id"),
            new ErrorReportingCallback<Void>(){
                public void onSuccess(final Void arg0) {
                    // FIXME item.set("hasWorkingCopy", false);
                    _selectionModel.update(page);
                }
            }
        );
    }
}
