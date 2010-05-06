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
package ccc.dto;

import java.util.HashMap;

import ccc.api.core.User;
import ccc.api.types.URIBuilder;
import ccc.plugins.s11n.Json;


/**
 * Enhanced user class providing server-side features.
 *
 * @author Civic Computing Ltd.
 */
public class UserEnhanced
    extends
        User {

    /** {@inheritDoc} */
    @Override
    // FIXME: Remove strings to constants.
    public void toJson(final Json json) {
        super.toJson(json);

        final HashMap<String, String> links = new HashMap<String, String>();
        if (null!=getId()) {
            links.put(
                "password",
                new URIBuilder(ccc.api.core.ResourceIdentifiers.User.PASSWORD)
                    .replace("id", getId().toString())
                    .toString());
            links.put(
                "self",
                new URIBuilder(ccc.api.core.ResourceIdentifiers.User.ELEMENT)
                    .replace("id", getId().toString())
                    .toString());
        }

        json.set("links", links); // TODO: Preserve parent links?
    }


}
