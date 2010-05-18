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
package ccc.api.temp;

import static ccc.plugins.s11n.JsonKeys.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.User;
import ccc.api.types.Username;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link User}s.
 *
 * @author Civic Computing Ltd.
 */
public class UserSerializer implements Serializer<User> {


    /** {@inheritDoc} */
    @Override
    public User read(final Json json) {
        if (null==json) { return null; }

        final User u = new User();

        final Map<String, String> links = json.getStringMap("links");
        if (null!=links) { u.addLinks(links); }
        u.setId(json.getId(ID));
        u.setEmail(json.getString(EMAIL));
        u.setName(json.getString(NAME));
        u.setPassword(json.getString(JsonKeys.PASSWORD));
        u.setPermissions(json.getStrings(PERMISSIONS));

        final String un = json.getString(USERNAME);
        u.setUsername((null==un) ? null : new Username(un));

        final Collection<String> r = json.getStrings(ROLES);
        u.setGroups(
            (null==r)
                ? new HashSet<UUID>()
                : new HashSet<UUID>(mapUuid(r)));

        final Map<String, String> md = json.getStringMap(JsonKeys.METADATA);
        u.setMetadata(
            (null==md)
                ? new HashMap<String, String>()
                : new HashMap<String, String>(md));

        return u;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final User instance) {
        if (null==instance) { return null; }

        json.set("links", instance.getLinks());
        json.set(ID, instance.getId());
        json.set(EMAIL, instance.getEmail());
        json.set(NAME, instance.getName());
        json.set(
            USERNAME,
            (null==instance.getUsername())
                ? null
                : instance.getUsername().toString());
        json.setStrings(ROLES, mapString(instance.getGroups()));
        json.set(JsonKeys.METADATA, instance.getMetadata());
        json.set(JsonKeys.PASSWORD, instance.getPassword());
        json.setStrings(PERMISSIONS, instance.getPermissions());

        return json;
    }



    private Set<String> mapString(final Set<UUID> uuids) {
        final Set<String> strings = new HashSet<String>();
        for (final UUID uuid : uuids) {
            strings.add(uuid.toString());
        }
        return strings;
    }


    private Collection<? extends UUID> mapUuid(final Collection<String> s) {
        final List<UUID> uuids = new ArrayList<UUID>();
        for (final String string : s) {
            uuids.add(UUID.fromString(string));
        }
        return uuids;
    }
}
