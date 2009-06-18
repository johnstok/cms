package ccc.contentcreator.actions;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dialogs.HistoryDialog;

import com.google.gwt.user.client.Window;

/**
 * Open a dialog to preview the selected resource.
 *
 * @author Civic Computing Ltd.
 */
public final class PreviewHistoricalAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();

    private final HistoryDialog _historyDialog;

    /**
     * Constructor.
     *
     * @param dialog The history dialog.
     */
    public PreviewHistoricalAction(final HistoryDialog dialog) {
        _historyDialog = dialog;
    }

    /** {@inheritDoc} */
    public void execute() {
        final LogEntrySummaryModelData item = _historyDialog.selectedItem();
        if (null==item) {
            return;
        }
        _queries.getAbsolutePath(
            item.getId(),
            new ErrorReportingCallback<String>(UI_CONSTANTS.preview()) {
                public void onSuccess(final String path) {
                    final String url =
                        Globals.appURL()
                        + path
                        + "?v="
                        + item.getIndex();
                    Window.open(
                        url,
                        "_blank",
                        "menubar=no,"
                        + "location=yes,"
                        + "toolbar=no,"
                        + "resizable=yes,"
                        + "scrollbars=yes,"
                        + "status=no");
                }
            }
        );
    }
}
