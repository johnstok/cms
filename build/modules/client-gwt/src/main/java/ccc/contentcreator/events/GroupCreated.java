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

import ccc.rest.dto.GroupDto;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GroupCreated
    extends
        GwtEvent<GroupCreated.GroupCreatedHandler> {

    private final GroupDto _group;


    /**
     * Constructor.
     *
     * @param group The newly created group.
     */
    public GroupCreated(final GroupDto group) { _group = group; }


    /**
     * Accessor.
     *
     * @return Returns the new group.
     */
    public GroupDto getGroup() { return _group; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final GroupCreated.GroupCreatedHandler handler) {
        handler.onCreate(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<GroupCreatedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'group created' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface GroupCreatedHandler extends EventHandler {


        /**
         * Handle a 'group created' event.
         *
         * @param event The event to handle.
         */
        void onCreate(GroupCreated event);
    }


    /** TYPE : Type. */
    public static final Type<GroupCreatedHandler> TYPE =
        new Type<GroupCreatedHandler>();
}
