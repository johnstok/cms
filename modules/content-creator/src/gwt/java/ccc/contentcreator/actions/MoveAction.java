package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.MoveDialog;
import ccc.services.api.ResourceSummary;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class MoveAction
    implements
        Action {

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;
    private final ResourceSummary _root;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     * @param root The root of current resource tree.
     */
    public MoveAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel,
                      final ResourceSummary root) {
        _selectionModel = selectionModel;
        _root = root;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        new MoveDialog(item, _selectionModel, _root).show();
    }
}
