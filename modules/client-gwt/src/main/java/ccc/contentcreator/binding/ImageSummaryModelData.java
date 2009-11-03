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

import ccc.rest.dto.FileDto;
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
    private int _maxDimension;

    /**
     * Constructor.
     *
     * @param fs The file summary.
     * @param maxDimension The maximum dimension for thumb nails.
     */
    public ImageSummaryModelData(final FileDto fs,
                                 final int maxDimension) {
        _fs = fs;
        _maxDimension = maxDimension;
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

            case DWIDTH:
                return (X) getDisplayWidth();

            case DHEIGHT:
                return (X) getDisplayHeight();

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
        HEIGHT,
        DWIDTH,
        DHEIGHT;
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

    /**
     * Accessor.
     *
     * @return The display width of the image resource.
     */
    public String getDisplayWidth() {
        final int width =
            Integer.decode(getWidth()).intValue();
        final int height =
            Integer.decode(getHeight()).intValue();
        if (_maxDimension > 0
            && (width > _maxDimension || height > _maxDimension)) {
            if (width >= height) {
                return ""+_maxDimension;
            }
            final float f = (float) width/(float) height;
            return ""+f*_maxDimension;

        }
        return ""+width;
    }

    /**
     * Accessor.
     *
     * @return The height of the image resource.
     */
    public String getDisplayHeight() {
        final int width =
            Integer.decode(getWidth()).intValue();
        final int height =
            Integer.decode(getHeight()).intValue();
        if (_maxDimension > 0
            && (width > _maxDimension || height > _maxDimension)) {
            if (height >= width) {
                return ""+_maxDimension;
            }
            final float f = (float) height/(float) width;
            return ""+f*_maxDimension;

        }
        return ""+width;
    }
}
