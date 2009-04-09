/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import ccc.commons.CCCProperties;
import ccc.commons.DBC;
import ccc.commons.Registry;
import ccc.domain.User;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.ServiceLookup;
import ccc.services.StatefulReader;
import ccc.services.UserManager;


/**
 * Default implementation of the {@link ObjectFactory} interface.
 * <br><br>
 * Properties:
 * <br>* respectVisibility - true by default.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultObjectFactory implements ObjectFactory {

    private final ServiceLookup _services;
    private boolean _respectVisiblity = true;

    /**
     * Constructor.
     *
     * @param registry The registry used to look up business services.
     */
    public DefaultObjectFactory(final Registry registry) {
        DBC.require().notNull(registry);
        _services =
            new ServiceLookup(CCCProperties.get("application.name"), registry);
    }

    /** {@inheritDoc} */
    @Override
    public Renderer createRenderer() {
        return new DefaultRenderer(
            dataManager(), searchEngine(), getReader(), _respectVisiblity);
    }


    /** {@inheritDoc} */
    @Override
    public StatefulReader getReader() {
        return _services.statefulReader();
    }


    /** {@inheritDoc} */
    @Override
    public void setRespectVisibility(final String newValue) {
        if ("false".equals(newValue)) {
            _respectVisiblity = false;
        } else {
            _respectVisiblity = true;
        }
    }


    /** {@inheritDoc} */
    @Override
    public User currentUser() {
        return userManager().loggedInUser();
    }


    /** {@inheritDoc} */
    @Override
    public boolean getRespectVisibility() {
        return _respectVisiblity;
    }


    private DataManager dataManager() {
        return _services.dataManager();
    }


    private SearchEngine searchEngine() {
        return _services.localSearchEngine();
    }


    private UserManager userManager() {
        return _services.localUserManager();
    }
}
