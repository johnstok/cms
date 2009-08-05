package ccc.contentcreator.actions;

import ccc.contentcreator.dialogs.HistoryDialog;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class CreateWorkingCopyFromHistoricalVersionAction
    extends
        RemotingAction {

    private final HistoryDialog _dialog;


    /**
     * Constructor.
     *
     * @param dialog The selection model for this action.
     */
    public CreateWorkingCopyFromHistoricalVersionAction(
                                                  final HistoryDialog dialog) {
        super(UI_CONSTANTS.revert(), RequestBuilder.POST);
        _dialog = dialog;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/resources/"
            + _dialog.getResourceId()
            + "/wc-create?v="
            + _dialog.selectedItem().getIndex();
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        _dialog.workingCopyCreated();
        _dialog.hide();
    }
}
