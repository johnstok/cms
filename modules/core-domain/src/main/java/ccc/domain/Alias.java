/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import ccc.api.AliasDelta;
import ccc.api.DBC;
import ccc.api.ID;
import ccc.api.ResourceType;


/**
 * An alias resource. Models a symbolic link that points to another
 * {@link Resource}.
 *
 * TODO: Add circular reference detection?
 *
 * @author Civic Computing Ltd
 */
public class Alias extends Resource {

    private Resource _target;


    /** Constructor: for persistence only. */
    protected Alias() { super(); }

    /**
     * Constructor.
     *
     * @param title The title for the alias.
     * @param target The target for the alias.
     */
    public Alias(final String title, final Resource target) {
        super(title);
        target(target);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceType type() {
        return ResourceType.ALIAS;
    }

    /**
     * Mutator for the target field.
     *
     * @param target The new target.
     */
    public void target(final Resource target) {
        DBC.require().notNull(target);
        _target = target;
    }

    /**
     * Accessor for the target field.
     *
     * @return The current target for this alias.
     */
    public Resource target() {
        return _target;
    }

    public AliasDelta createSnapshot() {
        final AliasDelta delta =
            new AliasDelta(
                target().name().toString(),
                new ID(target().id().toString()));
        return delta;
    }
}
