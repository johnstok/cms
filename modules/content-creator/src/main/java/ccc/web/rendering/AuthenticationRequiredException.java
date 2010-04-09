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
package ccc.web.rendering;

import ccc.api.types.DBC;


/**
 * An exception indicating that a user attempted to access a resource without
 * sufficient privileges.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated // FIXME Use the API exception.
public class AuthenticationRequiredException
    extends
        RuntimeException {

    private final String _resource;

    /**
     * Constructor.
     *
     * @param r The path to the resource that requires authentication.
     */
    public AuthenticationRequiredException(final String r) {
        DBC.require().notNull(r);
        _resource = r;
    }

    /**
     * Accessor.
     *
     * @return The path to the resource that requires authentication.
     */
    public String getTarget() {
        return _resource;
    }
}
