/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.api.ID;
import ccc.api.JsonKeys;
import ccc.api.ResourceSummary;
import ccc.api.ResourceType;
import ccc.api.Username;

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
            new ID(summaryObject.get(JsonKeys.ID).isString().stringValue()),

            new ID(summaryObject.get(
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
                : new ID(summaryObject.get(
                    JsonKeys.TEMPLATE_ID).isString().stringValue()),

            summaryObject.get(JsonKeys.TAGS).isString().stringValue(),

            summaryObject.get(JsonKeys.ABSOLUTE_PATH).isString().stringValue(),

            (null!=summaryObject.get(JsonKeys.INDEX_PAGE_ID).isNull())
            ? null
            : new ID(summaryObject.get(
                JsonKeys.INDEX_PAGE_ID).isString().stringValue()),

            summaryObject.get(JsonKeys.DESCRIPTION).isString().stringValue()
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

            case ID:
                return (X) _rs.getId();

            case LOCKED:
                return (X) _rs.getLockedBy();

            case MM_INCLUDE:
                return (X) Boolean.valueOf(_rs.isIncludeInMainMenu());

            case NAME:
                return (X) _rs.getName();

            case PARENT_ID:
                return (X) _rs.getParentId();

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
        /** ID : Property. */
        ID,
        /** PARENT_ID : Property. */
        PARENT_ID,
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
     * @return The  ID of the resource.
     */
    public ID getId() {
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
     * @return The ID of the parent.
     */
    public ID getParent() {
        return _rs.getParentId();
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
     * @return Template ID.
     */
    public ID getTemplateId() {
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
    public void setTemplateId(final ID id) {
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
    public void setIndexPageId(final ID id) {
        _rs.setIndexPageId(id);
    }

    /**
     * Accessor.
     *
     * @return The index page.
     */
    public ID getIndexPageId() {
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
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String revisionsPath() {
        return "/resources/"+getId()+"/revisions";
    }
}
