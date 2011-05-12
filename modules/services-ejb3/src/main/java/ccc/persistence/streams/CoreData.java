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
package ccc.persistence.streams;

import java.io.InputStream;

import ccc.api.types.StreamAction;
import ccc.domain.Data;


/**
 * Abstraction for reading / writing data streams.
 *
 * @author Civic Computing Ltd.
 */
public interface CoreData {

    /**
     * Create a new item of binary data.
     *
     * @param dataStream The input stream from which the bytes for the new data
     *        item should be read.
     * @param length The length of the input, in bytes.
     * @return An instance of {@link Data} that represents the contents of the
     *      stream.
     */
    Data create(InputStream dataStream, final long length);

    /**
     * Retrieve an existing item of binary data and write it to an output
     * stream.
     *
     * @param data The identifier for the existing data.
     * @param action An action to perform with the retrieved data.
     */
    void retrieve(Data data, StreamAction action);
}
