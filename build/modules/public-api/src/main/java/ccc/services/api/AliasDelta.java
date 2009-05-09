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
package ccc.services.api;

import java.io.Serializable;


/**
 * A delta for creating / updating aliases.
 *
 * @author Civic Computing Ltd.
 */
public final class AliasDelta implements Serializable {
    private ID _id;
    private String _targetName;
    private ID _targetId;

    @SuppressWarnings("unused") private AliasDelta() { super(); }

    /**
     * Constructor.
     *
     * @param id The alias' Id.
     * @param targetName The alias' target's name.
     * @param targetId The alias' target's id.
     */
    public AliasDelta(final ID id,
                      final String targetName,
                      final ID targetId) {
        _id = id;
        _targetName = targetName;
        _targetId = targetId;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public ID getId() {
        return _id;
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final ID id) {
        _id = id;
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
     * Mutator.
     *
     * @param targetName The targetName to set.
     */
    public void setTargetName(final String targetName) {
        _targetName = targetName;
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
}
