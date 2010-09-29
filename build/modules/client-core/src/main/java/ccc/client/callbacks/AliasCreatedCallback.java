package ccc.client.callbacks;

import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.client.core.InternalServices;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.core.S11nHelper;
import ccc.client.events.Event;

/**
 * Callback handler for creating an alias.
 *
 * @author Civic Computing Ltd.
 */
public class AliasCreatedCallback extends ResponseHandlerAdapter {

    /**
     * Constructor.
     *
     * @param name The action name.
     */
    public AliasCreatedCallback(final String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    public void onOK(final Response response) {
        final ResourceSummary newAlias =
            new S11nHelper().readResourceSummary(response);
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.ALIAS_CREATE);
        event.addProperty("resource", newAlias);
        InternalServices.REMOTING_BUS.fireEvent(event);
    }
}