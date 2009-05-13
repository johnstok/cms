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
package ccc.domain;

import ccc.services.api.ActionType;
import ccc.services.api.CCCRemoteException;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceExistsException
    extends
        RemoteExceptionSupport {

    private ResourceName _name;
    private Folder       _folder;

    /**
     * Constructor.
     *
     * @param folder
     * @param name
     */
    public ResourceExistsException(final Folder folder,
                                   final ResourceName name) {
        _folder = folder;
        _name = name;
    }


    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return "Folder already contains a resource with name '" + _name + "'.";
    }


    /** {@inheritDoc} */
    @Override
    public CCCRemoteException toRemoteException(final ActionType action) {
        return new CCCRemoteException(
            CCCRemoteException.EXISTS, action, getUUID().toString());
    }
}
