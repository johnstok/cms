package ccc.client.callbacks;

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.client.core.InternalServices;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;

/**
 * Callback handler for applying a working copy.
 *
 * @author Civic Computing Ltd.
 */
class WCAppliedCallback extends ResponseHandlerAdapter {

    private final Event<CommandType> _event;

    /**
     * Constructor.
     *
     * @param name The action name.
     * @param resource The resource whose WC has been applied.
     */
    WCAppliedCallback(final String name,
                      final ResourceSummary resource) {
        super(name);
        _event = new Event<CommandType>(CommandType.RESOURCE_APPLY_WC);
        _event.addProperty("resource", resource);
    }

    /** {@inheritDoc} */
    @Override
    public void onNoContent(final ccc.client.core.Response response) {
        InternalServices.REMOTING_BUS.fireEvent(_event);
    }
}