/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.api.exceptions;

import java.util.Map;
import java.util.UUID;


/**
 * An API call failed due to a conflict with the current state of the resource.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ConflictException
    extends
        CCException {

    /** RES_NAME : String. */
    protected static final String RES_NAME = "existing_name";
    /** RES_ID : String. */
    protected static final String RES_ID   = "existing_id";


    /** Constructor. */
    protected ConflictException() { super(); }


    /**
     * Constructor.
     *
     * @param message Description of the exception.
     * @param params  Further details describing the exception.
     */
    public ConflictException(final String message,
                             final Map<String, String> params) {
        super(message, null, params);
    }


    /**
     * Accessor for the resource.
     *
     * @return The resource.
     */
    public UUID getResourceId() { return UUID.fromString(getParam(RES_ID)); }


    /**
     * Accessor for the resource.
     *
     * @return The resource.
     */
    public String getResourceName() { return getParam(RES_NAME); }
}
