package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.EditTemplateDialog;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateTemplateAction
    implements
        Action {

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public CreateTemplateAction(
          final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            Globals.alert(Globals.uiConstants().noFolderSelected());
        } else {
            new EditTemplateDialog(
                item.getId().toString(),
                _selectionModel)
            .show();
        }
    }
}
