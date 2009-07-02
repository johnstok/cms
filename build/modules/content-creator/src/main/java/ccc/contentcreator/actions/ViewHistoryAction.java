package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.api.LogEntrySummary;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.HistoryDialog;

/**
 * View resource's history.
 *
 * @author Civic Computing Ltd.
 */
public final class ViewHistoryAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ViewHistoryAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        QUERIES_SERVICE.history(
            item.getId(),
            new ErrorReportingCallback<Collection<LogEntrySummary>>(
                UI_CONSTANTS.viewHistory()){
                public void onSuccess(final Collection<LogEntrySummary> data) {
                    new HistoryDialog(data, item.getId(), _selectionModel).show();
                }
            }
        );
    }
}
