package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.GwtJson;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.client.SelectionModelEventBus;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.controllers.EditTextFilePresenter;
import ccc.contentcreator.views.gxt.EditTextFileDialog;
import ccc.rest.dto.FileDto2;

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
        final FileDto2 dto = new FileDto2(new GwtJson(result));
        if ("text".equalsIgnoreCase(dto.getMimeType().getPrimaryType())) {
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
