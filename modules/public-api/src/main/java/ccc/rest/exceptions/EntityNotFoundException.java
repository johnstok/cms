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
package ccc.rest.exceptions;

import java.util.Collections;
import java.util.UUID;

import ccc.plugins.s11n.Json;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * Exception indicating that the look up of an entity failed.
 *
 * @author Civic Computing Ltd.
 */
public class EntityNotFoundException
    extends
        RestException {

    /**
     * Constructor.
     *
     * @param id The entity's id.
     */
    public EntityNotFoundException(final UUID id) {
        super(
            new Failure(
                FailureCode.NOT_FOUND,
                Collections.singletonMap(
                    "id", (null==id) ? null : id.toString())));
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of this exception.
     */
    public EntityNotFoundException(final Json json) {
        super(json);
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
        final String idString = getFailure().getParams().get("id");
        return (null==idString) ? null : UUID.fromString(idString);
    }


}
