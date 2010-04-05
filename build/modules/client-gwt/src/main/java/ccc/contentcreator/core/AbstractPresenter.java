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

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.widgets.ContentCreator;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;



/**
 * Abstract helper class for implementing MVP presenters.
 *
 * @param <T> The type of the MVP view.
 * @param <U> The type of the MVP model.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractPresenter<T, U> {

    private final Globals _globals;
    private final T _view;
    private final U _model;
    private final List<HandlerRegistration> _handlers =
        new ArrayList<HandlerRegistration>();


    /**
     * Constructor.
     *
     * @param globals Implementation of the Globals API.
     * @param view View implementation.
     * @param model Model implementation.
     */
    public AbstractPresenter(final Globals globals,
                             final T view,
                             final U model) {
        _globals = globals;
        _view = view;
        _model = model;
    }


    protected <R extends EventHandler> void addHandler(
                                                 final GwtEvent.Type<R> event,
                                                 final R handler) {
        _handlers.add(ContentCreator.EVENT_BUS.addHandler(event, handler));
    }


    protected void clearHandlers() {
        for (final HandlerRegistration hr : _handlers) {
            hr.removeHandler();
        }
        _handlers.clear();
    }


    /**
     * Accessor.
     *
     * @return Returns the globals.
     */
    public Globals getGlobals() {
        return _globals;
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
