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
 * An event indicating an error occurred.
 * FIXME Rename this class - collides with JRE class name.
 *
 * @author Civic Computing Ltd.
 */
public class Error
    extends
        GwtEvent<Error.ErrorHandler> {

    private final Throwable _throwable;
    private final String    _name;


    /**
     * Constructor.
     *
     * @param throwable The exception that occurred.
     * @param name The action being performed when the error occurred.
     */
    public Error(final Throwable throwable, final String name) {
        _throwable = throwable;
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the exception.
     */
    public Throwable getException() { return _throwable; }


    /**
     * Accessor.
     *
     * @return Returns the action name.
     */
    public String getName() { return _name; }


    /** {@inheritDoc} */
    @Override
    protected void dispatch(final Error.ErrorHandler handler) {
        handler.onError(this);
    }


    /** {@inheritDoc} */
    @Override
    public Type<ErrorHandler> getAssociatedType() { return TYPE; }


    /**
     * Handler for 'error' events.
     *
     * @author Civic Computing Ltd.
     */
    public static interface ErrorHandler extends EventHandler {


        /**
         * Handle an 'error' event.
         *
         * @param event The event to handle.
         */
        void onError(Error event);
    }


    /** TYPE : Type. */
    public static final Type<ErrorHandler> TYPE =
        new Type<ErrorHandler>();
}
