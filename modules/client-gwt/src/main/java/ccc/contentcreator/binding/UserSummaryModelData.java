/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
import java.util.Set;
import java.util.UUID;

import ccc.rest.dto.UserDto;
import ccc.types.Username;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link UserDto} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserSummaryModelData
    implements
        ModelData {

    private UserDto _us;

    /**
     * Constructor.
     *
     * @param us The User summary.
     */
    public UserSummaryModelData(final UserDto us) {
        _us = us;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case ID:
                return (X) _us.getId();

            case EMAIL:
                return (X) _us.getEmail();

            case USERNAME:
                return (X) _us.getUsername();

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
     * Property names for a user summary.
     *
     * @author Civic Computing Ltd.
     */
    public enum Property {
        /** ID : Property. */
        ID,
        /** EMAIL : Property. */
        EMAIL,
        /** USERNAME : Property. */
        USERNAME;
    }

    /**
     * Accessor.
     *
     * @return The Id.
     */
    public UUID getId() {
        return _us.getId();
    }

    /**
     * Accessor.
     *
     * @return The username.
     */
    public Username getUsername() {
        return _us.getUsername();
    }
}
