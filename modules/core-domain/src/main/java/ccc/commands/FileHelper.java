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

import ccc.domain.File;
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
     * @param f The file to read.
     * @param data The data manager for reading the data.
     */
    protected void extractImageMetadata(final File f, final DataManager data) {
        final ImageMetadataStreamAction img = new ImageMetadataStreamAction();
        data.retrieve(f.data(), img);
        f.addMetadata(img.getMetadata());
    }
}
