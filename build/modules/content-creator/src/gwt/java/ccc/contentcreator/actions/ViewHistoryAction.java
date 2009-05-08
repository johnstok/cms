package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.HistoryDialog;
import ccc.services.api.LogEntrySummary;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class ViewHistoryAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ViewHistoryAction(final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _queries.history(
            item.getId().toString(),
            new ErrorReportingCallback<Collection<LogEntrySummary>>(){
                public void onSuccess(final Collection<LogEntrySummary> data) {
                    new HistoryDialog(data, _selectionModel).show();
                }
            }
        );
    }
}
