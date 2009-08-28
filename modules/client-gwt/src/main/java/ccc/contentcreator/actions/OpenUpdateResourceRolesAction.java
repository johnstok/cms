package ccc.contentcreator.actions;

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UpdateResourceRolesDialog;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;

/**
 * Action to launch the 'update resource roles' dialog.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenUpdateResourceRolesAction
    extends
        RemotingAction {


    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    public OpenUpdateResourceRolesAction(final SingleSelectionModel ssm) {
        super(UI_CONSTANTS.updateRoles());
        _selectionModel = ssm;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/resources/"
            + _selectionModel.tableSelection().getId()
            + "/roles";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final List<String> data = new ArrayList<String>();
        final JSONArray rawData =
            JSONParser.parse(response.getText()).isArray();
        for (int i=0; i<rawData.size(); i++) {
            data.add(rawData.get(i).isString().stringValue());
        }

        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        new UpdateResourceRolesDialog(
            item.getId(),
            data)
        .show();
    }
}
