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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.services.api.ID;
import ccc.services.api.ResourceSummary;
import ccc.services.api.ResourceType;
import ccc.services.api.Username;

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
    public static final String DISPLAY_PROPERTY = Property.NAME.name();

    private ResourceSummary _rs;
    private String          _absolutePath; // FIXME: Move to ResourceSummary?

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
            new ID(summaryObject.get("id").isString().stringValue()),
            new ID(summaryObject.get("parentId").isString().stringValue()),
            summaryObject.get("name").isString().stringValue(),
            (null==summaryObject.get("publishedBy"))
                ? null
                : new Username(summaryObject.get("publishedBy").isString().stringValue()),
            summaryObject.get("title").isString().stringValue(),
            (null==summaryObject.get("lockedBy"))
                ? null
                : new Username(summaryObject.get("lockedBy").isString().stringValue()),
            ResourceType.valueOf(
                summaryObject.get("type").isString().stringValue()),
            (int) summaryObject.get("childCount").isNumber().doubleValue(),
            (int) summaryObject.get("folderCount").isNumber().doubleValue(),
            summaryObject.get("includeInMainMenu").isBoolean().booleanValue(),
            (null==summaryObject.get("sortOrder"))
                ? null
                :summaryObject.get("lockedBy").isString().stringValue(),
            false, // FIXME: Add to JSON!
            null, // FIXME: Add to JSON!
            null, // FIXME: Add to JSON!
            null, // FIXME: Add to JSON!
            null // FIXME: Add to JSON!
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
                return (X) _absolutePath;

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

    public enum Property {
        ID,
        PARENT_ID,
        NAME,
        PUBLISHED,
        TITLE,
        LOCKED,
        TYPE,
        CHILD_COUNT,
        FOLDER_COUNT,
        MM_INCLUDE,
        SORT_ORDER,
        WORKING_COPY,
        DATE_CHANGED,
        DATE_CREATED,
        ABSOLUTE_PATH;
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
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ID getId() {
        return _rs.getId();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param b
     */
    public void setWorkingCopy(final boolean b) {
        _rs.setHasWorkingCopy(b);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getName() {
        return _rs.getName();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ResourceType getType() {
        return _rs.getType();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public int getFolderCount() {
        return _rs.getFolderCount();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public int getChildCount() {
        return _rs.getChildCount();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Username getLocked() {
        return _rs.getLockedBy();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param order
     */
    public void setSortOrder(final String order) {
        _rs.setSortOrder(order);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param title
     */
    public void setTitle(final String title) {
        _rs.setTitle(title);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ID getParent() {
        return _rs.getParentId();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param value
     */
    public void setName(final String value) {
        _rs.setName(value);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param b
     */
    public void setIncludeInMainMenu(final boolean b) {
        _rs.setIncludeInMainMenu(b);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getTitle() {
        return _rs.getTitle();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getSortOrder() {
        return _rs.getSortOrder();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public Username getPublished() {
        return _rs.getPublishedBy();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public boolean hasWorkingCopy() {
        return _rs.isHasWorkingCopy();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public boolean isIncludedInMainMenu() {
        return _rs.isIncludeInMainMenu();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param path
     */
    public void setAbsolutePath(final String path) {
        _absolutePath = path;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getAbsolutePath() {
        return _absolutePath;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public ID getTemplateId() {
        return _rs.getTemplateId();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getTags() {
        return _rs.getTags();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param currentUser
     */
    public void setLocked(final Username user) {
        _rs.setLockedBy(user);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param currentUser
     */
    public void setPublished(final Username user) {
        _rs.setPublishedBy(user);
    }
}
