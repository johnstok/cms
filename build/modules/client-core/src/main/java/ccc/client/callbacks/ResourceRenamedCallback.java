package ccc.client.callbacks;

import java.util.UUID;

import ccc.api.types.CommandType;
import ccc.api.types.ResourcePath;
import ccc.client.core.InternalServices;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;

/**
 * Callback handler for renaming a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceRenamedCallback extends ResponseHandlerAdapter {

    private final Event<CommandType> _event;

    /**
     * Constructor.
     *
     * @param name The action name.
     * @param newPath The resource's new path.
     * @param id The resource's ID.
     * @param rName The resource's new name.
     */
    public ResourceRenamedCallback(final String name,
                                   final String rName,
                                   final UUID id,
                                   final ResourcePath newPath) {
        super(name);
        _event = new Event<CommandType>(CommandType.RESOURCE_RENAME);
        _event.addProperty("name", rName);
        _event.addProperty("path", newPath);
        _event.addProperty("id", id);
    }

    /** {@inheritDoc} */
    @Override
    public void onNoContent(final ccc.client.core.Response response) {
        InternalServices.REMOTING_BUS.fireEvent(_event);
    }
}