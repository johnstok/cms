package ccc.contentcreator.actions;

import java.util.HashMap;
import java.util.Map;

import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.MetadataDialog;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

/**
 * Update resource's metadata.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateMetadataAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public UpdateMetadataAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.updateMetadata());
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/resources/"
            + _selectionModel.tableSelection().getId()
            + "/metadata";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final Map<String, String> metadata = new HashMap<String, String>();
        for (final String key : result.keySet()) {
            metadata.put(key, result.get(key).isString().stringValue());
        }

        new MetadataDialog(
            _selectionModel.tableSelection(),
            metadata.entrySet(),
            _selectionModel)
        .show();
    }
}
