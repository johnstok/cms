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

import java.util.Map;

import ccc.domain.Data;
import ccc.services.DataManager;


/**
 * Helper class for processing files.
 *
 * @author Civic Computing Ltd.
 */
public class FileHelper {

    /**
     * Extract the image metadata for a file.
     *
     * @param props The file properties.
     * @param dm The data manager for reading the data.
     */
    protected void extractImageMetadata(final Data data,
                                        final Map<String, String> props,
                                        final DataManager dm) {
        final ImageMetadataStreamAction img = new ImageMetadataStreamAction();
        dm.retrieve(data, img);
        props.putAll(img.getMetadata());
    }
}
