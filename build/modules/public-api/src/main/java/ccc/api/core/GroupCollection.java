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

import java.util.ArrayList;
import java.util.Collection;

import ccc.api.types.PagedCollection;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GroupCollection
    extends
        PagedCollection<Group> {


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String list() {
        return getLink("self");
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String list(final int pageNo,
                       final int pageSize,
                       final String sort,
                       final String order) {
        final StringBuilder path = new StringBuilder();
        path.append(getLink("self"));
        path.append(
            "?page="+pageNo
            +"&count="+pageSize
            +"&sort="+sort
            +"&order="+order);
        return path.toString();
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);
        _totalCount = json.getLong(JsonKeys.SIZE).longValue();
        final Collection<Json> elementJson =
            json.getCollection(JsonKeys.ELEMENTS);
        final ArrayList<Group> elements =
            new ArrayList<Group>();
        for (final Json element : elementJson) {
            final Group g = new Group();
            g.fromJson(element);
            elements.add(g);
        }
        _elements.addAll(elements);
    }
}
