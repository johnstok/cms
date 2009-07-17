package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.api.TemplateSummary;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreatePageDialog;

/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public final class CreatePageAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public CreatePageAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            GLOBALS.alert(UI_CONSTANTS.noFolderSelected());
            return;
        }
        new GetTemplatesAction(UI_CONSTANTS.createPage()){
            @Override protected void execute(final Collection<TemplateSummary> templates) {
                new CreatePageDialog(templates, item, _selectionModel).show();
            }

        }.execute();
    }
}
