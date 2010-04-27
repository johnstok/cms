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
package ccc.domain;

import ccc.api.types.DBC;
import ccc.api.types.ACL.Entry;


/**
 * An access permission for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class AccessPermission {

    private boolean   _canRead   = false;
    private boolean   _canWrite  = false;
    private Principal _principal = null;


    /**
     * Constructor.
     */
    protected AccessPermission() { super(); }


    /**
     * Constructor.
     *
     * @param canRead Can the principal perform a read.
     * @param canWrite Can the principal perform a write.
     * @param principal The principal this permission manages.
     */
    public AccessPermission(final boolean canRead,
                            final boolean canWrite,
                            final Principal principal) {
        _canRead = canRead;
        _canWrite = canWrite;
        _principal = DBC.require().notNull(principal);
    }


    /**
     * Check whether this permission allows the specified user to read.
     *
     * @param user The user to test.
     *
     * @return Returns true if a read is allowed; false otherwise.
     */
    public boolean allowsRead(final UserEntity user) {
        return _canRead && _principal.includes(user);
    }


    /**
     * Check whether this permission allows the specified user to write.
     *
     * @param user The user to test.
     *
     * @return Returns true if a write is allowed; false otherwise.
     */
    public boolean allowsWrite(final UserEntity user) {
        return _canWrite && _principal.includes(user);
    }


    /**
     * Accessor.
     *
     * @return Returns the principal.
     */
    public Principal getPrincipal() {
        return _principal;
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            prime
            * result
            + ((_principal == null) ? 0 : _principal.hashCode());
        return result;
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccessPermission other = (AccessPermission) obj;
        if (_principal == null) {
            if (other._principal != null) {
                return false;
            }
        } else if (!_principal.equals(other._principal)) {
            return false;
        }
        return true;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public Entry createEntry() {
        final Entry e = new Entry();
        e._canRead = _canRead;
        e._canWrite = _canWrite;
        e._name = _principal.getName();
        e._principal = _principal.getId();
        return e;
    }
}
