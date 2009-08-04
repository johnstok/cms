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

import java.util.Collections;

import ccc.api.FailureCodes;
import ccc.commands.CommandFailedException;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceExistsException
    extends
        RemoteExceptionSupport {

    private Resource     _resource;
    private Folder       _folder;

    /**
     * Constructor.
     *
     * @param folder The folder in which the resource exists.
     * @param resource The existing resource.
     */
    public ResourceExistsException(final Folder folder,
                                   final Resource resource) {
        _folder = folder;
        _resource = resource;
    }


    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return
            "Folder already contains a resource with name '"
            + _resource.name()
            + "'.";
    }


    /** {@inheritDoc} */
    @Override
    public CommandFailedException toRemoteException() {
        return
            new CommandFailedException(
                new Failure(
                    FailureCodes.EXISTS,
                    getUUID().toString(),
                    Collections.singletonMap(
                        "existing_id", _resource.id().toString())));
    }
}
