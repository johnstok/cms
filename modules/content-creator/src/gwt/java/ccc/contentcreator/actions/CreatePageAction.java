package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreatePageDialog;
import ccc.services.api.TemplateDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class CreatePageAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public CreatePageAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            Globals.alert(Globals.uiConstants().noFolderSelected());
            return;
        }
        _queries.templates(
            new ErrorReportingCallback<Collection<TemplateDelta>>(){
                public void onSuccess(
                                      final Collection<TemplateDelta> list) {
                    new CreatePageDialog(list, item, _selectionModel).show();
                }
            }
        );
    }
}
