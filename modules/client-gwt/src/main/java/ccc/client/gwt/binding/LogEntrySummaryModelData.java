/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.binding;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.api.core.Revision;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link Revision} class.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntrySummaryModelData
    implements
        ModelData {

    /** EXPAND_PROPERTY : String. */
    public static final String EXPAND_PROPERTY = Property.COMMENT.name();

    private final Revision _les;

    /**
     * Constructor.
     *
     * @param les The log entry summary to wrap.
     */
    public LogEntrySummaryModelData(final Revision les) {
        _les = les;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case USERNAME:
                return (X) _les.getActorUsername();

            case COMMENT:
                return (X) _les.getComment();

            case HAPPENED_ON:
                return (X) _les.getHappenedOn();

            case INDEX:
                return (X) Long.valueOf(_les.getIndex());

            case IS_MAJOR_EDIT:
                return (X) Boolean.valueOf(_les.isMajor());

            default:
                throw new UnsupportedOperationException(
                    "Key not supported: "+property);
        }
    }


    /** {@inheritDoc} */
    @Override @Deprecated
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<String, Object>();
        for (final Property p : Property.values()) {
            properties.put(p.name(), get(p.name()));
        }
        return properties;
    }


    /** {@inheritDoc} */
    @Override @Deprecated
    public Collection<String> getPropertyNames() {
        final Set<String> names = new HashSet<String>();
        for (final Property p : Property.values()) {
            names.add(p.name());
        }
        return names;
    }


    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X set(final String property, final X value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /**
     * Property names.
     *
     * @author Civic Computing Ltd.
     */
    public enum Property {
        /** USERNAME : Property. */
        USERNAME,
        /** HAPPENED_ON : Property. */
        HAPPENED_ON,
        /** COMMENT : Property. */
        COMMENT,
        /** IS_MAJOR_EDIT : Property. */
        IS_MAJOR_EDIT,
        /** INDEX : Property. */
        INDEX;
    }


    /**
     * Accessor.
     *
     * @return The Index.
     */
    public long getIndex() {
        return _les.getIndex();
    }
}
