package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateSortOrderAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final SingleSelectionModel _selectionModel;
    private final ComboBox<ModelData> _sortCombo;

    /**
     * Constructor.
     *
     * @param selectionModel
     * @param sortCombo
     */
    public UpdateSortOrderAction(final SingleSelectionModel selectionModel,
                                 final ComboBox<ModelData> sortCombo) {
        _selectionModel = selectionModel;
        _sortCombo = sortCombo;
    }

    /** {@inheritDoc} */
    public void execute() {
        _commands.updateFolderSortOrder(
            _selectionModel.getSelectedFolder().<String>get("id"),
            _sortCombo.getValue().<String>get("value"),
            new ErrorReportingCallback<Void>(){
                public void onSuccess(final Void result) {
                    _selectionModel.refresh();
                }});
    }
}
