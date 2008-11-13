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

package ccc.domain;

import ccc.commons.serialisation.Serializer;


/**
 * Abstract base class extended by entity classes that require persistence
 * with optimistic locking.
 *
 * @author Civic Computing Ltd.
 */
public abstract class VersionedEntity extends Entity {

    private int  _version = -1;

    /** Constructor: for persistence only. */
    protected VersionedEntity() { super(); }


    /**
     * Accessor for the version field.
     *
     * @return This entity's version, as an integer.
     */
    public int version() {
        return _version;
    }


    /**
     * Mutator for the version property.
     *
     * @param version The new version to set.
     */
    public void version(final int version) {
        _version = version;
    }


    /** {@inheritDoc} */
    @Override
    public void serialize(final Serializer s) {
        super.serialize(s);
        s.number("version", version());
    }


}
