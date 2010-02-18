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
package ccc.contentcreator.core;



/**
 * Abstract helper class for implementing MVP presenters.
 *
 * @param <T> The type of the MVP view.
 * @param <U> The type of the MVP model.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractPresenter<T, U> {

    private final IGlobals _globals;
    private final EventBus _bus;
    private final T _view;
    private final U _model;


    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param bus Implementation of the Event Bus API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public AbstractPresenter(final IGlobals globals,
                             final EventBus bus,
                             final T view,
                             final U model) {
        _globals = globals;
        _bus = bus;
        _view = view;
        _model = model;
    }


    /**
     * Accessor.
     *
     * @return Returns the globals.
     */
    public IGlobals getGlobals() {
        return _globals;
    }


    /**
     * Accessor.
     *
     * @return Returns the bus.
     */
    public EventBus getBus() {
        return _bus;
    }


    /**
     * Accessor.
     *
     * @return Returns the view.
     */
    public T getView() {
        return _view;
    }


    /**
     * Accessor.
     *
     * @return Returns the model.
     */
    public U getModel() {
        return _model;
    }
}
