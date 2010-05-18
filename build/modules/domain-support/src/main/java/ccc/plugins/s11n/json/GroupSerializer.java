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
package ccc.plugins.s11n.json;

import java.util.HashSet;
import java.util.Map;

import ccc.api.core.Group;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link Group}s.
 *
 * @author Civic Computing Ltd.
 */
public class GroupSerializer implements Serializer<Group> {


    /** {@inheritDoc} */
    @Override
    public Group read(final Json json) {
        if (null==json) { return null; }

        final Group g = new Group();

        final Map<String, String> links = json.getStringMap("links");
        if (null!=links) { g.addLinks(links); }
        g.setId(json.getId(JsonKeys.ID));
        g.setName(
            json.getString(JsonKeys.NAME));
        g.setPermissions(
            new HashSet<String>(json.getStrings(JsonKeys.PERMISSIONS)));

        return g;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final Group instance) {
        if (null==instance) { return null; }

        json.set("links", instance.getLinks());
        json.set(JsonKeys.ID, instance.getId());
        json.set(JsonKeys.NAME, instance.getName());
        json.setStrings(JsonKeys.PERMISSIONS, instance.getPermissions());

        return json;
    }
}
