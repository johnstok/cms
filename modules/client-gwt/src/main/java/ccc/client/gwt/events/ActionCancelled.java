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
package ccc.client.gwt.events;

import ccc.client.gwt.binding.ActionSummaryModelData;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating a working copy was applied to a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ActionCancelled
    extends
        GwtEvent<ActionCancelled.ActionCancelledHandler> {

    private final ActionSummaryModelData _action;


    /**
     * Constructor.
     *
     * @param action The resource whose WC has been applied.
     */
    public ActionCancelled(final ActionSummaryModelData action) {
        _action = action;
    }


    /**
     * Accessor.
     *
     * @return Returns the new group.
     */
    public ActionSummaryModelData getAction() { return _action; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(
                        final ActionCancelled.ActionCancelledHandler handler) {
        handler.onCancel(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<ActionCancelledHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'group created' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface ActionCancelledHandler extends EventHandler {


        /**
         * Handle a 'WC applied' event.
         *
         * @param event The event to handle.
         */
        void onCancel(ActionCancelled event);
    }


    /** TYPE : Type. */
    public static final Type<ActionCancelledHandler> TYPE =
        new Type<ActionCancelledHandler>();
}
