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
package ccc.web.exceptions;

import java.util.UUID;

import ccc.api.types.DBC;


/**
 * An exception indicating an error during the processing of a request.
 *
 * @author Civic Computing Ltd.
 */
public class RequestFailedException
    extends
        WebException {

    private static final String BASIC_MESSAGE = "Failed to complete request";

    private final UUID _uuid = UUID.randomUUID();


    /**
     * Constructor.
     *
     * @param cause The original cause of the exception.
     */
    public RequestFailedException(final Throwable cause) {
        super(
            BASIC_MESSAGE+".",
            DBC.require().notNull(cause));
    }


    /**
     * Accessor.
     *
     * @return The unique ID for this exception.
     */
    public UUID getId() { return _uuid; }


    /** {@inheritDoc} */
    @Override
    public String getMessage() { return BASIC_MESSAGE+": "+getId(); }
}
