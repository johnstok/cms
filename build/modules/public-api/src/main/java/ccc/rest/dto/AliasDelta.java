/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.rest.dto;

import java.io.Serializable;
import java.util.UUID;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;


/**
 * A delta for creating / updating aliases.
 *
 * @author Civic Computing Ltd.
 */
public final class AliasDelta implements Serializable, Jsonable {
    private UUID _targetId;

    @SuppressWarnings("unused") private AliasDelta() { super(); }

    /**
     * Constructor.
     *
     * @param targetId The alias' target's id.
     */
    public AliasDelta(final UUID targetId) {
        _targetId = targetId;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation for this class.
     */
    public AliasDelta(final Json json) {
        this(
            json.getId(JsonKeys.TARGET_ID)
        );
    }

    /**
     * Accessor.
     *
     * @return Returns the targetId.
     */
    public UUID getTargetId() {
        return _targetId;
    }


    /**
     * Mutator.
     *
     * @param targetId The targetId to set.
     */
    public void setTargetId(final UUID targetId) {
        _targetId = targetId;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.TARGET_ID, getTargetId());
    }
}
