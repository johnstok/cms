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

import ccc.rest.dto.AliasDelta;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.snapshots.AliasSnapshot;
import ccc.types.DBC;
import ccc.types.ID;
import ccc.types.ResourceType;


/**
 * An alias resource. Models a symbolic link that points to another
 * {@link Resource}.
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
     * @throws CycleDetectedException If targeting the specified resource would
     *  cause a circular dependency.
     */
    public Alias(final String title,
                 final Resource target) throws CycleDetectedException {
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
     * @throws CycleDetectedException If targeting the specified resource would
     *  cause a circular dependency.
     */
    public void target(final Resource target) throws CycleDetectedException {
        DBC.require().notNull(target);
        if (equals(target) || isTargetedBy(target)) {
            throw new CycleDetectedException();
        }
        _target = target;
    }

    private boolean isTargetedBy(final Resource target) {
        if (target instanceof Alias) {
            final Alias alias = (Alias) target;
            return equals(alias.target()) || isTargetedBy(alias.target());
        }
        return false;
    }

    /**
     * Accessor for the target field.
     *
     * @return The current target for this alias.
     */
    public Resource target() {
        return _target;
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta createSnapshot() {
        final AliasDelta delta =
            new AliasDelta(new ID(target().id().toString()));
        return delta;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.TARGET_ID, target().id().toString());
    }




    /* ====================================================================
     * Snapshot support.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public final AliasSnapshot forWorkingCopy() {
        return new AliasSnapshot(this);
    }

    /** {@inheritDoc} */
    @Override
    public final AliasSnapshot forCurrentRevision() {
        return new AliasSnapshot(this);
    }

    /** {@inheritDoc} */
    @Override
    public final AliasSnapshot forSpecificRevision(final int revNo) {
        return new AliasSnapshot(this);
    }
}
