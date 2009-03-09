package ccc.contentcreator.actions;

import java.util.Date;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Publish a resource.
 *
 * @author Civic Computing Ltd.
 */
public class PublishOnAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final SingleSelectionModel _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel
     */
    public PublishOnAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.tableSelection();
        _commands.publish(
            item.<String>get("id"),
            new Date(new Date().getTime()+60000),
            new ErrorReportingCallback<Void>(){
                public void onSuccess(final Void arg0) {
                    // Nothing to do.
                }
            }
        );
    }
}
