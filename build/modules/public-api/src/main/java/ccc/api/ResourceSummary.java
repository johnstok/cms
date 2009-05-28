/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api;

import static ccc.api.JsonKeys.*;

import java.io.Serializable;
import java.util.Date;


/**
 * A summary of a resource.
 * FIXME: remove all mutators.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceSummary implements Serializable, Jsonable {

    private ID _id;
    private ID _parentId;
    private String _name;
    private Username _publishedBy;
    private String _title;
    private Username _lockedBy;
    private ResourceType _type;
    private int    _childCount;
    private int    _folderCount;
    private boolean _includeInMainMenu;
    private String _sortOrder;
    private boolean _hasWorkingCopy;
    private Date _dateCreated;
    private Date _dateChanged;
    private ID _templateId;
    private String _tags;

    @SuppressWarnings("unused") private ResourceSummary() { super(); }

    /**
     * Constructor.
     *
     * @param id The resource's id.
     * @param parentId The resource's parent folder id.
     * @param name The resource's name.
     * @param publishedBy The user that published the resource.
     * @param title The resource's title.
     * @param lockedBy The user that locked the resource.
     * @param type The type of the resource.
     * @param childCount The number of children the resource has.
     * @param folderCount The number of folders the resource contains.
     * @param includeInMainMenu Is the resource included in the main menu.
     * @param sortOrder The sort order for the resource.
     * @param hasWorkingCopy Does the resource have a working copy.
     * @param dateCreated When was the resource created.
     * @param dateChanged When was the resource last changed.
     * @param templateId The id of the resource's template.
     * @param tags The resource's tags.
     */
    public ResourceSummary(final ID id,
                           final ID parentId,
                           final String name,
                           final Username publishedBy,
                           final String title,
                           final Username lockedBy,
                           final ResourceType type,
                           final int childCount,
                           final int folderCount,
                           final boolean includeInMainMenu,
                           final String sortOrder,
                           final boolean hasWorkingCopy,
                           final Date dateCreated,
                           final Date dateChanged,
                           final ID templateId,
                           final String tags) {
        _id = id;
        _parentId = parentId;
        _name = name;
        _publishedBy = publishedBy;
        _title = title;
        _lockedBy = lockedBy;
        _type = type;
        _childCount = childCount;
        _folderCount = folderCount;
        _includeInMainMenu = includeInMainMenu;
        _sortOrder = sortOrder;
        _hasWorkingCopy = hasWorkingCopy;
        _dateCreated = new Date(dateCreated.getTime());
        _dateChanged = new Date(dateChanged.getTime());
        _templateId = templateId;
        _tags = tags;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public ID getId() {
        return _id;
    }


    /**
     * Accessor.
     *
     * @return Returns the parentId.
     */
    public ID getParentId() {
        return _parentId;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Accessor.
     *
     * @return Returns the publishedBy.
     */
    public Username getPublishedBy() {
        return _publishedBy;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * Accessor.
     *
     * @return Returns the lockedBy.
     */
    public Username getLockedBy() {
        return _lockedBy;
    }


    /**
     * Accessor.
     *
     * @return Returns the type.
     */
    public ResourceType getType() {
        return _type;
    }


    /**
     * Accessor.
     *
     * @return Returns the childCount.
     */
    public int getChildCount() {
        return _childCount;
    }


    /**
     * Accessor.
     *
     * @return Returns the folderCount.
     */
    public int getFolderCount() {
        return _folderCount;
    }


    /**
     * Accessor.
     *
     * @return Returns the includeInMainMenu.
     */
    public boolean isIncludeInMainMenu() {
        return _includeInMainMenu;
    }


    /**
     * Accessor.
     *
     * @return Returns the sortOrder.
     */
    public String getSortOrder() {
        return _sortOrder;
    }


    /**
     * Accessor.
     *
     * @return Returns the hasWorkingCopy.
     */
    public boolean isHasWorkingCopy() {
        return _hasWorkingCopy;
    }


    /**
     * Accessor.
     *
     * @return Returns the dateCreated.
     */
    public Date getDateCreated() {
        return new Date(_dateCreated.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the dateChanged.
     */
    public Date getDateChanged() {
        return new Date(_dateChanged.getTime());
    }


    /**
     * Mutator.
     *
     * @param hasWorkingCopy The hasWorkingCopy to set.
     */
    public void setHasWorkingCopy(final boolean hasWorkingCopy) {
        _hasWorkingCopy = hasWorkingCopy;
    }


    /**
     * Mutator.
     *
     * @param sortOrder The sortOrder to set.
     */
    public void setSortOrder(final String sortOrder) {
        _sortOrder = sortOrder;
    }


    /**
     * Mutator.
     *
     * @param title The title to set.
     */
    public void setTitle(final String title) {
        _title = title;
    }


    /**
     * Mutator.
     *
     * @param name The name to set.
     */
    public void setName(final String name) {
        _name = name;
    }


    /**
     * Mutator.
     *
     * @param includeInMainMenu The includeInMainMenu to set.
     */
    public void setIncludeInMainMenu(final boolean includeInMainMenu) {
        _includeInMainMenu = includeInMainMenu;
    }


    /**
     * Accessor.
     *
     * @return Returns the dateChanged.
     */
    public ID getTemplateId() {
        return _templateId;
    }


    /**
     * Accessor.
     *
     * @return Returns the tags.
     */
    public String getTags() {
        return _tags;
    }


    /**
     * Mutator.
     *
     * @param lockedBy The lockedBy to set.
     */
    public void setLockedBy(final Username lockedBy) {
        _lockedBy = lockedBy;
    }


    /**
     * Mutator.
     *
     * @param publishedBy The publishedBy to set.
     */
    public void setPublishedBy(final Username publishedBy) {
        _publishedBy = publishedBy;
    }


    /**
     * Mutator.
     *
     * @param templateId The templateId to set.
     */
    public void setTemplateId(final ID templateId) {
        _templateId = templateId;
    }

    /**
     * Mutator.
     *
     * @param tags The tags to set.
     */
    public void setTags(final String tags) {
        _tags = tags;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(ID, _id);
        json.set(NAME, _name);
        json.set(PARENT_ID, _parentId);
        json.set(TYPE, _type.name());
        json.set(LOCKED_BY, (null==_lockedBy)?null:_lockedBy.toString());
        json.set(TITLE, _title);
        json.set(
            PUBLISHED_BY, (null==_publishedBy)?null:_publishedBy.toString());
        json.set(CHILD_COUNT, _childCount);
        json.set(FOLDER_COUNT, _folderCount);
        json.set(INCLUDE_IN_MAIN_MENU, Boolean.valueOf(_includeInMainMenu));
        json.set(SORT_ORDER, _sortOrder);
        json.set(HAS_WORKING_COPY, Boolean.valueOf(_hasWorkingCopy));
        json.set(DATE_CREATED, _dateCreated);
        json.set(DATE_CHANGED, _dateChanged);
        json.set(TEMPLATE_ID, _templateId);
        json.set(TAGS, _tags);
    }
}
