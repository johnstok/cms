package ccc.contentcreator.actions;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.PreviewContentDialog;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Open a dialog to preview the selected resource.
 *
 * @author Civic Computing Ltd.
 */
public final class PreviewAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();

    private final SingleSelectionModel _selectionModel;
    private final boolean _useWorkingCopy;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     * @param useWorkingCopy Boolean for working copy preview.
     */
    public PreviewAction(final SingleSelectionModel selectionModel,
                         final boolean useWorkingCopy) {
        _selectionModel = selectionModel;
        _useWorkingCopy = useWorkingCopy;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.tableSelection();
        _queries.getAbsolutePath(
            item.<String>get("id"),
            new ErrorReportingCallback<String>() {
                public void onSuccess(final String path) {
                    new PreviewContentDialog(path, _useWorkingCopy).show();
                }
            }
        );
    }
}
