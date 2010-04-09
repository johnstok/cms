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
package ccc.api.exceptions;

import java.util.UUID;

import ccc.types.DBC;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * This exception is thrown when a working copy command is attempted for a
 * resource that doesn't support working copies.
 *
 * @author Civic Computing Ltd.
 */
public class WorkingCopyNotSupportedException
    extends
        InvalidException {

    private final UUID _resource;


    /**
     * Constructor.
     *
     * @param resource The resource.
     */
    public WorkingCopyNotSupportedException(final UUID resource) {
        super(new Failure(FailureCode.WC_UNSUPPORTED));
        DBC.require().notNull(resource);
        _resource = resource;
    }


    /**
     * Accessor for the resource.
     *
     * @return The resource.
     */
    public UUID getResource() {
        return _resource;
    }


    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return
            "Resource "
            + _resource
            + " is not working copy aware.";
    }
}
