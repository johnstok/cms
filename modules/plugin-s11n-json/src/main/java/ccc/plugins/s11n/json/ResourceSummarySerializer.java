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

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.api.types.Username;


/**
 * Serializer for {@link ResourceSummary}s.
 *
 * @author Civic Computing Ltd.
 */
class ResourceSummarySerializer extends BaseSerializer<ResourceSummary> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    ResourceSummarySerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final ResourceSummary s = new ResourceSummary();

        ResourceMappings.readRes(json, s);
        readResSummary(json, s);

        return s;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final ResourceSummary instance) {
        if (null==instance) { return null; }
        final Json json = newJson();

        ResourceMappings.writeRes(json, instance);
        writeResSummary(instance, json);

        return json.toString();
    }


    static void readResSummary(final Json json, final ResourceSummary s) {

        s.setId(json.getId(JsonKeys.ID));
        s.setParent(json.getId(JsonKeys.PARENT_ID));
        final String name = json.getString(JsonKeys.NAME);
        s.setName((null==name) ? null : new ResourceName(name));
        s.setPublishedBy(
            (null==json.getString(JsonKeys.PUBLISHED_BY))
            ? null
                : new Username(json.getString(JsonKeys.PUBLISHED_BY)));
        s.setTitle(json.getString(JsonKeys.TITLE));
        s.setLockedBy(
            (null==json.getString(JsonKeys.LOCKED_BY))
            ? null
                : new Username(json.getString(JsonKeys.LOCKED_BY)));
        final String type = json.getString(JsonKeys.TYPE);
        s.setType((null==type) ? null : ResourceType.valueOf(type));
        s.setChildCount(json.getInt(JsonKeys.CHILD_COUNT));
        s.setFolderCount(json.getInt(JsonKeys.FOLDER_COUNT));
        s.setIncludeInMainMenu(json.getBool(JsonKeys.INCLUDE_IN_MAIN_MENU));
        s.setHasWorkingCopy(json.getBool(JsonKeys.HAS_WORKING_COPY));
        s.setDateCreated(json.getDate(JsonKeys.DATE_CREATED));
        s.setDateChanged(json.getDate(JsonKeys.DATE_CHANGED));
        s.setTemplateId(json.getId(JsonKeys.TEMPLATE_ID));
        final Collection<String> tags = json.getStrings(JsonKeys.TAGS);
        s.setTags((null==tags) ? null : new HashSet<String>(tags));
        s.setAbsolutePath(json.getString(JsonKeys.ABSOLUTE_PATH));
        s.setIndexPageId(json.getId(JsonKeys.INDEX_PAGE_ID));
        s.setDescription(json.getString(JsonKeys.DESCRIPTION));
        s.setCreatedBy(
            (null==json.getString(JsonKeys.CREATED_BY))
            ? null
                : new Username(json.getString(JsonKeys.CREATED_BY)));
        s.setChangedBy(
            (null==json.getString(JsonKeys.CHANGED_BY))
            ? null
                : new Username(json.getString(JsonKeys.CHANGED_BY)));
        s.setVisible(json.getBool(JsonKeys.VISIBLE).booleanValue());
    }


    static void writeResSummary(final ResourceSummary instance,
                                 final Json json) {

        json.set(JsonKeys.ID, instance.getId());
        json.set(
            JsonKeys.NAME,
            (null==instance.getName()) ? null : instance.getName().toString());
        json.set(JsonKeys.PARENT_ID, instance.getParent());
        json.set(
            JsonKeys.TYPE,
            (null==instance.getType()) ? null : instance.getType().name());
        json.set(
            JsonKeys.LOCKED_BY,
            (null==instance.getLockedBy())
                ? null
                : instance.getLockedBy().toString());
        json.set(JsonKeys.TITLE, instance.getTitle());
        json.set(
            JsonKeys.PUBLISHED_BY,
            (null==instance.getPublishedBy())
                ? null
                : instance.getPublishedBy().toString());
        json.set(
            JsonKeys.CHILD_COUNT,
            Long.valueOf(instance.getChildCount()));
        json.set(
            JsonKeys.FOLDER_COUNT,
            Long.valueOf(instance.getFolderCount()));
        json.set(
            JsonKeys.INCLUDE_IN_MAIN_MENU,
            Boolean.valueOf(instance.isIncludeInMainMenu()));
        json.set(JsonKeys.HAS_WORKING_COPY,
            Boolean.valueOf(instance.isHasWorkingCopy()));
        json.set(JsonKeys.DATE_CREATED, instance.getDateCreated());
        json.set(JsonKeys.DATE_CHANGED, instance.getDateChanged());
        json.set(JsonKeys.TEMPLATE_ID, instance.getTemplateId());
        json.setStrings(JsonKeys.TAGS, instance.getTags());
        json.set(JsonKeys.ABSOLUTE_PATH, instance.getAbsolutePath());
        json.set(JsonKeys.INDEX_PAGE_ID, instance.getIndexPageId());
        json.set(JsonKeys.DESCRIPTION, instance.getDescription());
        json.set(
            JsonKeys.CREATED_BY,
            (null==instance.getCreatedBy())
                ? null
                : instance.getCreatedBy().toString());
        json.set(
            JsonKeys.CHANGED_BY,
            (null==instance.getChangedBy())
                ? null
                : instance.getChangedBy().toString());
        json.set(JsonKeys.VISIBLE, Boolean.valueOf(instance.isVisible()));
    }

}
