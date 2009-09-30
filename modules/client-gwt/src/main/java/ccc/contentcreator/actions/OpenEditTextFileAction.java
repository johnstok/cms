package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.client.SelectionModelEventBus;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.controllers.EditTextFilePresenter;
import ccc.contentcreator.views.gxt.EditTextFileDialog;
import ccc.rest.dto.TextFileDelta;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Action for text file edit dialog opening.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenEditTextFileAction
extends
    RemotingAction {


    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public OpenEditTextFileAction(final ResourceTable selectionModel) {
        super(GLOBALS.uiConstants().updateTextFile());
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        return "/files/" + item.getId();
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONObject result =
            JSONParser.parse(response.getText()).isObject();
        final TextFileDelta dto = new TextFileDelta(new GwtJson(result));
        if (dto.getContent() != null) {
            new EditTextFilePresenter(
                GLOBALS,
                new SelectionModelEventBus(_selectionModel),
                new EditTextFileDialog(),
                dto);
        } else {
            GLOBALS.alert(
                GLOBALS.uiConstants().noEditorForResource());
        }
    }

}
