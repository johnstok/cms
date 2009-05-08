package ccc.contentcreator.actions;

import java.util.Map;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.MetadataDialog;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateMetadataAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public UpdateMetadataAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _queries.metadata(
            item.<String>get("id"),
            new ErrorReportingCallback<Map<String, String>>(){
                public void onSuccess(final Map<String, String> data) {
                    new MetadataDialog(
                        item.<String>get("id"),
                        data.entrySet())
                    .show();
                }
            }
        );
    }
}
