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

import ccc.api.core.Folder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * Serializer for {@link Folder}s.
 *
 * @author Civic Computing Ltd.
 */
public class FolderSerializer
    extends
        ResourceSerializer<Folder> {


    /** {@inheritDoc} */
    @Override
    public Folder read(final Json json) {
        if (null==json) { return null; }

        final Folder f = super.read(json);

        f.setIndexPage(json.getId(JsonKeys.INDEX_PAGE_ID));
        f.setSortList(json.getStrings(JsonKeys.SORT_LIST));

        return f;
    }


    /** {@inheritDoc} */
    @Override protected Folder createObject() { return new Folder(); }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final Folder instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        json.set(JsonKeys.INDEX_PAGE_ID, instance.getIndexPage());
        json.setStrings(JsonKeys.SORT_LIST, instance.getSortList());

        return json;
    }
}
