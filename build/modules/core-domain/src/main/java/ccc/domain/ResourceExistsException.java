/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
