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
package ccc.api.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.api.types.ResourceType;
import ccc.api.types.Username;


/**
 * A summary of a resource.
 * TODO: remove all mutators?
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceSummary extends Res {

    private UUID         _id;
    private UUID         _parent;
    private String       _name;
    private Username     _publishedBy;
    private String       _title;
    private Username     _lockedBy;
    private ResourceType _type;
    private int          _childCount;
    private int          _folderCount;
    private boolean      _includeInMainMenu;
    private boolean      _hasWorkingCopy;
    private Date         _dateCreated;
    private Date         _dateChanged;
    private boolean      _isVisible;

    private Username     _createdBy;
    private Username     _changedBy;
    private UUID         _templateId;
    private Set<String>  _tags;
    private String       _absolutePath;
    private UUID         _indexPageId;
    private String       _description;

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
                           final boolean hasWorkingCopy,
                           final Date dateCreated,
                           final Date dateChanged,
                           final UUID templateId,
                           final Set<String> tags,
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
     * Retrieve the relative path to a resource's revision data.
     *
     * @return The path as a string.
     */
    public String revisionsPath() {
        return revisions().toString();
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
    public Set<String> getTags() {
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
    public void setTags(final Set<String> tags) {
        _tags = tags;
    }


    /**
     * Mutator.
     *
     * @param tags The tags to set.
     */
    @Deprecated
    public void setTags(final String tags) {
        setTags(parseTagString(tags));
    }


    private Set<String> parseTagString(final String tags) {
        DBC.require().notNull(tags);
        DBC.require().containsNoBrackets(tags);

        final String[] tagArray = tags.split(",");
        final Set<String> parsed = new HashSet<String>();
        for(final String tag : tagArray) {
            if (tag.trim().length() < 1) {
                continue;
            }
            parsed.add(tag.trim());
        }
        return parsed;
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
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final UUID id) {
        _id = id;
    }


    /**
     * Mutator.
     *
     * @param parent The parent to set.
     */
    public void setParent(final UUID parent) {
        _parent = parent;
    }


    /**
     * Mutator.
     *
     * @param createdBy The createdBy to set.
     */
    public void setCreatedBy(final Username createdBy) {
        _createdBy = createdBy;
    }


    /**
     * Mutator.
     *
     * @param changedBy The changedBy to set.
     */
    public void setChangedBy(final Username changedBy) {
        _changedBy = changedBy;
    }


    /**
     * Mutator.
     *
     * @param type The type to set.
     */
    public void setType(final ResourceType type) {
        _type = type;
    }


    /**
     * Mutator.
     *
     * @param childCount The childCount to set.
     */
    public void setChildCount(final int childCount) {
        _childCount = childCount;
    }


    /**
     * Mutator.
     *
     * @param dateCreated The dateCreated to set.
     */
    public void setDateCreated(final Date dateCreated) {
        _dateCreated = dateCreated;
    }


    /**
     * Mutator.
     *
     * @param dateChanged The dateChanged to set.
     */
    public void setDateChanged(final Date dateChanged) {
        _dateChanged = dateChanged;
    }


    /**
     * Mutator.
     *
     * @param count The folder count.
     */
    public void setFolderCount(final int count) {
        _folderCount = count;
    }


    /**
     * Increase the folder count by 1.
     */
    public void incrementFolderCount() {
        setFolderCount(getFolderCount()+1);
    }


    /**
     * Decrease the folder count by 1.
     */
    public void decrementFolderCount() {
        setFolderCount(getFolderCount()-1);
    }


    /**
     * Link.
     *
     * @return A link to this resource's name.
     */
    public Link rename() {
        return new Link(getLink(Resource.NAME));
    }


    /**
     * Link.
     *
     * @return A link to this resource's working copy.
     */
    public Link wc() {
        return new Link(getLink(Resource.WC));
    }


    /**
     * Link.
     *
     * @return A link to the resource collection.
     */
    public Link list() {
        return new Link(getLink(Resource.LIST));
    }


    /**
     * Link.
     *
     * @return A link to this resource's history.
     */
    public Link revisions() {
        return new Link(getLink(Resource.REVISIONS));
    }


    /**
     * Link.
     *
     * @return A link to this resource's history.
     */
    public Link templateRevision() {
        return new Link(getLink(Template.REVISION));
    }


    /**
     * Link.
     *
     * @return A link to this resource's absolute path.
     */
    public Link uriAbsPath() {
        return new Link(getLink(Resource.ABSOLUTE_PATH));
    }


    /**
     * Link.
     *
     * @return A link to this resource's 'include in menu' resource.
     */
    public Link includeMM() {
        return new Link(getLink(Resource.INCLUDE_MM));
    }


    /**
     * Link.
     *
     * @return A link to the user who has locked this resource.
     */
    public Link lock() {
        return new Link(getLink(Resource.LOCK));
    }


    /**
     * Link.
     *
     * @return A link to this resource's parent.
     */
    public Link move() {
        return new Link(getLink(Resource.PARENT));
    }


    /**
     * Link.
     *
     * @return A link to this resource's duration.
     */
    public Link duration() {
        return new Link(getLink(Resource.DURATION));
    }


    /**
     * Link.
     *
     * @return A link to this resource's ACL.
     */
    public Link acl() {
        return new Link(getLink(Resource.ACL));
    }


    /**
     * Link.
     *
     * @return A link to this resource's publisher.
     */
    public Link uriPublish() {
        return new Link(getLink(Resource.PUBLISH));
    }


    /**
     * Link.
     *
     * @return A link to this resource's 'include in menu' resource.
     */
    public Link excludeMM() {
        return new Link(getLink(Resource.EXCLUDE_MM));
    }


    /**
     * Link.
     *
     * @return A link to this resource's metadata.
     */
    public Link uriMetadata() {
        return new Link(getLink(Resource.METADATA));
    }


    /**
     * Link.
     *
     * @return A link to this resource's template.
     */
    public Link uriTemplate() {
        return new Link(getLink(Resource.TEMPLATE));
    }


    /**
     * Link.
     *
     * @return A link to this folder's images collection.
     */
    public Link images() {
        return new Link(getLink(Folder.IMAGES));
    }


    /**
     * Link.
     *
     * @return A link to this resource.
     */
    public Link self() {
        return new Link(getLink(Resource.SELF));
    }


    /**
     * Link.
     *
     * @return A link to this file's binary resource.
     */
    public Link selfBinary() {
        return new Link(getLink(File.SELF_BINARY));
    }


    /**
     * Link.
     *
     * @return A link to the folder collection's 'exists' resource.
     */
    public Link exists() {
        return
            new Link(getLink(Folder.EXISTS));
    }


    /**
     * Link.
     *
     * @return A link to this alias' target name.
     */
    public Link targetName() {
        return
            new Link(getLink(Alias.TARGET_NAME));
    }


    /**
     * Mutator.
     *
     * @param visible Is the resource visible.
     */
    public void setVisible(final boolean visible) {
        _isVisible = visible;
    }


    /**
     * Accessor.
     *
     * @return Returns true if the resource is visible; false otherwise.
     */
    public boolean isVisible() {
        return _isVisible;
    }


    /** UUID : String. */
    public static final String UUID          = "id";
    /** PARENT : String. */
    public static final String PARENT        = "parent";
    /** NAME : String. */
    public static final String NAME          = "name";
    /** PUBLISHED : String. */
    public static final String PUBLISHED     = "publishedBy";
    /** TITLE : String. */
    public static final String TITLE         = "title";
    /** LOCKED : String. */
    public static final String LOCKED        = "lockedBy";
    /** TYPE : String. */
    public static final String TYPE          = "type";
    /** CHILD_COUNT : String. */
    public static final String CHILD_COUNT   = "childCount";
    /** FOLDER_COUNT : String. */
    public static final String FOLDER_COUNT  = "folderCount";
    /** MM_INCLUDE : String. */
    public static final String MM_INCLUDE    = "includeInMainMenu";
    /** WORKING_COPY : String. */
    public static final String WORKING_COPY  = "hasWorkingCopy";
    /** DATE_CHANGED : String. */
    public static final String DATE_CHANGED  = "dateChanged";
    /** DATE_CREATED : String. */
    public static final String DATE_CREATED  = "dateCreated";
    /** ABSOLUTE_PATH : String. */
    public static final String ABSOLUTE_PATH = "absolutePath";
    /** INDEX_PAGE_ID : String. */
    public static final String INDEX_PAGE_ID = "indexPageId";
    /** DESCRIPTION : String. */
    public static final String DESCRIPTION   = "description";
    /** VISIBLE : String. */
    public static final String VISIBLE       = "visible";
}
