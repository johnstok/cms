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
package ccc.commons;

import ccc.api.core.PageCriteria;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.UserCriteria;


/**
 * Object factory of criteria objects for Velocity context use.
 *
 * @author Civic Computing Ltd.
 */
public class CCObjectFactory {
    private CCObjectFactory() {
        // NO-OP
    }

    /**
     * Factory method for PageCriteria.
     *
     * @return New PageCriteria object.
     */
    public static PageCriteria createPageCriteria() {
        return new PageCriteria();
    }

    /**
     * Factory method for ResourceCriteria.
     *
     * @return New ResourceCriteria object.
     */
    public static ResourceCriteria createResourceCriteria() {
        return new ResourceCriteria();
    }

    /**
     * Factory method for UserCriteria.
     *
     * @return New UserCriteria object.
     */
    public static UserCriteria createUserCriteria() {
        return new UserCriteria();
    }

}
