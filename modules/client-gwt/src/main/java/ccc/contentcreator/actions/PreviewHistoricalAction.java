package ccc.contentcreator.actions;

import ccc.contentcreator.binding.LogEntrySummaryModelData;
import ccc.contentcreator.client.Action;
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

        new GetAbsolutePathAction(UI_CONSTANTS.preview(),
                                  _historyDialog.getResourceId()) {
            @Override protected void execute(final String path) {
                final String url =
                    GLOBALS.appURL()
                    + "preview"
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
        }.execute();
    }
}
