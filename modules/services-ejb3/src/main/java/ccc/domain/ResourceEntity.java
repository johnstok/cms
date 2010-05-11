/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import static ccc.api.types.DBC.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.exceptions.InsufficientPrivilegesException;
import ccc.api.exceptions.LockMismatchException;
import ccc.api.exceptions.UnlockedException;
import ccc.api.types.ACL;
import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.api.types.Duration;
import ccc.api.types.DurationSerializer;
import ccc.api.types.Permission;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.api.types.URIBuilder;
import ccc.api.types.ACL.Entry;
import ccc.commons.CharConversion;
import ccc.commons.NormalisingEncoder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;


/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 *
 * @author Civic Computing Ltd
 */
public abstract class ResourceEntity
    extends
        Entity
    implements
        SnapshotSupport<Resource> {

    private static final int MAXIMUM_TITLE_LENGTH = 256;
    private static final int MAXIMUM_DATUM_LENGTH = 1000;
    private static final int MAXIMUM_DATUM_KEY_LENGTH = 100;

    private String         _title             = getId().toString();
    private ResourceName   _name              = ResourceName.escape(_title);
    private TemplateEntity       _template          = null;
    private FolderEntity         _parent            = null;
    private Integer        _parentIndex       = null;
    private UserEntity           _lockedBy          = null;
    private Set<String>    _tags              = new HashSet<String>();
    private Set<AccessPermission> _groupAcl   = new HashSet<AccessPermission>();
    private Set<AccessPermission> _userAcl    = new HashSet<AccessPermission>();
    private UserEntity           _publishedBy       = null;
    private boolean        _includeInMainMenu = false;
    private Date           _dateCreated       = new Date();
    private Date           _dateChanged       = _dateCreated;
    private Duration       _cache             = null;
    private String         _description       = "";
    private boolean        _deleted           = false;
    private UserEntity           _changedBy         = null;
    private UserEntity           _createdBy         = null;
    private Map<String, String> _metadata = new HashMap<String, String>();


    /** Constructor: for persistence only. */
    protected ResourceEntity() { super(); }


    /**
     * Constructor.
     *
     * @param name The name for this resource.
     * @param title The title of this resource, as a string.
     */
    protected ResourceEntity(final ResourceName name,
                       final String title) {
        setName(name);
        setTitle(title);
    }


    /**
     * Constructor.
     * The title parameter is escaped via {@link ResourceName#escape(String)} to
     * determine the resource's name.
     *
     * @param title The title of this resource, as a string.
     */
    public ResourceEntity(final String title) {
        setTitle(title);
        setName(ResourceName.escape(title));
    }


    /**
     * Query the type of this resource.
     *
     * @return The ResourceType that describes this resource.
     */
    public abstract ResourceType getType();


    /**
     * Type-safe helper method to convert an instance of {@link ResourceEntity} to a
     * subclass.
     *
     * @param <T> The type that this resource should be converted to.
     * @param resourceType The class representing the type that this resource
     *      should be converted to.
     * @return This resource as a Page.
     */
    public final <T extends ResourceEntity> T as(final Class<T> resourceType) {
        return resourceType.cast(this);
    }


    /**
     * Determine the template for this resource. Iterates up the parent
     * hierarchy if necessary.
     *
     * @param def The default template to use if we cannot compute one.
     * @return The template or null if none is found.
     */
    public final TemplateEntity computeTemplate(final TemplateEntity def) {
        return
        (null!=getTemplate())
            ? getTemplate()
            : (null!=_parent)
                ? _parent.computeTemplate(def)
                : def;
    }


    /**
     * Determine the absolute path for a resource.
     *
     * @return The absolute path as a {@link ResourcePath}.
     */
    public final ResourcePath getAbsolutePath() {
        return
        (null==getParent())
        ? new ResourcePath(getName())
        : getParent().getAbsolutePath().append(getName());
    }


    /** {@inheritDoc} */
    public ResourceName getName() {
        return _name;
    }


    /**
     * Mutator for the name field.
     *
     * @param resourceName The new resource name.
     */
    public void setName(final ResourceName resourceName) {
        require().notNull(resourceName);
        _name = resourceName;
    }


    /** {@inheritDoc} */
    public String getTitle() {
        return _title;
    }


    /**
     * Sets the title of the resource.
     *
     * @param titleString The new title for this resource.
     */
    public void setTitle(final String titleString) {
        require().notEmpty(titleString);
        require().maxLength(titleString, MAXIMUM_TITLE_LENGTH);
        require().containsNoBrackets(titleString);
        final CharConversion fixer = new CharConversion();
        _title = fixer.fix(titleString);
    }


    /**
     * Accessor for the template.
     *
     * @return The {@link TemplateEntity}.
     */
    public TemplateEntity getTemplate() {
        if (null==_template) {
            return null;
        } else if (_template.isDeleted()) {
            return null;
        }
        return _template;
    }


    /**
     * Sets the template for this resource.
     *
     * @param template The new template.
     */
    public void setTemplate(final TemplateEntity template) {
        _template = template;
    }


    /**
     * Accessor for the resource's parent.
     *
     * @return The folder containing this resource.
     */
    public FolderEntity getParent() {
        return _parent;
    }


    /**
     * Mutator for the resource's parent. <i>This method should only be called
     * by the {@link FolderEntity} class.</i>
     *
     * @param parent The folder containing this resource.
     */
    void setParent(final FolderEntity parent, final Integer index) {
        _parent = parent;
        if (null!=parent) {
            _parentIndex = require().notNull(index);
        } else {
            _parentIndex = null;
        }
    }


    /**
     * Query method to determine whether a resource is locked.
     *
     * @return True if the resource is locked, false otherwise.
     */
    public boolean isLocked() {
        return null != _lockedBy;
    }


    /**
     * Lock a resource.
     *
     * @param u The user who is locking the resource.
     * @throws LockMismatchException If the resource is already locked.
     */
    public void lock(final UserEntity u) throws LockMismatchException {
        require().notNull(u);
        if (isLocked()) {
            throw new LockMismatchException(getId());
        }
        _lockedBy = u;
    }


    /**
     * Query method - determine who has locked this resource.
     *
     * @return The locking user or null if the resource is not locked.
     */
    public UserEntity getLockedBy() {
        return _lockedBy;
    }


    /**
     * Unlock the resource.
     * Only the user who locked the resource, or an administrator may call this
     * method.
     *
     * @param user The user releasing the lock.
     * @throws UnlockedException If the resource isn't locked.
     * @throws InsufficientPrivilegesException If the user has insufficient
     *  privileges to unlock the resource.
     */
    public void unlock(final UserEntity user) throws InsufficientPrivilegesException,
                                               UnlockedException {

        if (!isLocked()) {
            throw new UnlockedException(getId());
        }

        if (!canUnlock(user)) {
            throw new InsufficientPrivilegesException(
                CommandType.RESOURCE_UNLOCK, user.getId());
        }

        _lockedBy = null;
    }


    /**
     * Determine whether a user can unlock this resource.
     *
     * @param user The user trying to unlock the resource.
     * @return True if the user can unlock the resource, false otherwise.
     */
    public boolean canUnlock(final UserEntity user) {
        return user.equals(getLockedBy())
        || user.hasPermission(Permission.RESOURCE_UNLOCK);
    }


    /**
     * Set the tags for this resource.
     *
     * @param tags A set of string tags.
     */
    public void setTags(final Set<String> tags) {
        require().notNull(tags);
        _tags.clear();
        for (final String tag : tags) {
            require().containsNoBrackets(tag);
            final String clean = tag.trim();
            if (clean.length()<1) { continue; }
            _tags.add(clean);
        }
    }


    /** {@inheritDoc} */
    public Set<String> getTags() {
        return new HashSet<String>(_tags);
    }


    /**
     * Publish the resource.
     *
     * @param user The user.
     */
    public void publish(final UserEntity user) {
        require().notNull(user);
        _publishedBy = user;
    }


    /**
     * Query method to determine whether a resource is published.
     *
     * @return True if the resource is published, false otherwise.
     */
    public boolean isPublished() {
        return _publishedBy != null;
    }


    /**
     * Return user who published the resource.
     *
     * @return The user or null if the resource is unpublished.
     */
    public UserEntity getPublishedBy() {
        return _publishedBy;
    }


    /**
     * Unpublish the resource.
     */
    public void unpublish() {
        _publishedBy = null;
    }


    /**
     * Query method to determine whether a resource is visible.
     *
     * A resource is visible if itself and all of its parents are published.
     *
     * @return True if this resource and all parents are published.
     */
    public boolean isVisible() {
        final boolean parentVisible =
            (null==_parent) ? true : _parent.isVisible();
        return parentVisible && isPublished() && !isDeleted();
    }


    /**
     * Query method to determine whether a resource is secure.
     *
     * A resource is secure if itself or any of its parents has access control
     * entries.
     *
     * @return True if this resource is secure.
     */
    public boolean isSecure() {
        final boolean parentSecure =
            (null==_parent) ? false : _parent.isSecure();
        return
            parentSecure
            || !getGroupAcl().isEmpty()
            || !getUserAcl().isEmpty();
    }


    /**
     * Confirm this resource is locked by the specified user.
     * If this resource is locked by the specified user this method does
     * nothing; otherwise an exception is thrown.
     *
     * @param user The user who should have the lock.
     * @throws UnlockedException If the resource isn't locked.
     * @throws LockMismatchException If the resource is locked by another user.
     */
    public void confirmLock(final UserEntity user) throws UnlockedException,
                                                    LockMismatchException {
        if (!isLocked()) {
            throw new UnlockedException(getId());
        }
        if (!getLockedBy().equals(user)) {
            throw new LockMismatchException(getId());
        }
    }


    /** {@inheritDoc} */
    public boolean isIncludedInMainMenu() {
        return _includeInMainMenu;
    }


    /**
     * Mutator for 'include in main menu' property.
     *
     * @param shouldInclude Should the resource be included?
     */
    public void setIncludedInMainMenu(final boolean shouldInclude) {
            _includeInMainMenu = shouldInclude;
    }


    /**
     * Accessor for the root parent of this resource.
     *
     * @return The root parent of this resource.
     */
    public ResourceEntity getRoot() {
        if (null == _parent) {
            return this;
        }
        return _parent.getRoot();
    }


    /**
     * Add new metadata for this resource.
     *
     * @param key The key by which the datum will be accessed.
     * @param value The value of the datum. May not be NULL.
     */
    public void addMetadatum(final String key, final String value) {
        require().notEmpty(value);
        require().maxLength(value, MAXIMUM_DATUM_LENGTH);
        require().notEmpty(key);
        require().maxLength(key, MAXIMUM_DATUM_KEY_LENGTH);
        require().containsNoBrackets(key);
        require().containsNoBrackets(value);
        _metadata.put(key, value);
    }


    /**
     * Calculate the resource's metadatum for a given key.
     * <p>If this resource has no value for the key its parents will be checked
     * recursively.
     *
     * @param key The key to the metadatum.
     *
     * @return The metadatum's value.
     */
    public String getMetadatum(final String key) {
        String datum = _metadata.get(key);
        if (null==datum && null!=_parent) {
            datum = _parent.getMetadatum(key);
        }
        return datum;
    }


    /**
     * Remove the metadatum with the specified key.
     *
     * @param key The key with which the datum was stored.
     */
    public void clearMetadatum(final String key) {
        require().notEmpty(key);
        _metadata.remove(key);
    }


    /**
     * Accessor for all metadata of the resource. Does not return any parent
     * metadata.
     *
     * @return The metadata as a hash map.
     */
    public Map<String, String> getMetadata() {
        return new HashMap<String, String>(_metadata);
    }

    /**
     * Compute the complete set of metadata for this resource.
     * This method recursively queries all parents to determine the complete
     * set of metadata.
     *
     * @return The metadata as a map.
     */
    public Map<String, String> computeMetadata() {
        // TODO: Can we make this more efficient?
        if (null==_parent) {
            return new HashMap<String, String>(_metadata);
        }
        final HashMap<String, String> metadata =new HashMap<String, String>();
        metadata.putAll(_parent.computeMetadata());
        metadata.putAll(getMetadata());
        return metadata;
    }


    /**
     * Remove all metadata for this resource.
     */
    public void clearMetadata() {
        _metadata.clear();
    }


    /** {@inheritDoc} */
    public Date getDateCreated() {
        return new Date(_dateCreated.getTime());
    }


    /**
     * Query method - determine who created this resource.
     *
     * @return The creating user.
     */
    public UserEntity getCreatedBy() {
        return _createdBy;
    }


    /**
     * Mutator for the date the resource was created.
     *
     * @param createdOn The date of creation.
     * @param createdBy The user who created.
     */
    public void setDateCreated(final Date createdOn, final UserEntity createdBy) {
        require().notNull(createdOn);
        require().notNull(createdBy);
        _dateCreated = new Date(createdOn.getTime());
        _createdBy = createdBy;
    }


    /** {@inheritDoc} */
    public Date getDateChanged() {
        return new Date(_dateChanged.getTime());
    }


    /**
     * Query method - determine who last changed this resource.
     *
     * @return The last user to change this resource.
     */
    public UserEntity getChangedBy() {
        return _changedBy;
    }


    /**
     * Mutator for the date the resource last changed.
     *
     * @param changedOn The date the resource changed.
     * @param changedBy The user who changed the resource.
     */
    public void setDateChanged(final Date changedOn, final UserEntity changedBy) {
        require().notNull(changedOn);
        require().notNull(changedBy);
        _dateChanged = new Date(changedOn.getTime());
        _changedBy = changedBy;
    }


    /**
     * Mutator.
     *
     * @param p The permission to add.
     */
    public void addGroupPermission(final AccessPermission p) {
        _groupAcl.add(p);
    }


    /**
     * Accessor.
     *
     * @return This resource's groups.
     */
    public Collection<Entry> getGroupAcl() {
        final Set<Entry> acl = new HashSet<Entry>();
        for (final AccessPermission p : _groupAcl) {
            acl.add(p.createEntry());
        }
        return acl;
    }


    /**
     * Mutator.
     *
     * @param p The permission to add.
     */
    public void addUserPermission(final AccessPermission p) {
        _userAcl.add(p);
    }


    /**
     * Accessor.
     *
     * @return The users allowed to access this resource.
     */
    public Collection<Entry> getUserAcl() {
        final Set<Entry> acl = new HashSet<Entry>();
        for (final AccessPermission p : _userAcl) {
            acl.add(p.createEntry());
        }
        return acl;
    }


    /** {@inheritDoc} */
    public boolean isReadableBy(final UserEntity user) {
        final boolean parentIsAccessible =
            (null==_parent) ? true : getParent().isReadableBy(user);

        if (0==_groupAcl.size() && 0==_userAcl.size()) {
            return parentIsAccessible;
        }

        if (null==user) { return false; }

        for (final AccessPermission p : _groupAcl) {
            if (p.allowsRead(user)) {
                return parentIsAccessible;
            }
        }

        for (final AccessPermission p : _userAcl) {
            if (p.allowsRead(user)) {
                return parentIsAccessible;
            }
        }

        return false;
    }


    /** {@inheritDoc} */
    public boolean isWriteableBy(final UserEntity user) {
        final boolean parentIsWriteable =
            (null==_parent) ? true : getParent().isWriteableBy(user);

        if (0==_groupAcl.size() && 0==_userAcl.size()) {
            return parentIsWriteable;
        }

        if (null==user) { return false; }

        for (final AccessPermission p : _groupAcl) {
            if (p.allowsWrite(user)) {
                return parentIsWriteable;
            }
        }

        for (final AccessPermission p : _userAcl) {
            if (p.allowsWrite(user)) {
                return parentIsWriteable;
            }
        }

        return false;
    }


    /**
     * Mutator.
     *
     * @param cache The cache duration for the resource.
     */
    public void setCacheDuration(final Duration cache) {
        _cache = cache;
    }


    /**
     * Accessor.
     *
     * @return This resource's cache duration.
     */
    public Duration getCacheDuration() {
        return _cache;
    }


    /**
     * Compute the cache duration for the resource.
     *
     * @return This resource's computed cache duration or null
     *  if cache is not set in hierarchy.
     */
    public Duration computeCache() {
        if (_cache != null || getParent() == null) {
            return getCacheDuration();
        }
        return getParent().computeCache();
    }


    /** {@inheritDoc} */
    public final String getDescription() {
        return _description;
    }


    /**
     * Mutator for the file description.
     *
     * @param description The new description as a string.
     */
    public void setDescription(final String description) {
        _description = (null==description) ? "" : description;
    }


    /**
     * Add metadata to this resource.
     *
     * @param metadata The metadata to add, as a hashmap.
     */
    public void addMetadata(final Map<String, String> metadata) {
        _metadata.putAll(metadata);
    }


    /**
     * Query method.
     *
     * @return True if this resource is deleted; false otherwise.
     */
    public boolean isDeleted() {
        return _deleted;
    }


    /**
     * Mark this file as deleted.
     */
    public void delete() {
        _deleted = true;
    }


    /**
     * Mark this file as un-deleted.
     */
    public void undelete() {
        _deleted = false;
    }


    /**
     * Create a snapshot of this resource that can be serialized to JSON.
     *
     * @return The snapshot as an implementation of {@link Jsonable}.
     */
    public abstract Jsonable createSnapshot();


    /** {@inheritDoc} */
    @Override public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.TITLE, getTitle());
        json.set(JsonKeys.NAME, getName().toString());
        json.set(
            JsonKeys.TEMPLATE_ID,
            (null==getTemplate()) ? null : getTemplate().getId().toString());
        json.set(
            JsonKeys.PARENT_ID,
            (null==getParent()) ? null : getParent().getId().toString());
        json.set(
            JsonKeys.LOCKED_BY,
            (null==getLockedBy()) ? null : getLockedBy().getId().toString());
        json.setStrings(JsonKeys.TAGS, new ArrayList<String>(getTags()));
        json.set(
            JsonKeys.ACL,
            getAcl());
        json.set(
            JsonKeys.PUBLISHED_BY,
            (null==getPublishedBy()) ? null : getPublishedBy().getId().toString());
        json.set(
            JsonKeys.INCLUDE_IN_MAIN_MENU,
            Boolean.valueOf(isIncludedInMainMenu()));
        json.set(JsonKeys.DATE_CREATED, getDateCreated());
        json.set(JsonKeys.DATE_CHANGED, getDateChanged());
        json.set(
            JsonKeys.CACHE_DURATION,
            new DurationSerializer().write(json.create(), getCacheDuration()));
        json.set(JsonKeys.DESCRIPTION, getDescription());
        json.set(JsonKeys.TYPE, getType().name());
        json.set(JsonKeys.DELETED, Boolean.valueOf(isDeleted()));
    }


    /**
     * Accessor.
     *
     * @return An ACL for this resource.
     */
    public ACL getAcl() {
        final ACL acl = new ACL();
        acl.setGroups(getGroupAcl());
        acl.setUsers(getUserAcl());
        return acl;
    }


    /**
     * Populate the specified DTO with data from this resource.
     *
     * @param dto The DTO to populate.
     */
    protected void setDtoProps(final Resource dto) {
        /* These methods are in alphabetical order, for simplicity. */
        dto.setAbsolutePath(getAbsolutePath().removeTop().toString());
        dto.setCacheDuration(computeCache());
        dto.setDateChanged(getDateChanged());
        dto.setDateCreated(getDateCreated());
        dto.setDescription(getDescription());
        dto.setId(getId());
        dto.setInMainMenu(isIncludedInMainMenu());
        dto.setLocked(isLocked());
        dto.setLockedBy((isLocked()) ? getLockedBy().getId() : null);
        dto.setMetadata(computeMetadata());
        dto.setName(getName());
        dto.setParent((null==getParent())?null:getParent().getId());
        dto.setPublished(isPublished());
        dto.setPublishedBy((isPublished()) ? getPublishedBy().getId() : null);
        dto.setSecure(isSecure());
        dto.setTags(getTags());
        dto.setTemplate(
            (null==computeTemplate(null)) ? null : computeTemplate(null).getId());
        dto.setTitle(getTitle());
        dto.setType(getType());
        dto.setVisible(isVisible());

        dto.addLink(
            Resource.METADATA,
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.METADATA)
                .build("id", getId().toString(), new NormalisingEncoder()));
        dto.addLink(
            Resource.TEMPLATE,
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.TEMPLATE)
            .build("id", getId().toString(), new NormalisingEncoder()));
        dto.addLink(
            Resource.DURATION,
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.DURATION)
            .build("id", getId().toString(), new NormalisingEncoder()));
    }


    /**
     * Map a collection of Resource to a collection of ResourceSummary.
     *
     * @param resources The collection of resources to map.
     * @return The corresponding collection of ResourceSummary.
     */
    public static List<ResourceSummary> mapResources(
                               final Collection<? extends ResourceEntity> resources) {
        final List<ResourceSummary> mapped =
            new ArrayList<ResourceSummary>();
        for (final ResourceEntity r : resources) {
            mapped.add(r.mapResource());
        }
        return mapped;
    }


    /**
     * Create a summary for a resource.
     *
     * @return The corresponding summary.
     */
    public ResourceSummary mapResource() {
        int childCount = 0;
        int folderCount = 0;
        String sortOrder = null;
        UUID indexPage = null;
        boolean hasWorkingCopy = false;
        if (getType() == ResourceType.FOLDER) {
            childCount = as(FolderEntity.class).getEntries().size();
            folderCount = as(FolderEntity.class).getFolders().size();
            sortOrder = as(FolderEntity.class).getSortOrder().name();
            indexPage = (null==as(FolderEntity.class).getIndexPage())
                ? null : as(FolderEntity.class).getIndexPage().getId();
        } else if (getType() == ResourceType.PAGE) {
            hasWorkingCopy = (as(PageEntity.class).hasWorkingCopy());
        } else if (getType() == ResourceType.FILE) {
            hasWorkingCopy = (as(FileEntity.class).hasWorkingCopy());
        }

        final ResourceSummary rs =
            new ResourceSummary(
                getId(),
                (null==getParent()) ? null : getParent().getId(),
                getName().toString(),
                (isPublished())
                    ? getPublishedBy().getUsername() : null,
                getTitle(),
                (isLocked()) ? getLockedBy().getUsername() : null,
                getType(),
                childCount,
                folderCount,
                isIncludedInMainMenu(),
                sortOrder,
                hasWorkingCopy,
                getDateCreated(),
                getDateChanged(),
                (null==getTemplate()) ? null : getTemplate().getId(),
                getTags(),
                getAbsolutePath().removeTop().toString(),
                indexPage,
                getDescription(),
                (getCreatedBy() != null)
                    ? getCreatedBy().getUsername() : null,
                (getChangedBy() != null)
                    ? getChangedBy().getUsername() : null
            );

        rs.addLink(
            "revisions",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.REVISIONS)
                .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "absolute-path",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.PATH)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "template",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.TEMPLATE)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "metadata",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.METADATA)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "exclude_mm",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.EXCLUDE_MM)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "publish",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.PUBLISH)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "acl",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.ACL)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "unlock",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.UNLOCK)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "duration",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.DURATION)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "parent",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.PARENT)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "lock",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.LOCK)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "include_mm",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.INCLUDE_MM)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "wc-create",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.WC_CREATE)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "delete",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.DELETE)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "unpublish",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.UNPUBLISH)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "list",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.LIST)
            .build("id", getId().toString(), new NormalisingEncoder())
            +"?{-join|&|parent,sort,order,page,count,type}");
        rs.addLink(
            "wc_clear",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.WC_CLEAR)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "wc_apply",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.WC_APPLY)
            .build("id", getId().toString(), new NormalisingEncoder()));
        rs.addLink(
            "name",
            new URIBuilder(ccc.api.core.ResourceIdentifiers.Resource.NAME)
            .build("id", getId().toString(), new NormalisingEncoder()));

        switch (getType()) {
            case ALIAS:
                rs.addLink(
                    Resource.SELF,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.Alias.ELEMENT)
                    .build("id", getId().toString(), new NormalisingEncoder()));
                break;

            case PAGE:
                rs.addLink(
                    Resource.SELF,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.Page.ELEMENT)
                    .build("id", getId().toString(), new NormalisingEncoder()));
                rs.addLink(
                    Page.WORKING_COPY,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.Page.WC)
                    .build("id", getId().toString(), new NormalisingEncoder()));
                break;

            case FOLDER:
                rs.addLink(
                    Resource.SELF,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.Folder.ELEMENT)
                    .build("id", getId().toString(), new NormalisingEncoder()));
                rs.addLink(
                    Folder.EXISTS,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.Folder.ELEMENT)
                    .build("id", getId().toString(), new NormalisingEncoder())
                    + "/{name}/exists"); // FIXME: How to only replace one param
                break;

            case FILE:
                rs.addLink(
                    Resource.SELF,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.File.ELEMENT)
                    .build("id", getId().toString(), new NormalisingEncoder()));
                break;

            case TEMPLATE:
                rs.addLink(
                    Resource.SELF,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.Template.ELEMENT)
                    .build("id", getId().toString(), new NormalisingEncoder()));
                rs.addLink(
                    Resource.DELTA,
                    new URIBuilder(ccc.api.core.ResourceIdentifiers.Template.DELTA)
                    .build("id", getId().toString(), new NormalisingEncoder()));
                break;

            default:
                break;
        }

        return rs;
    }


    /**
     * Determine whether a resource can be indexed by the search engine.
     *
     * @return True if the resource can be indexed; false otherwise.
     */
    public boolean isIndexable() {
        return Boolean.valueOf(getMetadatum("searchable")).booleanValue();
    }


    /**
     * Set the index value for this resource.
     *
     * @param index The index value.
     */
    public void setIndexPosition(final int index) {
        DBC.require().minValue(index, 0);
        _parentIndex = Integer.valueOf(index);
    }


    /**
     * Accessor.
     *
     * @return The resource's index in its parent folder.
     */
    public Integer getIndex() { return _parentIndex; }


    /**
     * Remove all group permissions.
     */
    public void clearGroupAcl() {
        _groupAcl.clear();
    }


    /**
     * Remove all user permissions.
     */
    public void clearUserAcl() {
        _userAcl.clear();
    }
}
