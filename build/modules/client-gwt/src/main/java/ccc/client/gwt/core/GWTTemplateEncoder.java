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
 * Revision      $Rev: 2761 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-05-06 18:03:31 +0100 (Thu, 06 May 2010) $
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.core;

import ccc.api.types.Link.Encoder;

import com.google.gwt.http.client.URL;

/**
 * Encodes strings with GWT's URL class.
 *
 * @author Civic Computing Ltd.
 */
public final class GWTTemplateEncoder implements Encoder {

    /** {@inheritDoc} */
    @Override public String encode(final String string) {
        return URL.encodeComponent(string);
    }
}
