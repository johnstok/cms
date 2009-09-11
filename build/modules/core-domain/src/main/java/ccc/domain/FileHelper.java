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

import java.util.Map;

import ccc.persistence.FileRepository;


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
     * @param data The data to extract image metadata from.
     */
    public void extractImageMetadata(final Data data,
                                     final Map<String, String> props,
                                     final FileRepository dm) {
        final ImageMetadataStreamAction img = new ImageMetadataStreamAction();
        dm.retrieve(data, img);
        props.putAll(img.getMetadata());
    }
}
