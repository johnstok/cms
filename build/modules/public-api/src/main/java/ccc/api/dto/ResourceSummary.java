/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.api.dto;

import static ccc.plugins.s11n.JsonKeys.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ccc.api.types.ResourceType;
import ccc.api.types.Username;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.Jsonable2;


/**
 * A summary of a resource.
 * TODO: remove all mutators?
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceSummary implements Serializable, Jsonable2 {

    private UUID _id;
    private UUID _parent;
    private String _name;
    private Username _publishedBy;
    private Username _createdBy;
    private Username _changedBy;
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
    private UUID _templateId;
    private String _tags;
    private String _absolutePath;
    private UUID _indexPageId;
    private String _description;


    /**
     * Constructor.
     */
    public ResourceSummary() { super(); }


    /**
     * Constructor.
     *
     * @param id The resource's id.
     * @param parent The resource's parent folder id.
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
     * @param absolutePath The resource's absolute path.
     * @param indexPageId The id of the index page.
     * @param description The description of the resource.
     * @param createdBy The user who created the resource.
     * @param changedBy The user who changed the resource.
     */
    public ResourceSummary(final UUID id,
                           final UUID parent,
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
                           final UUID templateId,
                           final String tags,
                           final String absolutePath,
                           final UUID indexPageId,
                           final String description,
                           final Username createdBy,
                           final Username changedBy) {
        _id = id;
        _parent = parent;
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
        _absolutePath = absolutePath;
        _indexPageId = indexPageId;
        _description = description;
        _createdBy = createdBy;
        _changedBy = changedBy;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of a resource summary.
     */
    public ResourceSummary(final Json json) {
        fromJson(json);
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
        return _id;
    }


    /**
     * Accessor.
     *
     * @return Returns the parent.
     */
    public UUID getParent() {
        return _parent;
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
     * @return Returns the createdBy.
     */
    public Username getCreatedBy() {
        return _createdBy;
    }

    /**
     * Accessor.
     *
     * @return Returns the changedBy.
     */
    public Username getChangedBy() {
        return _changedBy;
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
    public UUID getTemplateId() {
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
    public void setTemplateId(final UUID templateId) {
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


    /**
     * Accessor.
     *
     * @return Returns the absolute path.
     */
    public String getAbsolutePath() {
        return _absolutePath;
    }


    /**
     * Mutator.
     *
     * @param absolutePath The absolutePath to set.
     */
    public void setAbsolutePath(final String absolutePath) {
        _absolutePath = absolutePath;
    }


    /**
     * Accessor.
     *
     * @return Returns the indexPageId.
     */
    public UUID getIndexPageId() {
        return _indexPageId;
    }


    /**
     * Mutator.
     *
     * @param indexPageId The indexPageId to set.
     */
    public void setIndexPageId(final UUID indexPageId) {
        _indexPageId = indexPageId;
    }


    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    public String getDescription() {
        return _description;
    }


    /**
     * Mutator.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        _description = description;
    }


    /**
     * Accessor.
     *
     * @return Returns the name with the first letter capitalised.
     */
    public String getCappedName() {
        final String heading = getName();
        final String c = heading.substring(0, 1).toUpperCase();
        final String cappedName = c+heading.substring(1);
        return cappedName;
    }


    /**
     * Mutator.
     *
     * @param count The folder count.
     */
    public void setFolderCount(final int count) {
        _folderCount = count;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(ID, _id);
        json.set(NAME, _name);
        json.set(PARENT_ID, _parent);
        json.set(TYPE, _type.name());
        json.set(LOCKED_BY, (null==_lockedBy)?null:_lockedBy.toString());
        json.set(TITLE, _title);
        json.set(
            PUBLISHED_BY, (null==_publishedBy)?null:_publishedBy.toString());
        json.set(CHILD_COUNT, Long.valueOf(_childCount));
        json.set(FOLDER_COUNT, Long.valueOf(_folderCount));
        json.set(INCLUDE_IN_MAIN_MENU, Boolean.valueOf(_includeInMainMenu));
        json.set(SORT_ORDER, _sortOrder);
        json.set(HAS_WORKING_COPY, Boolean.valueOf(_hasWorkingCopy));
        json.set(DATE_CREATED, _dateCreated);
        json.set(DATE_CHANGED, _dateChanged);
        json.set(TEMPLATE_ID, _templateId);
        json.set(TAGS, _tags);
        json.set(ABSOLUTE_PATH, _absolutePath);
        json.set(INDEX_PAGE_ID, _indexPageId);
        json.set(DESCRIPTION, _description);
        json.set(CREATED_BY, (null==_createdBy)?null:_createdBy.toString());
        json.set(CHANGED_BY, (null==_changedBy)?null:_changedBy.toString());
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        _id = json.getId(ID);
        _parent = json.getId(PARENT_ID);
        _name = json.getString(NAME);
        _publishedBy =
            (null==json.getString(PUBLISHED_BY))
                ? null
                : new Username(json.getString(PUBLISHED_BY));
        _title = json.getString(TITLE);
        _lockedBy =
            (null==json.getString(LOCKED_BY))
                ? null
                : new Username(json.getString(LOCKED_BY));
        _type = ResourceType.valueOf(json.getString(TYPE));
        _childCount = json.getInt(CHILD_COUNT);
        _folderCount = json.getInt(FOLDER_COUNT);
        _includeInMainMenu = json.getBool(INCLUDE_IN_MAIN_MENU);
        _sortOrder = json.getString(SORT_ORDER);
        _hasWorkingCopy = json.getBool(HAS_WORKING_COPY);
        _dateCreated = json.getDate(DATE_CREATED);
        _dateChanged = json.getDate(DATE_CHANGED);
        _templateId = json.getId(TEMPLATE_ID);
        _tags = json.getString(TAGS);
        _absolutePath = json.getString(ABSOLUTE_PATH);
        _indexPageId = json.getId(INDEX_PAGE_ID);
        _description = json.getString(DESCRIPTION);
        _createdBy =
            (null==json.getString(CREATED_BY))
                ? null
                : new Username(json.getString(CREATED_BY));
        _changedBy =
            (null==json.getString(CHANGED_BY))
            ? null
                : new Username(json.getString(CHANGED_BY));
    }
}
