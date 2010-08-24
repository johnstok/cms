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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ccc.api.core.ACL;
import ccc.api.core.ACL.Entry;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link ACL}s.
 *
 * @author Civic Computing Ltd.
 */
public class ACLSerializer implements Serializer<ACL> {


    /** {@inheritDoc} */
    @Override
    public ACL read(final Json json) {
        if (null==json) { return null; }

        final ACL d = new ACL();
        d.setGroups(unmap(json.getCollection(JsonKeys.GROUPS)));
        d.setUsers(unmap(json.getCollection(JsonKeys.USERS)));

        return d;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final ACL instance) {
        if (null==instance) { return null; }

        json.setJsons(JsonKeys.GROUPS, map(instance.getGroups(), json));
        json.setJsons(JsonKeys.USERS, map(instance.getUsers(), json));

        return json;
    }


    private Collection<Json> map(final Set<Entry> entries, final Json json) {
        final Collection<Json> jsonEntries = new ArrayList<Json>();
        for (final Entry entry : entries) {
            final Json jsonEntry = json.create();
            jsonEntry.set(JsonKeys.PRINCIPAL, entry.getPrincipal());
            jsonEntry.set(JsonKeys.NAME, entry.getName());
            jsonEntry.set("can_read", entry.isReadable());
            jsonEntry.set("can_write", entry.isWriteable());
            jsonEntries.add(jsonEntry);
        }
        return jsonEntries;
    }


    /**
     * Un-map from a JSON collection to an Entry collection.
     *
     * @param s The JSON collection.
     *
     * @return The corresponding ACL entry collection.
     */
    private Collection<Entry> unmap(final Collection<Json> s) {
        final Set<Entry> entries = new HashSet<Entry>();
        for (final Json json : s) {
            final Entry e = new Entry();
            e.setPrincipal(json.getId(JsonKeys.PRINCIPAL));
            e.setName(json.getString(JsonKeys.NAME));
            e.setReadable(json.getBool("can_read").booleanValue());
            e.setWriteable(json.getBool("can_write").booleanValue());
            entries.add(e);
        }
        return entries;
    }
}
