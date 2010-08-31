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

import static ccc.plugins.s11n.json.JsonKeys.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccc.api.core.Alias;
import ccc.api.core.File;
import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.PageCriteria;
import ccc.api.core.Res;
import ccc.api.core.Resource;
import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;
import ccc.api.types.Range;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceMappings {

    public static void writeResource(final Json json, final Resource instance) {
        final Json durationJson =
            DurationSerializer.writeDuration(
                instance.getCacheDuration(), json.create());

        json.set(ResourceSerializer.WORKING_COPY, instance.isWcAvailable());
        json.set(JsonKeys.ABSOLUTE_PATH, instance.getAbsolutePath());
        json.set(JsonKeys.CACHE_DURATION, durationJson);
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
        json.set(JsonKeys.TEMPLATE_ID+"2", instance.getTemplate2());
        json.set(JsonKeys.TITLE, instance.getTitle());
        json.set(
            JsonKeys.TYPE,
            (null==instance.getType()) ? null : instance.getType().name());
    }

    public static void readResource(final Json json, final Resource r) {
        r.setWcAvailable(
            json.getBool(ResourceSerializer.WORKING_COPY).booleanValue());
        r.setAbsolutePath(json.getString(JsonKeys.ABSOLUTE_PATH));
        r.setCacheDuration(
            DurationSerializer.read(json.getJson(JsonKeys.CACHE_DURATION)));
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
        r.setTemplate2(json.getId(JsonKeys.TEMPLATE_ID+"2"));
        r.setTitle(json.getString(JsonKeys.TITLE));
        final String type = json.getString(JsonKeys.TYPE);
        r.setType((null==type) ? null : ResourceType.valueOf(type));
    }

    public static void writeTemplate(final Json json, final Template instance) {
        final Json mimeJson = json.create();
        MimeTypeSerializer.writeMimeType(mimeJson, instance.getMimeType());

        json.set(DEFINITION, instance.getDefinition());
        json.set(BODY,       instance.getBody());
        json.set(MIME_TYPE, mimeJson);
    }

    public static void readTemplate(final Json json, final Template t) {
        t.setDefinition(json.getString(DEFINITION));
        t.setBody(json.getString(BODY));
        t.setMimeType(MimeTypeSerializer.readMimeType(json.getJson(MIME_TYPE)));
    }

    public static void writeFile(final Json json, final File instance) {
        final Json mimeJson = json.create();
        MimeTypeSerializer.writeMimeType(mimeJson, instance.getMimeType());

        json.set(MIME_TYPE,mimeJson);
        json.set(PATH, instance.getPath());
        json.set(PROPERTIES, instance.getProperties());
        json.set(SIZE, Long.valueOf(instance.getSize()));
        json.set(DATA, instance.getData());
        json.set(MAJOR_CHANGE, Boolean.valueOf(instance.isMajorEdit()));
        json.set(COMMENT, instance.getComment());
        json.set(TEXT, instance.getContent());
    }

    public static void readFile(final Json json, final File f) {
        f.setMimeType(MimeTypeSerializer.readMimeType(json.getJson(MIME_TYPE)));
        f.setPath(json.getString(PATH));
        f.setProperties(json.getStringMap(PROPERTIES));
        f.setSize(json.getLong(JsonKeys.SIZE).longValue());
        f.setData(json.getId(JsonKeys.DATA));
        f.setMajorEdit(json.getBool(MAJOR_CHANGE).booleanValue());
        f.setComment(json.getString(COMMENT));
        f.setContent(json.getString(TEXT));
    }

    public static void writeAlias(final Json json, final Alias instance) {
        json.set(JsonKeys.TARGET_ID, instance.getTargetId());
    }

    public static void readAlias(final Json json, final Alias a) {
        a.setTargetId(json.getId(JsonKeys.TARGET_ID));
    }

    public static void writeFolder(final Json json, final Folder instance) {
        json.set(JsonKeys.INDEX_PAGE_ID, instance.getIndexPage());
        json.setStrings(JsonKeys.SORT_LIST, instance.getSortList());
    }

    public static void readFolder(final Json json, final Folder f) {
        f.setIndexPage(json.getId(JsonKeys.INDEX_PAGE_ID));
        f.setSortList(json.getStrings(JsonKeys.SORT_LIST));
    }

    public static void writePage(final Json json, final Page instance) {
        json.setJsons(
            JsonKeys.PARAGRAPHS,
            ParagraphSerializer
                .writeParagraphs(json, instance.getParagraphs()));
        json.set(
            JsonKeys.COMMENT,
            instance.getComment());
        json.set(
            JsonKeys.MAJOR_CHANGE,
            Boolean.valueOf(instance.isMajorChange()));
    }

    public static void readPage(final Json json, final Page p) {
        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        for (final Json jsonPara : json.getCollection(JsonKeys.PARAGRAPHS)) {
            paragraphs.add(ParagraphSerializer.read(jsonPara));
        }
        p.setParagraphs(paragraphs);
        p.setComment(json.getString(JsonKeys.COMMENT));
        p.setMajorChange(json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue());
    }

    public static void writeRes(final Json json, final Res instance) {
        json.set("links", instance.getLinks());
    }

    public static void readRes(final Json json, final Res r) {
        final Map<String, String> links = json.getStringMap("links");
        if (null!=links) { r.addLinks(links); }
    }

    public static void readPageCriteria(final Json json, final PageCriteria p) {
        final Map<String, Range<?>> ranges = new HashMap<String, Range<?>>();
        final Collection<Json> jsonRanges = json.getCollection("para-ranges");
        for (final Json jsonRange : jsonRanges) {
            final String name = jsonRange.getString("name");
            final String className = jsonRange.getString("class");
            if (Date.class.getName().equals(className)) {
                ranges.put(
                    name,
                    new Range<Date>(
                        Date.class,
                        jsonRange.getDate("start"),
                        jsonRange.getDate("end")));
            } else if (String.class.getName().equals(className)) {
                ranges.put(
                    name,
                    new Range<String>(
                        String.class,
                        jsonRange.getString("start"),
                        jsonRange.getString("end")));
//            } else if (BigDecimal.class.getName().equals(className)) {
//                ranges.put(
//                    name,
//                    new Range<BigDecimal>(
//                        BigDecimal.class,
//                        jsonRange.getBigDecimal("start"),
//                        jsonRange.getBigDecimal("end")));
            } else {
                throw new RuntimeException(
                    "Unsupported range type: "+className);
            }
        }
        p.setParaRanges(ranges);

        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        for (final Json jsonPara : json.getCollection(JsonKeys.PARAGRAPHS)) {
            paragraphs.add(ParagraphSerializer.read(jsonPara));
        }
        p.setParaMatches(paragraphs);

        p.setParaSortField(json.getString("para-sort-field"));

        final String pSortOrder = json.getString("para-sort-order");
        p.setParaSortOrder(
            (null==pSortOrder) ? null : SortOrder.valueOf(pSortOrder));

        final String pSortType = json.getString("para-sort-type");
        p.setParaSortType(
            (null==pSortType) ? null : ParagraphType.valueOf(pSortType));
    }

    public static void writePageCriteria(final Json json,
                                         final PageCriteria instance) {
        final List<Json> ranges = new ArrayList<Json>();

        for (final Map.Entry<String, Range<?>> i
                                        : instance.getParaRanges().entrySet()) {
            final Json range = json.create();
            if (Date.class.equals(i.getValue().getType())) {
                final Range<Date> j = (Range<Date>) i.getValue();
                range.set("start", j.getStart());
                range.set("end", j.getEnd());
            } else if (String.class.equals(i.getValue().getType())) {
                final Range<String> j = (Range<String>) i.getValue();
                range.set("start", j.getStart());
                range.set("end", j.getEnd());
//            } else if (BigDecimal.class.equals(i.getValue().getType())) {
//                final Range<BigDecimal> j = (Range<BigDecimal>) i.getValue();
//                range.set("start", j.getStart());
//                range.set("end", j.getEnd());
            } else {
                throw new RuntimeException(
                    "Unsupported range type: "+i.getValue().getType());
            }
            range.set("class", i.getValue().getType().getName());
            range.set("name", i.getKey());
            ranges.add(range);
        }
        json.setJsons("para-ranges", ranges);

        json.setJsons(
            JsonKeys.PARAGRAPHS,
            ParagraphSerializer.writeParagraphs(json, instance.getParaMatches()));

        json.set("para-sort-field", instance.getParaSortField());

        final SortOrder o = instance.getParaSortOrder();
        json.set("para-sort-order", (null==o) ? null : o.name());

        final ParagraphType t = instance.getParaSortType();
        json.set("para-sort-type", (null==t) ? null : t.name());
    }
}
