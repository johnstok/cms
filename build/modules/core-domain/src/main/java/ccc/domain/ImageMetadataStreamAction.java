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
package ccc.domain;

import static ccc.types.FilePropertyNames.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import ccc.persistence.StreamAction;

/**
 * A stream action used to extract metadata from an image file.
 *
 * @author Civic Computing Ltd.
 */
final class ImageMetadataStreamAction
    implements
        StreamAction {

    private static final Logger LOG =
        Logger.getLogger(ImageMetadataStreamAction.class);

    private final Map<String, String> _metadata = new HashMap<String, String>();


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) {

        _metadata.clear();

        BufferedImage image = null;
        try {
            image = ImageIO.read(is);
        } catch (final IOException e) {
            LOG.warn("Error reading image.", e);
        } catch (final IllegalArgumentException e) {
            LOG.warn("Error reading image.", e);
        }
        if (null==image) { // No valid image reader exists.
            return;
        }

        _metadata.put(HEIGHT, String.valueOf(image.getHeight()));
        _metadata.put(WIDTH, String.valueOf(image.getWidth()));
    }


    /**
     * Accessor.
     *
     * @return The current metadata as a map.
     */
    public Map<String, String> getMetadata() {
        return new HashMap<String, String>(_metadata);
    }
}
