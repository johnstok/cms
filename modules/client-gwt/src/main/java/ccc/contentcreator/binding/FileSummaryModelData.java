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

import ccc.rest.dto.ActionSummary;
import ccc.rest.dto.FileDto;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link ActionSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class FileSummaryModelData
    implements
        ModelData {

    private FileDto _fs;

    /**
     * Constructor.
     *
     * @param fs The file summary.
     */
    public FileSummaryModelData(final FileDto fs) {
        _fs = fs;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case ID:
                return (X) _fs.getId();

            case PATH:
                return (X) _fs.getPath();

            case MIME_TYPE:
                return (X) _fs.getMimeType();

            case NAME:
                return (X) _fs.getName();

            case TITLE:
                return (X) _fs.getTitle();

            case SHORT_NAME:
//                return (X) Util.ellipse(_fs.getTitle(), 15);
                return (X) _fs.getTitle();

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

    public enum Property {
        ID, NAME, TITLE, MIME_TYPE, PATH, SHORT_NAME;
    }

    /**
     * Accessor.
     *
     * @return The title.
     */
    public String getTitle() {
        return _fs.getTitle();
    }

    /**
     * Accessor.
     *
     * @return The path.
     */
    public String getPath() {
        return _fs.getPath();
    }

    /**
     * Accessor.
     *
     * @return The ID.
     */
    public UUID getId() {
        return _fs.getId();
    }
}
