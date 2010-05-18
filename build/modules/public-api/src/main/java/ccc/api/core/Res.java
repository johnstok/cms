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
     * TODO: Add a description for this method.
     *
     * @param key
     * @return
     */
    public String getLink(final String key) {
        return _links.get(key);
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param key
     * @param value
     */
    public void addLink(final String key, final String value) {
        _links.put(key, value);
    }


    public Map<String, String> getLinks() {
        return new HashMap<String, String>(_links);
    }



    /**
     * TODO: Add a description for this method.
     *
     * @param links
     */
    public void addLinks(final Map<String, String> links) {
        _links.putAll(links);
    }
}
