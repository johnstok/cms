package ccc.contentcreator.actions;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

import com.google.gwt.user.client.Window;

/**
 * Open a dialog to preview the selected resource.
 *
 * @author Civic Computing Ltd.
 */
public final class PreviewAction
    implements
        Action {

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
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        final String url =
            Globals.appURL()
                + item.getAbsolutePath()
                + ((_useWorkingCopy) ? "?wc" : "");

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
