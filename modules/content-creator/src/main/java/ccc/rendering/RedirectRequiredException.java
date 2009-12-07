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
package ccc.rendering;

import ccc.domain.CCCException;
import ccc.types.DBC;


/**
 * An exception indicating that a redirect is required.
 *
 * @author Civic Computing Ltd.
 */
public class RedirectRequiredException
    extends
        CCCException {

    private final String _target;

    /**
     * Constructor.
     *
     * @param target The target path for the redirect.
     */
    public RedirectRequiredException(final String target) {
        DBC.require().notNull(target);
        _target = target;
    }

    /**
     * Accessor for the Resource which is the target of the redirect.
     *
     * @return The redirect's target, a Resource.
     */
    public String getTarget() {
        return _target;
    }

}
