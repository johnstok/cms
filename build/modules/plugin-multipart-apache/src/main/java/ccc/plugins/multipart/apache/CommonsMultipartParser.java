/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.multipart.apache;

import java.io.InputStream;

import ccc.plugins.multipart.MultipartFormData;
import ccc.plugins.multipart.MultipartParser;


/**
 * Commons File Upload implementation of the {@link MultipartParser} API.
 *
 * @author Civic Computing Ltd.
 */
public class CommonsMultipartParser
    implements
        MultipartParser {

    /** {@inheritDoc} */
    @Override
    public MultipartFormData parse(final String charEncoding,
                                   final int contentLength,
                                   final String contentType,
                                   final InputStream inputStream) {
        return new MultipartForm(
            charEncoding, contentLength, contentType, inputStream);
    }
}
