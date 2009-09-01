/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ID;


/**
 * A delta for creating / updating aliases.
 *
 * @author Civic Computing Ltd.
 */
public final class AliasDelta implements Serializable, Jsonable {
    private ID _targetId;

    @SuppressWarnings("unused") private AliasDelta() { super(); }

    /**
     * Constructor.
     *
     * @param targetId The alias' target's id.
     */
    public AliasDelta(final ID targetId) {
        _targetId = targetId;
    }


    /**
     * Constructor.
     *
     * @param json
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
    public ID getTargetId() {
        return _targetId;
    }


    /**
     * Mutator.
     *
     * @param targetId The targetId to set.
     */
    public void setTargetId(final ID targetId) {
        _targetId = targetId;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.TARGET_ID, getTargetId());
    }
}
