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

import ccc.api.core.ResourceCriteria;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;


/**
 * Serializer for {@link ResourceCriteria} objects.
 *
 * @param <T> The type of criteria object to serialize.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ResourceCriteriaSerializer<T extends ResourceCriteria>
    extends
        AbstractSerializer<T> {


    /** {@inheritDoc} */
    @Override
    public T read(final Json json) {
        if (null==json) { return null; }

        final T c = super.read(json);

        c.setChangedAfter(json.getDate("changed-after"));
        c.setChangedBefore(json.getDate("changed-before"));
        c.setLocked(json.getBool(JsonKeys.LOCKED));
        c.setMainmenu(json.getBool(JsonKeys.INCLUDE_IN_MAIN_MENU));
        c.setMetadata(json.getStringMap(JsonKeys.METADATA));
        c.setParent(json.getId(JsonKeys.PARENT_ID));
        c.setPublished(json.getBool(JsonKeys.PUBLISHED));
        c.setSortField(json.getString("sort-field"));
        final String so = json.getString("sort-order");
        c.setSortOrder((null==so) ? null : SortOrder.valueOf(so));
        c.setTag(json.getString(JsonKeys.TAGS));
        final String t = json.getString(JsonKeys.TYPE);
        c.setType((null==t) ? null : ResourceType.valueOf(t));

        return c;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final T instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        json.set("changed-after", instance.getChangedAfter());
        json.set("changed-before", instance.getChangedBefore());
        json.set(JsonKeys.LOCKED, instance.getLocked());
        json.set(JsonKeys.INCLUDE_IN_MAIN_MENU, instance.getMainmenu());
        json.set(JsonKeys.METADATA, instance.getMetadata());
        json.set(JsonKeys.PARENT_ID, instance.getParent());
        json.set(JsonKeys.PUBLISHED, instance.getPublished());
        json.set("sort-field", instance.getSortField());
        final SortOrder so = instance.getSortOrder();
        json.set("sort-order", (null==so) ? null : so.name());
        json.set(JsonKeys.TAGS, instance.getTag());
        final ResourceType t = instance.getType();
        json.set(JsonKeys.TYPE, (null==t) ? null : t.name());

        return json;
    }
}
