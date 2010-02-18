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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * A GXT model datum that wraps an Java 5 enum.
 *
 * @param <T> The type of enum this model data wraps.
 *
 * @author Civic Computing Ltd.
 */
public class EnumModelData<T extends Enum<T>>
    implements
        ModelData {

    private final T _enumValue;


    /**
     * Constructor.
     *
     * @param value The enum value for this model data.
     */
    public EnumModelData(final T value) {
        _enumValue = value;
    }


    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked")
    public <X> X get(final String property) {
        if ("name".equals(property)) {
            return (X) _enumValue.name();
        }
        throw new IllegalArgumentException("No such property: "+property);
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getProperties() {
        return Collections.singletonMap("name", (Object) _enumValue.name());
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> getPropertyNames() {
        return Collections.singletonList("name");
    }


    /** {@inheritDoc} */
    @Override
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("EnumModelData is read-only.");
    }


    /** {@inheritDoc} */
    @Override
    public <X> X set(final String property, final X value) {
        throw new UnsupportedOperationException("EnumModelData is read-only.");
    }


    /**
     * Accessor.
     *
     * @return Return the enum value this model data wraps.
     */
    public T getValue() {
        return _enumValue;
    }


    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            prime * result + ((_enumValue == null) ? 0 : _enumValue.hashCode());
        return result;
    }


    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final EnumModelData<?> other = (EnumModelData<?>) obj;
        if (_enumValue == null) {
            if (other._enumValue != null) { return false; }
        } else if (!_enumValue.equals(other._enumValue)) { return false; }
        return true;
    }
}
