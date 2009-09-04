package ccc.contentcreator.actions;

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.HistoryDialog;
import ccc.rest.dto.RevisionDto;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;

/**
 * View resource's history.
 *
 * @author Civic Computing Ltd.
 */
public final class ViewHistoryAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ViewHistoryAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.viewHistory());
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    @Override protected String getPath() {
        return _selectionModel.tableSelection().revisionsPath();

    }

    /** {@inheritDoc} */
    @Override protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<RevisionDto> history =
            new ArrayList<RevisionDto>();
        for (int i=0; i<result.size(); i++) {
            history.add(
                new RevisionDto(new GwtJson(result.get(i).isObject())));
        }

        new HistoryDialog(
            history, _selectionModel.tableSelection().getId(), _selectionModel)
        .show();
    }
}
