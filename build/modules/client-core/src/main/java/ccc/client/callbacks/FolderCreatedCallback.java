package ccc.client.callbacks;

import ccc.api.core.Folder;
import ccc.api.types.CommandType;
import ccc.client.core.InternalServices;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.core.S11nHelper;
import ccc.client.events.Event;

/**
 * Callback handler for creating a folder.
 *
 * @author Civic Computing Ltd.
 */
public class FolderCreatedCallback extends ResponseHandlerAdapter {

    /**
     * Constructor.
     *
     * @param name The action name.
     */
    public FolderCreatedCallback(final String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    public void onOK(final ccc.client.core.Response response) {
        final Folder rs = new S11nHelper().readFolder(response);
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.FOLDER_CREATE);
        event.addProperty("resource", rs);
        InternalServices.REMOTING_BUS.fireEvent(event);
    }
}