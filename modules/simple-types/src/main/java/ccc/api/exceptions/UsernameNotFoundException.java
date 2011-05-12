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
 * Revision      $Rev: 2841 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-05-27 18:22:22 +0100 (Thu, 27 May 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.exceptions;

import java.util.HashMap;


/**
 * This exception indicates that an user was not found. User's username has 
 * been modified or it never existed.
 *
 * @author Civic Computing Ltd.
 */
public class UsernameNotFoundException
    extends
    InvalidException {
    
    private static final String ENTITY   = "username";

    /** Constructor. */
    public UsernameNotFoundException() { super(); }


    /**
     * Constructor.
     *
     * @param resource The resource for which a cycle was detected.
     */
    public UsernameNotFoundException(final String username) {
        super(
            "No user with username: "
                + username
                + ".",
            new HashMap<String, String>() {{
                put(ENTITY,   (null==username) ? null : username.toString());
            }});
    }
}
