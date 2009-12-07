/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.binding;


import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.rest.dto.ResourceSummary;
import ccc.serialization.JsonKeys;
import ccc.types.ResourceType;
import ccc.types.Username;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;


/**
 * {@link ModelData} implementation for the {@link ResourceSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSummaryModelData
    implements
        ModelData {
    /** DISPLAY_PROPERTY : String. */
    public static final String DISPLAY_PROPERTY = Property.NAME.name();

    private ResourceSummary _rs;

    /**
     * Constructor.
     *
     * @param rs The resource summary to wrap.
     */
    public ResourceSummaryModelData(final ResourceSummary rs) {
        _rs = rs;
    }

    /**
     * Factory method.
     *
     * @param jsonObject JSON object representing a resource summary.
     * @return A model data object representing the resource summary.
     */
    public static ResourceSummaryModelData create(final JSONValue jsonObject) {

        final JSONObject summaryObject = jsonObject.isObject();

        return new ResourceSummaryModelData(new ResourceSummary(
            UUID.fromString(
                summaryObject.get(JsonKeys.ID).isString().stringValue()),

            UUID.fromString(summaryObject.get(
                JsonKeys.PARENT_ID).isString().stringValue()),

            summaryObject.get(JsonKeys.NAME).isString().stringValue(),

            (null!=summaryObject.get(JsonKeys.PUBLISHED_BY).isNull())
                ? null
                : new Username(summaryObject.get(
                    JsonKeys.PUBLISHED_BY).isString().stringValue()),

            summaryObject.get(JsonKeys.TITLE).isString().stringValue(),

            (null!=summaryObject.get(JsonKeys.LOCKED_BY).isNull())
                ? null
                : new Username(summaryObject.get(
                    JsonKeys.LOCKED_BY).isString().stringValue()),

            ResourceType.valueOf(
                summaryObject.get(JsonKeys.TYPE).isString().stringValue()),

            (int) summaryObject.get(
                JsonKeys.CHILD_COUNT).isNumber().doubleValue(),

            (int) summaryObject.get(
                JsonKeys.FOLDER_COUNT).isNumber().doubleValue(),

            summaryObject.get(
                JsonKeys.INCLUDE_IN_MAIN_MENU).isBoolean().booleanValue(),

            (null!=summaryObject.get(JsonKeys.SORT_ORDER).isNull())
                ? null
                :summaryObject.get(
                    JsonKeys.SORT_ORDER).isString().stringValue(),

            summaryObject.get(
                JsonKeys.HAS_WORKING_COPY).isBoolean().booleanValue(),

            new Date((long) summaryObject.get(
                JsonKeys.DATE_CREATED).isNumber().doubleValue()),

            new Date((long) summaryObject.get(
                JsonKeys.DATE_CHANGED).isNumber().doubleValue()),

            (null!=summaryObject.get(JsonKeys.TEMPLATE_ID).isNull())
                ? null
                : UUID.fromString(summaryObject.get(
                    JsonKeys.TEMPLATE_ID).isString().stringValue()),

            summaryObject.get(JsonKeys.TAGS).isString().stringValue(),

            summaryObject.get(JsonKeys.ABSOLUTE_PATH).isString().stringValue(),

            (null!=summaryObject.get(JsonKeys.INDEX_PAGE_ID).isNull())
            ? null
            :UUID.fromString(summaryObject.get(
                JsonKeys.INDEX_PAGE_ID).isString().stringValue()),

            summaryObject.get(JsonKeys.DESCRIPTION).isString().stringValue(),
            (null!=summaryObject.get(JsonKeys.CREATED_BY).isNull())
            ? null
            : new Username(summaryObject.get(
                JsonKeys.CREATED_BY).isString().stringValue())
        ));
    }


    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case CHILD_COUNT:
                return (X) Integer.valueOf(_rs.getChildCount());

            case DATE_CHANGED:
                return (X) _rs.getDateChanged();

            case DATE_CREATED:
                return (X) _rs.getDateCreated();

            case FOLDER_COUNT:
                return (X) Integer.valueOf(_rs.getFolderCount());

            case UUID:
                return (X) _rs.getId();

            case LOCKED:
                return (X) _rs.getLockedBy();

            case MM_INCLUDE:
                return (X) Boolean.valueOf(_rs.isIncludeInMainMenu());

            case NAME:
                return (X) _rs.getName();

            case PARENT:
                return (X) _rs.getParent();

            case PUBLISHED:
                return (X) _rs.getPublishedBy();

            case SORT_ORDER:
                return (X) _rs.getSortOrder();

            case TITLE:
                return (X) _rs.getTitle();

            case TYPE:
                return (X) _rs.getType();

            case WORKING_COPY:
                return (X) Boolean.valueOf(_rs.isHasWorkingCopy());

            case ABSOLUTE_PATH:
                return (X) _rs.getAbsolutePath();

            case INDEX_PAGE_ID:
                return (X) _rs.getIndexPageId();

            case DESCRIPTION:
                return (X) _rs.getDescription();

            default:
                throw new UnsupportedOperationException(
                    "Key not supported: "+property);
        }
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<String, Object>();
        for (final Property p : Property.values()) {
            properties.put(p.name(), get(p.name()));
        }
        return properties;
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public Collection<String> getPropertyNames() {
        final Set<String> names = new HashSet<String>();
        for (final Property p : Property.values()) {
            names.add(p.name());
        }
        return names;
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X set(final String property, final X value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * Property values form model data.
     *
     * @author Civic Computing Ltd.
     */
    public enum Property {
        /** UUID : Property. */
        UUID,
        /** PARENT : Property. */
        PARENT,
        /** NAME : Property. */
        NAME,
        /** PUBLISHED : Property. */
        PUBLISHED,
        /** TITLE : Property. */
        TITLE,
        /** LOCKED : Property. */
        LOCKED,
        /** TYPE : Property. */
        TYPE,
        /** CHILD_COUNT : Property. */
        CHILD_COUNT,
        /** FOLDER_COUNT : Property. */
        FOLDER_COUNT,
        /** MM_INCLUDE : Property. */
        MM_INCLUDE,
        /** SORT_ORDER : Property. */
        SORT_ORDER,
        /** WORKING_COPY : Property. */
        WORKING_COPY,
        /** DATE_CHANGED : Property. */
        DATE_CHANGED,
        /** DATE_CREATED : Property. */
        DATE_CREATED,
        /** ABSOLUTE_PATH : Property. */
        ABSOLUTE_PATH,
        /** INDEX_PAGE_ID : Property. */
        INDEX_PAGE_ID,
        /** DESCRIPTION : Property. */
        DESCRIPTION;
    }

    /**
     * Merge the specified resource summary into this model data.
     *
     * @param rs The new resource summary.
     */
    public void merge(final ResourceSummary rs) {
        _rs = rs;
    }

    /**
     * Accessor.
     *
     * @return The  UUID of the resource.
     */
    public UUID getId() {
        return _rs.getId();
    }

    /**
     * Mutator.
     *
     * @param b The boolean value to set.
     */
    public void setWorkingCopy(final boolean b) {
        _rs.setHasWorkingCopy(b);
    }

    /**
     * Accessor.
     *
     * @return The name.
     */
    public String getName() {
        return _rs.getName();
    }

    /**
     * Accessor.
     *
     * @return The resource type.
     */
    public ResourceType getType() {
        return _rs.getType();
    }

    /**
     * Accessor.
     *
     * @return The folder count.
     */
    public int getFolderCount() {
        return _rs.getFolderCount();
    }

    /**
     * Accessor.
     *
     * @return The child count.
     */
    public int getChildCount() {
        return _rs.getChildCount();
    }

    /**
     * Accessor.
     *
     * @return The user name of the resource locker.
     */
    public Username getLocked() {
        return _rs.getLockedBy();
    }

    /**
     * Mutator.
     *
     * @param order The sort order to set.
     */
    public void setSortOrder(final String order) {
        _rs.setSortOrder(order);
    }

    /**
     * Mutator.
     *
     * @param title The resource title to set.
     */
    public void setTitle(final String title) {
        _rs.setTitle(title);
    }

    /**
     * Accessor.
     *
     * @return The UUID of the parent.
     */
    public UUID getParent() {
        return _rs.getParent();
    }

    /**
     * Mutator.
     *
     * @param value The name to set.
     */
    public void setName(final String value) {
        _rs.setName(value);
    }

    /**
     * Mutator.
     *
     * @param b The boolean value to set.
     */
    public void setIncludeInMainMenu(final boolean b) {
        _rs.setIncludeInMainMenu(b);
    }

    /**
     * Accessor.
     *
     * @return The title of the resource.
     */
    public String getTitle() {
        return _rs.getTitle();
    }

    /**
     * Accessor.
     *
     * @return The sort order of the resource.
     */
    public String getSortOrder() {
        return _rs.getSortOrder();
    }

    /**
     * Accessor.
     *
     * @return Username of the publisher.
     */
    public Username getPublished() {
        return _rs.getPublishedBy();
    }

    /**
     * Mutator.
     *
     * @return true if has a working copy.
     */
    public boolean hasWorkingCopy() {
        return _rs.isHasWorkingCopy();
    }

    /**
     * Accessor.
     *
     * @return true if included in the main menu.
     */
    public boolean isIncludedInMainMenu() {
        return _rs.isIncludeInMainMenu();
    }

    /**
     * Mutator.
     *
     * @param absolutePath The absolute path to set.
     */
    public void setAbsolutePath(final String absolutePath) {
        _rs.setAbsolutePath(absolutePath);
    }

    /**
     * Accessor.
     *
     * @return The absolute path.
     */
    public String getAbsolutePath() {
        return _rs.getAbsolutePath();
    }

    /**
     * Accessor.
     *
     * @return Template UUID.
     */
    public UUID getTemplateId() {
        return _rs.getTemplateId();
    }

    /**
     * Accessor.
     *
     * @return The tags.
     */
    public String getTags() {
        return _rs.getTags();
    }

    /**
     * Mutator.
     *
     * @param user The user to set.
     */
    public void setLocked(final Username user) {
        _rs.setLockedBy(user);
    }

    /**
     * Mutator.
     *
     * @param user The user to set.
     */
    public void setPublished(final Username user) {
        _rs.setPublishedBy(user);
    }

    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setTemplateId(final UUID id) {
        _rs.setTemplateId(id);
    }

    /**
     * Mutator.
     *
     * @param tags The tags to set.
     */
    public void setTags(final String tags) {
        _rs.setTags(tags);
    }

    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setIndexPageId(final UUID id) {
        _rs.setIndexPageId(id);
    }

    /**
     * Accessor.
     *
     * @return The index page.
     */
    public UUID getIndexPageId() {
        return _rs.getIndexPageId();
    }

    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    public String getDescription() {

        return _rs.getDescription();
    }

    /**
     * Mutator.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        _rs.setDescription(description);
    }

    /**
     * Retrieve the relative path to a resource's revision data.
     *
     * @return The path as a string.
     */
    public String revisionsPath() {
        return "/resources/"+getId()+"/revisions";
    }
}
