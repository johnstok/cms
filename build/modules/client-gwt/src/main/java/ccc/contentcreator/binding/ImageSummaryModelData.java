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
import java.util.Set;
import java.util.UUID;

import ccc.api.dto.FileDto;
import ccc.types.FilePropertyNames;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * {@link ModelData} implementation for the {@link FileDto} class.
 *
 * @author Civic Computing Ltd.
 */
public class ImageSummaryModelData
    implements
        ModelData {

    private FileDto _fs;

    /**
     * Constructor.
     *
     * @param fs The file summary.
     */
    public ImageSummaryModelData(final FileDto fs) {
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

            case WIDTH:
                return (X) getWidth();

            case HEIGHT:
                return (X) getHeight();

            case TITLE:
            case SHORT_NAME:
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

    /**
     * Property names for a file summary.
     */
    private enum Property {
        ID,
        NAME,
        TITLE,
        MIME_TYPE,
        PATH,
        SHORT_NAME,
        WIDTH,
        HEIGHT;
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

    /**
     * Accessor.
     *
     * @return The width of the image resource.
     */
    public String getWidth() {
        return _fs.getProperties().get(FilePropertyNames.WIDTH);
    }

    /**
     * Accessor.
     *
     * @return The height of the image resource.
     */
    public String getHeight() {
        return _fs.getProperties().get(FilePropertyNames.HEIGHT);
    }

}
