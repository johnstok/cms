package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UpdateFolderSortOrderDialog;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateSortOrderAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selectionModel for this action.
     */
    public UpdateSortOrderAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData selectedModel = _selectionModel.tableSelection();
        new UpdateFolderSortOrderDialog(
            _selectionModel,
            selectedModel.<String>get("sortOrder"))
        .show();
    }
}
