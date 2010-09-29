package ccc.client.callbacks;

import ccc.api.core.ActionSummary;
import ccc.api.types.CommandType;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;

/**
 * Callback handler for applying a working copy.
 *
 * @author Civic Computing Ltd.
 */
public class ActionCancelledCallback extends ResponseHandlerAdapter {

    private final Event<CommandType> _event;

    /**
     * Constructor.
     *
     * @param action The resource whose WC has been applied.
     */
    public ActionCancelledCallback(final ActionSummary action) {
        super(I18n.UI_CONSTANTS.cancel());
        _event = new Event<CommandType>(CommandType.ACTION_CANCEL);
        _event.addProperty("action", action);
    }

    /** {@inheritDoc} */
    @Override
    public void onNoContent(final ccc.client.core.Response response) {
        InternalServices.REMOTING_BUS.fireEvent(_event);
    }
}