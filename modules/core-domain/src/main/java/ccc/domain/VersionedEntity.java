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



/**
 * Abstract base class extended by entity classes that require persistence
 * with optimistic locking.
 *
 * @author Civic Computing Ltd.
 */
public abstract class VersionedEntity extends Entity {

    private long  _version = -1;

    /** Constructor: for persistence only. */
    protected VersionedEntity() { super(); }


    /**
     * Accessor for the version field.
     *
     * @return This entity's version, as an integer.
     */
    public long version() {
        return _version;
    }


    /**
     * Mutator for the version property.
     *
     * @param version The new version to set.
     */
    public void version(final long version) {
        _version = version;
    }
}
