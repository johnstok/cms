package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.RenameDialog;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class RenameAction
    implements
        Action {

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public RenameAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        new RenameDialog(item, _selectionModel).show();
    }
}
