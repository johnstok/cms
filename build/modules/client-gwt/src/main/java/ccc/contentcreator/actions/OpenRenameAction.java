package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SelectionModelEventBus;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.controllers.RenameResourcePresenter;
import ccc.contentcreator.views.gxt.RenameDialog;

/**
 * Rename a resource.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenRenameAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public OpenRenameAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        new RenameResourcePresenter(
            GLOBALS,
            new SelectionModelEventBus(_selectionModel),
            new RenameDialog(),
            item);
    }
}
