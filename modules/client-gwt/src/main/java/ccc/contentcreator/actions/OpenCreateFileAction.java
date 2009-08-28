package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UploadFileDialog;

/**
 * Create a file.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateFileAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreateFileAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData parent = _selectionModel.treeSelection();
        if (parent == null) {
            GLOBALS.alert(GLOBALS.uiConstants().noFolderSelected());
        } else {
            new UploadFileDialog(parent, _selectionModel).show();
        }
    }
}
