/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.binding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import ccc.api.dto.GroupDto;
import ccc.plugins.s11n.JsonKeys;



/**
 * Gxt model data binding for a group.
 *
 * @author Civic Computing Ltd.
 */
public final class GroupModelData
    extends
        CccModelData {

    private GroupDto _delegate;


    /**
     * Constructor.
     *
     * @param delegate The DTO this model delegates to.
     */
    public GroupModelData(final GroupDto delegate) {
        _delegate = delegate;
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public <X> X get(final String property) {
        if (property.equals(JsonKeys.ID)) {
            return (X) _delegate.getId();
        } else if (property.equals(JsonKeys.NAME)) {
            return (X) _delegate.getName();
        } else if (property.equals(JsonKeys.PERMISSIONS)) {
            return (X) _delegate.getPermissions();
        } else {
            throw new IllegalArgumentException("Key not supported: "+property);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> getPropertyNames() {
        return
            Arrays.asList(
                JsonKeys.ID,
                JsonKeys.NAME,
                JsonKeys.PERMISSIONS);
    }


    /** {@inheritDoc} */
    @Override
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public <X> X set(final String property, final X value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /**
     * Mutator.
     *
     * @param delegate The DTO this model data delegates to.
     */
    public void setDelegate(final GroupDto delegate) {
        _delegate = delegate;
    }


    /**
     * Accessor.
     *
     * @return The underlying DTO.
     */
    public GroupDto getDelegate() {
        return _delegate;
    }


    /** {@inheritDoc} */
    @Override
    public UUID getId() { return get(JsonKeys.ID); }
}
