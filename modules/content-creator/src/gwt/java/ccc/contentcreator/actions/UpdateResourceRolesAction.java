package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UpdateResourceRolesDialog;

/**
 * Action to launch the 'update resource roles' dialog.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateResourceRolesAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    public UpdateResourceRolesAction(final SingleSelectionModel ssm) {
        _selectionModel = ssm;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _queries.roles(
            item.getId(),
            new ErrorReportingCallback<Collection<String>>(){
                public void onSuccess(final Collection<String> data) {
                    new UpdateResourceRolesDialog(
                        item.getId(),
                        data)
                    .show();
                }
            }
        );
    }
}
