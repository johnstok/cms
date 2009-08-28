package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreateFolderDialog;

/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateFolderAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreateFolderAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            GLOBALS.alert(GLOBALS.uiConstants().noFolderSelected());
        } else {
            new CreateFolderDialog(item, _selectionModel).show();
        }
    }
}
