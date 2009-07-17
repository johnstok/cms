package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.EditFolderDialog;

/**
 * Edit a folder.
 *
 * @author Civic Computing Ltd.
 */
public class EditFolderAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selectionModel for this action.
     */
    public EditFolderAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData selectedModel =
            _selectionModel.tableSelection();
        new EditFolderDialog(
            _selectionModel,
            selectedModel.getSortOrder(),
            selectedModel.getIndexPageId())
        .show();
    }
}
