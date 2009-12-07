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
package ccc.contentcreator.binding;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import ccc.contentcreator.api.CommandTypeConstants;
import ccc.contentcreator.client.IGlobals;
import ccc.rest.dto.RevisionDto;
import ccc.types.CommandType;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link RevisionDto} class.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntrySummaryModelData
    implements
        ModelData {

    /** EXPAND_PROPERTY : String. */
    public static final String EXPAND_PROPERTY = Property.COMMENT.name();

    private final RevisionDto _les;
    private IGlobals _globals;

    /**
     * Constructor.
     *
     * @param les The log entry summary to wrap.
     * @param globals The globals.
     */
    public LogEntrySummaryModelData(final RevisionDto les,
                                    final IGlobals globals) {
        _les = les;
        _globals = globals;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case USERNAME:
                return (X) _les.getActorUsername();

            case COMMAND:
                return (X) _les.getCommand();

            case LOCALISED_ACTION:
                return (X) getLocalisedAction();

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
        /** COMMAND : Property. */
        COMMAND,
        /** LOCALISED_ACTION : Property. */
        LOCALISED_ACTION,
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
     * @return The action.
     */
    public CommandType getAction() {
        return _les.getCommand();
    }

    /**
     * Looks up for localised string for the {@link CommandType}.
     *
     * @return The localised string or name of the enum if nothing found.
     */
    public String getLocalisedAction() {
        final CommandTypeConstants types = _globals.commandTypeConstants();

        String local = null;
        try {
            local = types.getString(_les.getCommand().camelCaseName());
        } catch (final MissingResourceException e) {
            local = _les.getCommand().name();
        }
        return local;
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
