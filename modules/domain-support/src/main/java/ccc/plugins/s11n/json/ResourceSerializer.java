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

import java.util.Collection;
import java.util.HashSet;

import ccc.api.core.Resource;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * Serializer for {@link Resource}s.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ResourceSerializer<T extends Resource>
    extends
        ResSerializer<T> {


    /** {@inheritDoc} */
    @Override
    public T read(final Json json) {
        if (null==json) { return null; }

        final T r = super.read(json);

        r.setAbsolutePath(json.getString(JsonKeys.ABSOLUTE_PATH));
        final Json duration = json.getJson(JsonKeys.CACHE_DURATION);
        r.setCacheDuration(
            (null==duration) ? null : new DurationSerializer().read(duration));
        r.setDateChanged(json.getDate(JsonKeys.DATE_CHANGED));
        r.setDateCreated(json.getDate(JsonKeys.DATE_CREATED));
        r.setDescription(json.getString(JsonKeys.DESCRIPTION));
        r.setId(json.getId(JsonKeys.ID));
        r.setInMainMenu(
            json.getBool(JsonKeys.INCLUDE_IN_MAIN_MENU).booleanValue());
        r.setLocked(json.getBool(JsonKeys.LOCKED).booleanValue());
        r.setPublished(json.getBool(JsonKeys.PUBLISHED).booleanValue());
        r.setSecure(json.getBool(JsonKeys.SECURE).booleanValue());
        r.setVisible(json.getBool(JsonKeys.VISIBLE).booleanValue());
        r.setLockedBy(json.getId(JsonKeys.LOCKED_BY));
        r.setMetadata(json.getStringMap(JsonKeys.METADATA));
        final String name = json.getString(JsonKeys.NAME);
        r.setName((null==name) ? null : new ResourceName(name));
        r.setParent(json.getId(JsonKeys.PARENT_ID));
        r.setPublishedBy(json.getId(JsonKeys.PUBLISHED_BY));
        r.setRevision(json.getInt(JsonKeys.REVISION).intValue());
        final Collection<String> tags = json.getStrings(JsonKeys.TAGS);
        r.setTags((null==tags) ? null : new HashSet<String>(tags));
        r.setTemplate(json.getId(JsonKeys.TEMPLATE_ID));
        r.setTitle(json.getString(JsonKeys.TITLE));
        final String type = json.getString(JsonKeys.TYPE);
        r.setType((null==type) ? null : ResourceType.valueOf(type));

        return r;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final T instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        json.set(JsonKeys.ABSOLUTE_PATH, instance.getAbsolutePath());
        json.set(
            JsonKeys.CACHE_DURATION,
            new DurationSerializer().write(
                json.create(), instance.getCacheDuration()));
        json.set(JsonKeys.DATE_CHANGED, instance.getDateChanged());
        json.set(JsonKeys.DATE_CREATED, instance.getDateCreated());
        json.set(JsonKeys.DESCRIPTION, instance.getDescription());
        json.set(JsonKeys.ID, instance.getId());
        json.set(
            JsonKeys.INCLUDE_IN_MAIN_MENU,
            Boolean.valueOf(instance.isInMainMenu()));
        json.set(JsonKeys.LOCKED, Boolean.valueOf(instance.isLocked()));
        json.set(JsonKeys.PUBLISHED, Boolean.valueOf(instance.isPublished()));
        json.set(JsonKeys.SECURE, Boolean.valueOf(instance.isSecure()));
        json.set(JsonKeys.VISIBLE, Boolean.valueOf(instance.isVisible()));
        json.set(JsonKeys.LOCKED_BY, instance.getLockedBy());
        json.set(JsonKeys.METADATA, instance.getMetadata());
        json.set(
            JsonKeys.NAME,
            (null==instance.getName()) ? null : instance.getName().toString());
        json.set(JsonKeys.PARENT_ID, instance.getParent());
        json.set(JsonKeys.PUBLISHED_BY, instance.getPublishedBy());
        json.set(JsonKeys.REVISION, Long.valueOf(instance.getRevision()));
        json.setStrings(JsonKeys.TAGS, instance.getTags());
        json.set(JsonKeys.TEMPLATE_ID, instance.getTemplate());
        json.set(JsonKeys.TITLE, instance.getTitle());
        json.set(
            JsonKeys.TYPE,
            (null==instance.getType()) ? null : instance.getType().name());

        return json;
    }
}
