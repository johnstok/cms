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

import ccc.contentcreator.binding.ResourceSummaryModelData;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating a working copy was applied to a resource.
 *
 * @author Civic Computing Ltd.
 */
public class WorkingCopyCleared
    extends
        GwtEvent<WorkingCopyCleared.WCClearedHandler> {

    private final ResourceSummaryModelData _resource;


    /**
     * Constructor.
     *
     * @param resource The resource whose WC has been applied.
     */
    public WorkingCopyCleared(final ResourceSummaryModelData resource) {
        _resource = resource;
    }


    /**
     * Accessor.
     *
     * @return Returns the new group.
     */
    public ResourceSummaryModelData getResource() { return _resource; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final WorkingCopyCleared.WCClearedHandler handler) {
        handler.onClear(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<WCClearedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'group created' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface WCClearedHandler extends EventHandler {


        /**
         * Handle a 'WC applied' event.
         *
         * @param event The event to handle.
         */
        void onClear(WorkingCopyCleared event);
    }


    /** TYPE : Type. */
    public static final Type<WCClearedHandler> TYPE =
        new Type<WCClearedHandler>();
}
