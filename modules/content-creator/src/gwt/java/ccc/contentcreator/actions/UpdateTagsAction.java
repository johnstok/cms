package ccc.contentcreator.actions;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.UpdateTagsDialog;
import ccc.services.api.ResourceDelta;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UpdateTagsAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();
    private final UIConstants _constants = Globals.uiConstants();

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel
     */
    public UpdateTagsAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.tableSelection();
        _queries.resourceDelta(
            item.<String>get("id"),
            new ErrorReportingCallback<ResourceDelta>(){
                public void onSuccess(final ResourceDelta delta) {
                    if (delta == null) {
                        Globals.alert(_constants.noTemplateFound()); // TODO: Can we really get to this path?!
                    } else {
                        new UpdateTagsDialog(delta).show();
                    }
                }
            }
        );
    }
}
