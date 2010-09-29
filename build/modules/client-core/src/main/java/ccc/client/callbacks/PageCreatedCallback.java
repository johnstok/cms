package ccc.client.callbacks;

import ccc.api.core.Page;
import ccc.api.types.CommandType;
import ccc.client.core.InternalServices;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.core.S11nHelper;
import ccc.client.events.Event;

/**
 * Callback handler for creating a page.
 *
 * @author Civic Computing Ltd.
 */
public class PageCreatedCallback extends ResponseHandlerAdapter {

    /**
     * Constructor.
     *
     * @param name The action name.
     */
    public PageCreatedCallback(final String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    public void onOK(final ccc.client.core.Response response) {
        final Page rs = new S11nHelper().readPage(response);
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.PAGE_CREATE);
        event.addProperty("resource", rs);
        InternalServices.REMOTING_BUS.fireEvent(event);
    }
}