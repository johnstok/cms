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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


/**
 * An event indicating a text file was updated.
 *
 * @author Civic Computing Ltd.
 */
public class TextFileUpdated
    extends
        GwtEvent<TextFileUpdated.UpdatedHandler> {


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final TextFileUpdated.UpdatedHandler handler) {
        handler.onUpdated(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<UpdatedHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'text file updated' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface UpdatedHandler extends EventHandler {


        /**
         * Handle a 'text file updated' event.
         *
         * @param event The event to handle.
         */
        void onUpdated(TextFileUpdated event);
    }


    /** TYPE : Type. */
    public static final Type<UpdatedHandler> TYPE =
        new Type<UpdatedHandler>();
}
