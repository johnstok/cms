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
package ccc.domain;

import java.util.Map;

import ccc.persistence.DataRepository;


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
                                     final DataRepository dm) {
        final ImageMetadataStreamAction img = new ImageMetadataStreamAction();
        dm.retrieve(data, img);
        props.putAll(img.getMetadata());
    }
}
