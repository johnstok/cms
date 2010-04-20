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
package ccc.api.exceptions;

import java.util.HashMap;
import java.util.UUID;


/**
 * Exception indicating that the look up of an entity failed.
 *
 * @author Civic Computing Ltd.
 */
public class EntityNotFoundException
    extends
        CCException {

    private static final String ENTITY   = "id";


    /** Constructor. */
    public EntityNotFoundException() { super(); }


    /**
     * Constructor.
     *
     * @param entity The entity's id.
     */
    public EntityNotFoundException(final UUID entity) {
        super(
            "No entity with specified ID: "
                + entity
                + ".",
            null,
            new HashMap<String, String>() {{
                put(ENTITY,   (null==entity) ? null : entity.toString());
            }});
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
        final String entity = getParam(ENTITY);
        return (null==entity) ? null : UUID.fromString(entity);
    }
}
