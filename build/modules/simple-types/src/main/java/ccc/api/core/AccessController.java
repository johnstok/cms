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
 * Revision      $Rev: 3252 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-10-29 17:23:55 +0100 (Fri, 29 Oct 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.core;

import java.util.Collection;
import java.util.UUID;

import ccc.api.core.ACL.Entry;
import ccc.api.types.DBC;


/**
 * Determines access to a resource by a user.
 *
 * @author Civic Computing Ltd.
 */
public class AccessController {

    private final ACL _perms;

    /**
     * Constructor.
     *
     * @param perms The permissions for the user requiring access.
     */
    public AccessController(final ACL perms) {
        _perms = DBC.require().notNull(perms);
    }


    /**
     * Test whether a collection of ACLs allow this controller read access.
     *
     * @param acls The ACLs for the resource.
     *
     * @return True if this controller can read.
     */
    public boolean canRead(final Collection<ACL> acls) {
        for (final ACL acl : acls) {
            if (!isReadable(acl)) { return false; }
        }
        return true;
    }


    /**
     * Test whether a collection of ACLs allow this controller write access.
     *
     * @param acls The ACLs for the resource.
     *
     * @return True if this controller can read.
     */
    public boolean canWrite(final Collection<ACL> acls) {
        for (final ACL acl : acls) {
            if (!isWriteable(acl)) { return false; }
        }
        return true;
    }


    private boolean isWriteable(final ACL acl) {
        if (acl.getGroups().isEmpty() && acl.getUsers().isEmpty()) {
            return true;
        }

        for (final Entry group : acl.getGroups()) {
            if (group.isWriteable()
                && permsIncludeGroup(group.getPrincipal())) {
                return true;
            }
        }
        for (final Entry user : acl.getUsers()) {
            if (user.isWriteable()
                && permsIncludeUser(user.getPrincipal())) {
                return true;
            }
        }

        return false;
    }


    private boolean isReadable(final ACL acl) {
        if (acl.getGroups().isEmpty() && acl.getUsers().isEmpty()) {
            return true;
        }

        for (final Entry group : acl.getGroups()) {
            if (group.isReadable()
                && permsIncludeGroup(group.getPrincipal())) {
                return true;
            }
        }
        for (final Entry user : acl.getUsers()) {
            if (user.isReadable()
                && permsIncludeUser(user.getPrincipal())) {
                return true;
            }
        }

        return false;
    }


    private boolean permsIncludeUser(final UUID principal) {
        for (final Entry user : _perms.getUsers()) {
            if (principal.equals(user.getPrincipal())) { return true; }
        }
        return false;
    }


    private boolean permsIncludeGroup(final UUID principal) {
        for (final Entry group : _perms.getGroups()) {
            if (principal.equals(group.getPrincipal())) { return true; }
        }
        return false;
    }
}
