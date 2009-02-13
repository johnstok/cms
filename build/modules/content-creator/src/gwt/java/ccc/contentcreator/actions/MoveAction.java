package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.MoveDialog;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class MoveAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;
    private final ResourceSummary _root;

    /**
     * Constructor.
     *
     * @param selectionModel
     * @param root
     */
    public MoveAction(final SingleSelectionModel selectionModel,
                      final ResourceSummary root) {
        _selectionModel = selectionModel;
        _root = root;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.tableSelection();
        new MoveDialog(item, _selectionModel, _root).show();
    }
}
