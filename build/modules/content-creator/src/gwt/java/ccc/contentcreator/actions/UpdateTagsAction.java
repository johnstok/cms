package ccc.contentcreator.actions;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UpdateTagsDialog;
import ccc.services.api.ResourceDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateTagsAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();
    private final UIConstants _constants = Globals.uiConstants();

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public UpdateTagsAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _queries.resourceDelta(
            item.getId().toString(),
            new ErrorReportingCallback<ResourceDelta>(){
                public void onSuccess(final ResourceDelta delta) {
                    if (delta == null) {
                        Globals.alert(_constants.noTemplateFound()); // TODO: Can we really get to this path?!
                    } else {
                        new UpdateTagsDialog(delta).show();
                    }
                }
            }
        );
    }
}
