package ccc.contentcreator.actions;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
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

    private final HistoryDialog _historyDialog;
    private IGlobals _globals = new IGlobalsImpl();
    private QueriesServiceAsync _qs = _globals.queriesService();

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
        _qs.getAbsolutePath(
            _historyDialog.getResourceId(),
            new ErrorReportingCallback<String>(UI_CONSTANTS.preview()) {
                public void onSuccess(final String path) {
                    final String url =
                        new IGlobalsImpl().appURL()
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
