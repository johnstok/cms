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

import ccc.rest.RestException;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * Exception thrown when a resource cannot be created because an existing
 * resource already has the same absolute path.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceExistsException
    extends
        CccCheckedException {

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
    public RestException toRemoteException() {
        return
            new RestException(
                new Failure(
                    FailureCode.EXISTS,
                    Collections.singletonMap(
                        "existing_id", _resource.id().toString())));
    }
}
