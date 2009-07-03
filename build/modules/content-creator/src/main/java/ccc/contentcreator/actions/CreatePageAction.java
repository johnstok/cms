package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.api.TemplateSummary;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreatePageDialog;

/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public final class CreatePageAction
    implements
        Action {

    private IGlobals _globals = new IGlobalsImpl();
    private QueriesServiceAsync _qs = _globals.queriesService();
    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public CreatePageAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.treeSelection();
        if (item == null) {
            _globals.alert(UI_CONSTANTS.noFolderSelected());
            return;
        }
        _qs.templates(
            new ErrorReportingCallback<Collection<TemplateSummary>>(
                                                    UI_CONSTANTS.createPage()){
                public void onSuccess(
                                      final Collection<TemplateSummary> list) {
                    new CreatePageDialog(list, item, _selectionModel).show();
                }
            }
        );
    }
}
