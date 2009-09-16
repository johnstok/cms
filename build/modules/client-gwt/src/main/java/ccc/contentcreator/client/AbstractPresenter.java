/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;


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
