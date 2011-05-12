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
 * An exception indicating that a redirect is required.
 *
 * @author Civic Computing Ltd.
 */
public class RedirectRequiredException
    extends
        RuntimeException {

    private final String _target;
    private final boolean _isPermanent;

    /**
     * Constructor.
     *
     * @param target The target path for the redirect.
     */
    public RedirectRequiredException(final String target) {
        this(target, false);
    }

    /**
     * Constructor.
     *
     * @param target The target path for the redirect.
     * @param isPermanent Is the redirect permanent.
     */
    public RedirectRequiredException(final String target,
                                     final boolean isPermanent) {
        DBC.require().notNull(target);
        _target = target;
        _isPermanent = isPermanent;
    }

    /**
     * Accessor for the Resource which is the target of the redirect.
     *
     * @return The redirect's target, a Resource.
     */
    public String getTarget() {
        return _target;
    }


    /**
     * Accessor.
     *
     * @return Returns true if the redirect is permanent, false otherwise.
     */
    public final boolean isPermanent() {
        return _isPermanent;
    }
}
