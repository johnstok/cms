package ccc.client.callbacks;

import ccc.api.core.Comment;
import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.client.core.InternalServices;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;

/**
 * Callback handler for updating a comment.
 *
 * @author Civic Computing Ltd.
 */
public class CommentUpdatedCallback extends ResponseHandlerAdapter {

    private final Comment _comment;

    /**
     * Constructor.
     *
     * @param name The action name.
     * @param comment The updated comment.
     */
    public CommentUpdatedCallback(final String name,
                                  final Comment comment) {
        super(name);
        _comment = DBC.require().notNull(comment);
    }

    /** {@inheritDoc} */
    @Override
    public void onOK(final ccc.client.core.Response response) {
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.COMMENT_UPDATE);
        event.addProperty("comment", _comment);
        InternalServices.REMOTING_BUS.fireEvent(event);
    }
}