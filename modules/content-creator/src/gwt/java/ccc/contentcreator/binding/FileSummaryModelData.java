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

import ccc.api.ActionSummary;
import ccc.api.FileSummary;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.util.Util;


/**
 * {@link ModelData} implementation for the {@link ActionSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class FileSummaryModelData
    implements
        ModelData {

    private FileSummary _fs;

    /**
     * Constructor.
     *
     * @param fs
     */
    public FileSummaryModelData(final FileSummary fs) {
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
                return (X) Util.ellipse(_fs.getTitle(), 15);

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
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getTitle() {
        return _fs.getTitle();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getPath() {
        return _fs.getTitle();
    }
}
