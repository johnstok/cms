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
package ccc.rest.exceptions;

import java.util.Collections;
import java.util.UUID;

import ccc.types.Failure;
import ccc.types.FailureCode;
import ccc.types.ResourceName;


/**
 * Exception thrown when a resource cannot be created because an existing
 * resource already has the same absolute path.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceExistsException
    extends
        ConflictException {

    private ResourceName _resourceName;
    private UUID         _resourceId;


    /**
     * Constructor.
     *
     * @param resourceId   The ID of the existing resource.
     * @param resourceName The name of the existing resource.
     */
    public ResourceExistsException(final UUID resourceId,
                                   final ResourceName resourceName) {
        super(
            new Failure(
                FailureCode.EXISTS,
                Collections.singletonMap(
                    "existing_id", resourceId.toString())));
        _resourceId = resourceId;
        _resourceName = resourceName;
    }


    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return
            "Folder already contains a resource with name '"
            + _resourceName
            + "'.";
    }
}
