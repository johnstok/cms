package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.SingleSelectionModel;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ClearWorkingCopyAction
    extends
        RemotingAction {

    private final SingleSelectionModel _selectionModel;


    /**
     * Constructor.
     *
     * @param selectionModel The selection model for this action.
     */
    public ClearWorkingCopyAction(final SingleSelectionModel selectionModel) {
        super(UI_CONSTANTS.deleteWorkingCopy(), RequestBuilder.POST);
        _selectionModel = selectionModel;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/resources/"+_selectionModel.tableSelection().getId()+"/wc-clear";
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummaryModelData page = _selectionModel.tableSelection();
        _selectionModel.tableSelection().setWorkingCopy(false);
        _selectionModel.update(page);
    }
}
