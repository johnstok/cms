package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.services.api.ResourceSummary;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class PublishAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final SingleSelectionModel _selectionModel;

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
        _commands.publish(
            item.getId(),
            new ErrorReportingCallback<ResourceSummary>(){
                public void onSuccess(final ResourceSummary arg0) {
                    item.merge(arg0);
                    _selectionModel.update(item);
                }
            }
        );
    }
}
