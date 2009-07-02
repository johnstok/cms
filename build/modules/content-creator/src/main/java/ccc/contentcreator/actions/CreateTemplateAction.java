package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.EditTemplateDialog;

/**
 * Create a template.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateTemplateAction
    implements
        Action {

    private IGlobals _globals = new IGlobalsImpl();
    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public CreateTemplateAction(
          final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            _globals.alert(_globals.uiConstants().noFolderSelected());
        } else {
            new EditTemplateDialog(
                item.getId(),
                _selectionModel)
            .show();
        }
    }
}
