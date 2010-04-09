/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.contentcreator.events;

import ccc.api.dto.GroupDto;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * Event signalling a user group was updated.
 *
 * @author Civic Computing Ltd.
 */
public class GroupUpdated
    extends
        GwtEvent<GroupUpdated.GroupUpdatedHandler> {

    private final GroupDto _group;


    /**
     * Constructor.
     *
     * @param group The updated group.
     */
    public GroupUpdated(final GroupDto group) { _group = group; }


    /**
     * Accessor.
     *
     * @return Returns the new group.
     */
    public GroupDto getGroup() { return _group; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final GroupUpdated.GroupUpdatedHandler handler) {
        handler.onUpdate(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<GroupUpdatedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'group updated' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface GroupUpdatedHandler extends EventHandler {


        /**
         * Handle a 'group updated' event.
         *
         * @param event The event to handle.
         */
        void onUpdate(GroupUpdated event);
    }


    /** TYPE : Type. */
    public static final Type<GroupUpdatedHandler> TYPE =
        new Type<GroupUpdatedHandler>();
}
