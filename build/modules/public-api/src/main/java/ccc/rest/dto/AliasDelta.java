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
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;


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
