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

import ccc.rest.dto.TemplateSummary;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link TemplateSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateSummaryModelData
    implements
        ModelData {

    private TemplateSummary _ts;

    /**
     * Constructor.
     *
     * @param ts The template summary to wrap.
     */
    public TemplateSummaryModelData(final TemplateSummary ts) {
        _ts = ts;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case ID:
                return (X) _ts.getId();

            case BODY:
                return (X) _ts.getBody();

            case DEFINITION:
                return (X) _ts.getDefinition();

            case DESCRIPTION:
                return (X) _ts.getDescription();

            case NAME:
                return (X) _ts.getName();

            case TITLE:
                return (X) _ts.getTitle();

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
     * Property names for a template summary.
     *
     * @author Civic Computing Ltd.
     */
    public enum Property {
        /** ID : Property. */
        ID,
        /** NAME : Property. */
        NAME,
        /** TITLE : Property. */
        TITLE,
        /** DESCRIPTION : Property. */
        DESCRIPTION,
        /** BODY : Property. */
        BODY,
        /** DEFINITION : Property. */
        DEFINITION;
    }

    /**
     * Accessor.
     *
     * @return The ID.
     */
    public UUID getId() {
        return _ts.getId();
    }

    /**
     * Accessor.
     *
     * @return The definition.
     */
    public String getDefinition() {
        return _ts.getDefinition();
    }

    /**
     * Accessor.
     *
     * @return The description.
     */
    public String getDescription() {
        return _ts.getDescription();
    }

    /**
     * Accessor.
     *
     * @return The name.
     */
    public String getName() {
        return _ts.getName();
    }
}
