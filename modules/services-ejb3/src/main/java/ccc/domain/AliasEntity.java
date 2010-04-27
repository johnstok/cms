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

import ccc.api.core.Alias;
import ccc.api.exceptions.CycleDetectedException;
import ccc.api.types.DBC;
import ccc.api.types.ResourceType;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * An alias resource. Models a symbolic link that points to another
 * {@link ResourceEntity}.
 *
 * @author Civic Computing Ltd
 */
public class AliasEntity extends ResourceEntity {

    private ResourceEntity _target;


    /** Constructor: for persistence only. */
    protected AliasEntity() { super(); }

    /**
     * Constructor.
     *
     * @param title The title for the alias.
     * @param target The target for the alias.
     */
    public AliasEntity(final String title,
                 final ResourceEntity target) {
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
     */
    public void target(final ResourceEntity target) {
        DBC.require().notNull(target);
        if (equals(target) || isTargetedBy(target)) {
            throw new CycleDetectedException(getId());
        }
        _target = target;
    }

    private boolean isTargetedBy(final ResourceEntity target) {
        if (target instanceof AliasEntity) {
            final AliasEntity alias = (AliasEntity) target;
            return equals(alias.target()) || isTargetedBy(alias.target());
        }
        return false;
    }

    /**
     * Accessor for the target field.
     *
     * @return The current target for this alias.
     */
    public ResourceEntity target() {
        if (null==_target) {
            return null;
        } else if (_target.isDeleted()) {
            return null;
        }
        return _target;
    }

    /** {@inheritDoc} */
    @Override
    public Alias createSnapshot() {
        final Alias delta =
            new Alias(target().getId());
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
    public final Alias forWorkingCopy() {
        return createDto();
    }

    /** {@inheritDoc} */
    @Override
    public final Alias forCurrentRevision() {
        return createDto();
    }

    /** {@inheritDoc} */
    @Override
    public final Alias forSpecificRevision(final int revNo) {
        return createDto();
    }

    private Alias createDto() {
        final Alias dto =
            new Alias(
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
}
