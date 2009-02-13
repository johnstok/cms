package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UploadFileDialog;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateFileAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel
     */
    public CreateFileAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.treeSelection();
        new UploadFileDialog(
            item.<String>get("id"),
            item.<String>get("name"),
            _selectionModel)
        .show();
    }
}
