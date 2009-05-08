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

import ccc.services.api.ActionSummary;
import ccc.services.api.ID;
import ccc.services.api.UserSummary;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link ActionSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserSummaryModelData
    implements
        ModelData {

    private UserSummary _us;

    /**
     * Constructor.
     *
     * @param us
     */
    public UserSummaryModelData(final UserSummary us) {
        _us = us;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked")
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
    @Override
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<String, Object>();
        for (final Property p : Property.values()) {
            properties.put(p.name(), get(p.name()));
        }
        return properties;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<String> getPropertyNames() {
        final Set<String> names = new HashSet<String>();
        for (final Property p : Property.values()) {
            names.add(p.name());
        }
        return names;
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

    public enum Property {
        ID, EMAIL, USERNAME;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ID getId() {
        return _us.getId();
    }
}
