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
package ccc.api.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Base API class supporting s11n and linking.
 *
 * @author Civic Computing Ltd.
 */
public class Res
    implements
        Serializable {

    private final Map<String, String> _links = new HashMap<String, String>();


    /**
     * Get the link for a rel name.
     *
     * @param rel The rel name for the link.
     *
     * @return The corresponding link or NULL if no such link exists.
     */
    public String getLink(final String rel) {
        return _links.get(rel);
    }


    /**
     * Set the link for a rel name.
     *
     * @param rel The rel name for the link.
     * @param link The link to set.
     */
    public void addLink(final String rel, final String link) {
        _links.put(rel, link);
    }


    /**
     * Get a map of available links.
     *
     * @return All rel links for this object.
     */
    public Map<String, String> getLinks() {
        return new HashMap<String, String>(_links);
    }



    /**
     * Add multiple rel links.
     *
     * @param links The links to add.
     */
    public void addLinks(final Map<String, String> links) {
        _links.putAll(links);
    }
}
