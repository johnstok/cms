package ccc.contentcreator.actions;

import java.util.Map;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.MetadataDialog;

/**
 * Update resource's metadata.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateMetadataAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;
    private IGlobals _globals = new IGlobalsImpl();
    private QueriesServiceAsync _qs = _globals.queriesService();

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public UpdateMetadataAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _qs.metadata(
            item.getId(),
            new ErrorReportingCallback<Map<String, String>>(
                UI_CONSTANTS.updateMetadata()){
                public void onSuccess(final Map<String, String> data) {
                    new MetadataDialog(
                        item,
                        data.entrySet(),
                        _selectionModel)
                    .show();
                }
            }
        );
    }
}
