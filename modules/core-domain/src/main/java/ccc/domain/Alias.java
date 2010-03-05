/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasDto;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.DBC;
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
    public ResourceType getType() {
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
        if (null==_target) {
            return null;
        } else if (_target.isDeleted()) {
            return null;
        }
        return _target;
    }

    /** {@inheritDoc} */
    @Override
    public AliasDelta createSnapshot() {
        final AliasDelta delta =
            new AliasDelta(target().getId());
        return delta;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(
            JsonKeys.TARGET_ID,
            (null==target()) ? null : target().getId().toString());
    }




    /* ====================================================================
     * Snapshot support.
     * ================================================================== */

    /** {@inheritDoc} */
    @Override
    public final AliasDto forWorkingCopy() {
        return createDto();
    }

    /** {@inheritDoc} */
    @Override
    public final AliasDto forCurrentRevision() {
        return createDto();
    }

    /** {@inheritDoc} */
    @Override
    public final AliasDto forSpecificRevision(final int revNo) {
        return createDto();
    }

    private AliasDto createDto() {
        final AliasDto dto =
            new AliasDto(
                getParent().getId(),
                getName(),
                (null==target())?null:target().getId());
        dto.setTargetPath(
            (null==target())
                ? null
                : target().getAbsolutePath().removeTop().toString());
        setDtoProps(dto);
        return dto;
    }


    /**
     * Create a delta for an alias.
     *
     * @return A corresponding delta.
     */
    public AliasDelta deltaAlias() {
        return createSnapshot();
    }
}
