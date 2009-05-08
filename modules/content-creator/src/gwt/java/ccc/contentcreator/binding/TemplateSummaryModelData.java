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

import ccc.services.api.ID;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link TemplateDelta} class.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateSummaryModelData
    implements
        ModelData {

    private TemplateDelta _td;

    /**
     * Constructor.
     *
     * @param td The template delta to wrap.
     */
    public TemplateSummaryModelData(final TemplateDelta td) {
        _td = td;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked")
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case ID:
                return (X) _td.getId();

            case BODY:
                return (X) _td.getBody();

            case DEFINITION:
                return (X) _td.getDefinition();

            case DESCRIPTION:
                return (X) _td.getDescription();

            case NAME:
                return (X) _td.getName();

            case TITLE:
                return (X) _td.getTitle();

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
        ID, NAME, TITLE, DESCRIPTION, BODY, DEFINITION;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ID getId() {
        return _td.getId();
    }
}
