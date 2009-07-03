package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.SingleSelectionModel;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class PublishAction
    implements
        Action {

    private final SingleSelectionModel _selectionModel;
    private IGlobals _globals = new IGlobalsImpl();
    private CommandServiceAsync _cs = _globals.commandService();

    /**
     * Constructor.
     *
     * @param selectionModel The selection model to use.
     */
    public PublishAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();
        _cs.publish(
            item.getId(),
            new ErrorReportingCallback<Void>(UI_CONSTANTS.publish()){
                public void onSuccess(final Void arg0) {
                    item.setPublished(
                        new IGlobalsImpl().currentUser().getUsername());
                    _selectionModel.update(item);
                }
            }
        );
    }
}
