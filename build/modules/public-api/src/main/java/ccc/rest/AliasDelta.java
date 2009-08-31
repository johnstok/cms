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
package ccc.rest;

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
    private String _targetName;
    private ID _targetId;

    @SuppressWarnings("unused") private AliasDelta() { super(); }

    /**
     * Constructor.
     *
     * @param targetName The alias' target's name.
     * @param targetId The alias' target's id.
     */
    public AliasDelta(final String targetName,
                      final ID targetId) {
        _targetName = targetName;
        _targetId = targetId;
    }


    /**
     * Constructor.
     *
     * @param json
     */
    public AliasDelta(final Json json) {
        this(
            json.getString("target-name"), // TODO: Use JsonKeys
            json.getId(JsonKeys.TARGET)
        );
    }

    /**
     * Accessor.
     *
     * @return Returns the targetName.
     */
    public String getTargetName() {
        return _targetName;
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
        json.set("target-name", getTargetName()); // TODO: Use JsonKeys
        json.set(JsonKeys.TARGET, getTargetId());
    }
}
