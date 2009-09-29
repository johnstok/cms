package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SelectionModelEventBus;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.controllers.CreateTextFilePresenter;
import ccc.contentcreator.views.gxt.CreateTextFileDialog;

/**
 * Create a text file.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateTextFileAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public OpenCreateTextFileAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            GLOBALS.alert(GLOBALS.uiConstants().noFolderSelected());
        } else {
            new CreateTextFilePresenter(
                GLOBALS,
                new SelectionModelEventBus(_selectionModel),
                new CreateTextFileDialog(),
                item);
        }
    }
}
