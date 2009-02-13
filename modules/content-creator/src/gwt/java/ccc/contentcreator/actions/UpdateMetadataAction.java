package ccc.contentcreator.actions;

import java.util.Map;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.MetadataDialog;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateMetadataAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel
     */
    public UpdateMetadataAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.tableSelection();
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
