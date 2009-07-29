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
package ccc.commands;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import ccc.domain.File;
import ccc.services.DataManager;

/**
 * A stream action used to extract metadata from an image file.
 *
 * @author Civic Computing Ltd.
 */
final class ImageMetadataStreamAction
    implements
        DataManager.StreamAction {

    private final Map<String, String> _metadata = new HashMap<String, String>();


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) throws Exception {

        final BufferedImage image = ImageIO.read(is);
        if (null==image) { // No valid image reader exists.
            return;
        }

        _metadata.clear();
        _metadata.put(File.HEIGHT, String.valueOf(image.getHeight()));
        _metadata.put(File.WIDTH, String.valueOf(image.getWidth()));
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
