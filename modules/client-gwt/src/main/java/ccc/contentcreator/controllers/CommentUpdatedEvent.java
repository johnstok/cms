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
package ccc.contentcreator.controllers;

import ccc.contentcreator.binding.CommentModelData;
import ccc.contentcreator.client.Event;


/**
 * An event indicating a comment was updated.
 *
 * @author Civic Computing Ltd.
 */
public class CommentUpdatedEvent implements Event {

    private final CommentModelData _comment;


    /**
     * Constructor.
     *
     * @param resource The updated resource.
     */
    public CommentUpdatedEvent(final CommentModelData resource) {
        _comment = resource;
    }


    /** {@inheritDoc} */
    @Override
    public Type getType() {
        return Event.Type.COMMENT_UPDATED;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public CommentModelData getComment() {
        return _comment;
    }
}
