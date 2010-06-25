/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.client.core;

import ccc.api.core.Failure;
import ccc.api.types.DBC;


/**
 * An exception class representing a remote failure.
 *
 * @author Civic Computing Ltd.
 */
public class RemoteException
    extends
        Exception {

    private final Failure _failure;


    /**
     * Constructor.
     *
     * @param failure The remote failure.
     */
    public RemoteException(final Failure failure) {
        DBC.require().notNull(failure);
        _failure = failure;
    }


    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return
            "Code: "+_failure.getCode()
            + "\nID: "+_failure.getId()
            + "\nMessage: "+_failure.getParams().get("message");
    }


    /**
     * Get the code for the exception.
     *
     * @return The failure code.
     */
    public String getCode() {
        return _failure.getCode();
    }
}
