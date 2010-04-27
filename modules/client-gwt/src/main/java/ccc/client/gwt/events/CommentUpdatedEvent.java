/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.events;

import ccc.api.core.Comment;
import ccc.api.types.DBC;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating a comment was updated.
 *
 * @author Civic Computing Ltd.
 */
public class CommentUpdatedEvent
    extends
        GwtEvent<CommentUpdatedEvent.CommentUpdatedHandler> {

    private final Comment _comment;


    /**
     * Constructor.
     *
     * @param resource The updated comment.
     */
    public CommentUpdatedEvent(final Comment resource) {
        _comment = DBC.require().notNull(resource);
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public Comment getComment() { return _comment; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(
                        final CommentUpdatedHandler handler) {
        handler.onUpdate(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<CommentUpdatedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'comment updated' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface CommentUpdatedHandler extends EventHandler {


        /**
         * Handle a 'comment updated' event.
         *
         * @param event The event to handle.
         */
        void onUpdate(CommentUpdatedEvent event);
    }


    /** TYPE : Type. */
    public static final Type<CommentUpdatedHandler> TYPE =
        new Type<CommentUpdatedHandler>();
}
