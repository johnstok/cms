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

import static ccc.types.DBC.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ccc.commons.WordCharFixer;
import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.CommandType;
import ccc.types.CreatorRoles;
import ccc.types.Duration;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;


/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 *
 * @author Civic Computing Ltd
 */
public abstract class Resource
    extends
        Entity
    implements
        SnapshotSupport<ResourceSnapshot> {

    private static final int MAXIMUM_TITLE_LENGTH = 256;
    private static final int MAXIMUM_DATUM_LENGTH = 1000;
    private static final int MAXIMUM_DATUM_KEY_LENGTH = 100;

    private String         _title             = id().toString();
    private ResourceName   _name              = ResourceName.escape(_title);
    private Template       _template          = null;
    private Folder         _parent            = null;
    private User           _lockedBy          = null;
    private Set<String>    _tags              = new HashSet<String>();
    private Set<String>    _roles             = new HashSet<String>();
    private User           _publishedBy       = null;
    private boolean        _includeInMainMenu = false;
    private Date           _dateCreated       = new Date();
    private Date           _dateChanged       = _dateCreated;
    private Duration       _cache             = null;
    private String         _description       = "";
    private boolean        _deleted           = false;
    private User           _changedBy         = null;
    private User           _createdBy         = null;
    private Map<String, String> _metadata = new HashMap<String, String>();


    /** Constructor: for persistence only. */
    protected Resource() { super(); }


    /**
     * Constructor.
     *
     * @param name The name for this resource.
     * @param title The title of this resource, as a string.
     */
    protected Resource(final ResourceName name,
                       final String title) {
        require().notNull(name);
        require().containsNoBrackets(title);
        name(name);
        title(title);
    }


    /**
     * Constructor.
     * The title parameter is escaped via {@link ResourceName#escape(String)} to
     * determine the resource's name.
     *
     * @param title The title of this resource, as a string.
     */
    public Resource(final String title) {
        require().containsNoBrackets(title);
        title(title);
        name(ResourceName.escape(title));
    }


    /**
     * Query the type of this resource.
     *
     * @return The ResourceType that describes this resource.
     */
    public abstract ResourceType type();


    /**
     * Type-safe helper method to convert an instance of {@link Resource} to a
     * subclass.
     *
     * @param <T> The type that this resource should be converted to.
     * @param resourceType The class representing the type that this resource
     *      should be converted to.
     * @return This resource as a Page.
     */
    public final <T extends Resource> T as(final Class<T> resourceType) {
        return resourceType.cast(this);
    }


    /**
     * Determine the template for this resource. Iterates up the parent
     * hierarchy if necessary.
     *
     * @param def The default template to use if we cannot compute one.
     * @return The template or null if none is found.
     */
    public final Template computeTemplate(final Template def) {
        return
        (null!=template())
            ? template()
            : (null!=_parent)
                ? _parent.computeTemplate(def)
                : def;
    }


    /**
     * Determine the absolute path for a resource.
     *
     * @return The absolute path as a {@link ResourcePath}.
     */
    public final ResourcePath absolutePath() {
        return
        (null==parent())
        ? new ResourcePath(name())
        : parent().absolutePath().append(name());
    }


    /** {@inheritDoc} */
    public ResourceName name() {
        return _name;
    }


    /**
     * Mutator for the name field.
     *
     * @param resourceName The new resource name.
     */
    public void name(final ResourceName resourceName) {
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
    public void title(final String titleString) {
        require().notEmpty(titleString);
        require().maxLength(titleString, MAXIMUM_TITLE_LENGTH);
        require().containsNoBrackets(titleString);
        final WordCharFixer fixer = new WordCharFixer();
        _title = fixer.fix(titleString);
    }


    /**
     * Accessor for the template.
     *
     * @return The {@link Template}.
     */
    public Template template() {
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
    public void template(final Template template) {
        _template = template;
    }


    /**
     * Accessor for the resource's parent.
     *
     * @return The folder containing this resource.
     */
    public Folder parent() {
        return _parent;
    }


    /**
     * Mutator for the resource's parent. <i>This method should only be called
     * by the {@link Folder} class.</i>
     *
     * @param parent The folder containing this resource.
     */
    void parent(final Folder parent) {
        _parent = parent;
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
    public void lock(final User u) throws LockMismatchException {
        require().notNull(u);
        if (isLocked()) {
            throw new LockMismatchException(this);
        }
        _lockedBy = u;
    }


    /**
     * Query method - determine who has locked this resource.
     *
     * @return The locking user or null if the resource is not locked.
     */
    public User lockedBy() {
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
    public void unlock(final User user) throws InsufficientPrivilegesException,
                                               UnlockedException {

        if (!isLocked()) {
            throw new UnlockedException(this);
        }

        if (!canUnlock(user)) {
            throw new InsufficientPrivilegesException(
                CommandType.RESOURCE_UNLOCK, user);
        }

        _lockedBy = null;
    }


    /**
     * Determine whether a user can unlock this resource.
     *
     * @param user The user trying to unlock the resource.
     * @return True if the user can unlock the resource, false otherwise.
     */
    public boolean canUnlock(final User user) {
        return user.equals(lockedBy())
        || user.hasRole(CreatorRoles.ADMINISTRATOR);
    }


    /**
     * Set the tags for this resource.
     *
     * @param tagString A string of comma separated values that represents the
     *  tags for this resource.
     */
    public void tags(final String tagString) {
        require().notNull(tagString);
        require().containsNoBrackets(tagString);

        final String[] tagArray = tagString.split(",");
        _tags.clear();
        for(final String tag : tagArray) {
            if (tag.trim().length() < 1) {
                continue;
            }
            _tags.add(tag.trim());
        }
    }


    /** {@inheritDoc} */
    public Set<String> tags() {
        return Collections.unmodifiableSet(_tags);
    }


    /**
     * Return this resource's tags as a comma separated list.
     *
     * @return A string representation of the tags.
     */
    public String tagString() {
        final StringBuilder sb = new StringBuilder();
        for (final String tag : tags()) {
            sb.append(tag);
            sb.append(',');
        }

        String tagString = sb.toString();
        if (tagString.endsWith(",")) {
            tagString = tagString.substring(0, tagString.length()-1);
        }

        return tagString;
    }


    /**
     * Publish the resource.
     *
     * @param user The user.
     */
    public void publish(final User user) {
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
    public User publishedBy() {
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
     * A resource is secure if itself or any of its parents have roles.
     *
     * @return True if this resource or any of its parents have roles.
     */
    public boolean isSecure() {
        final boolean parentSecure =
            (null==_parent) ? false : _parent.isSecure();
        return parentSecure || !roles().isEmpty();
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
    public void confirmLock(final User user) throws UnlockedException,
                                                    LockMismatchException {
        if (!isLocked()) {
            throw new UnlockedException(this);
        }
        if (!lockedBy().equals(user)) {
            throw new LockMismatchException(this);
        }
    }


    /** {@inheritDoc} */
    public boolean includeInMainMenu() {
        return _includeInMainMenu;
    }


    /**
     * Mutator for 'include in main menu' property.
     *
     * @param shouldInclude Should the resource be included?
     */
    public void includeInMainMenu(final boolean shouldInclude) {
            _includeInMainMenu = shouldInclude;
    }


    /**
     * Accessor for the root parent of this resource.
     *
     * @return The root parent of this resource.
     */
    public Resource root() {
        if (null == _parent) {
            return this;
        }
        return _parent.root();
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


    /** {@inheritDoc} */
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
    public Map<String, String> metadata() {
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
        metadata.putAll(metadata());
        return metadata;
    }


    /**
     * Remove all metadata for this resource.
     */
    public void clearMetadata() {
        _metadata.clear();
    }


    /** {@inheritDoc} */
    public Date dateCreated() {
        return new Date(_dateCreated.getTime());
    }


    /**
     * Query method - determine who created this resource.
     *
     * @return The creating user.
     */
    public User createdBy() {
        return _createdBy;
    }


    /**
     * Mutator for the date the resource was created.
     *
     * @param createdOn The date of creation.
     */
    public void dateCreated(final Date createdOn, final User createdBy) {
        require().notNull(createdOn);
        require().notNull(createdBy);
        _dateCreated = new Date(createdOn.getTime());
        _createdBy = createdBy;
    }


    /** {@inheritDoc} */
    public Date dateChanged() {
        return new Date(_dateChanged.getTime());
    }


    /**
     * Query method - determine who last changed this resource.
     *
     * @return The last user to change this resource.
     */
    public User changedBy() {
        return _changedBy;
    }


    /**
     * Mutator for the date the resource last changed.
     *
     * @param changedOn The date the resource changed.
     */
    public void dateChanged(final Date changedOn, final User changedBy) {
        require().notNull(changedOn);
        require().notNull(changedBy);
        _dateChanged = new Date(changedOn.getTime());
        _changedBy = changedBy;
    }


    /**
     * Mutator.
     *
     * @param roles The roles this collection should have.
     */
    public void roles(final Collection<String> roles) {
        _roles.clear();
        _roles.addAll(roles);
    }


    /**
     * Accessor.
     *
     * @return This resource's roles.
     */
    public Collection<String> roles() {
        return new HashSet<String>(_roles);
    }


    /**
     * Compute the complete set of roles for this resource.
     * This method recursively queries all parents to determine the complete
     * set of roles this resource requires.
     *
     * @return The roles as a collection.
     */
    public Collection<String> computeRoles() {
        // TODO: Can we make this more efficient?
        if (null==_parent) {
            return roles();
        }
        final Collection<String> roles = new ArrayList<String>();
        roles.addAll(_parent.computeRoles());
        roles.addAll(roles());
        return roles;
    }


    /** {@inheritDoc} */
    public boolean isAccessibleTo(final User user) {
        final boolean parentIsAccessible =
            (null==_parent) ? true : parent().isAccessibleTo(user);

        if (0==roles().size()) {
            return parentIsAccessible;
        }

        if (null==user) {
            return false;
        }

        for (final String role : roles()) {
            if (user.hasRole(role)) {
                return parentIsAccessible;
            }
        }

        return false;
    }


    /**
     * Mutator.
     *
     * @param cache The cache duration for the resource.
     */
    public void cache(final Duration cache) {
        _cache = cache;
    }


    /**
     * Accessor.
     *
     * @return This resource's cache duration.
     */
    public Duration cache() {
        return _cache;
    }


    /**
     * Compute the cache duration for the resource.
     *
     * @return This resource's computed cache duration or null
     *  if cache is not set in hierarchy.
     */
    public Duration computeCache() {
        if (_cache != null || parent() == null) {
            return cache();
        }
        return parent().computeCache();
    }


    /** {@inheritDoc} */
    public final String description() {
        return _description;
    }


    /**
     * Mutator for the file description.
     *
     * @param description The new description as a string.
     */
    public void description(final String description) {
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
        json.set(JsonKeys.NAME, name().toString());
        json.set(
            JsonKeys.TEMPLATE_ID,
            (null==template()) ? null : template().id().toString());
        json.set(
            JsonKeys.PARENT_ID,
            (null==parent()) ? null : parent().id().toString());
        json.set(
            JsonKeys.LOCKED_BY,
            (null==lockedBy()) ? null : lockedBy().id().toString());
        json.setStrings(JsonKeys.TAGS, new ArrayList<String>(tags()));
        json.setStrings(JsonKeys.ROLES, new ArrayList<String>(tags()));
        json.set(
            JsonKeys.PUBLISHED_BY,
            (null==publishedBy()) ? null : publishedBy().id().toString());
        json.set(
            JsonKeys.INCLUDE_IN_MAIN_MENU,
            Boolean.valueOf(includeInMainMenu()));
        json.set(JsonKeys.DATE_CREATED, dateCreated());
        json.set(JsonKeys.DATE_CHANGED, dateChanged());
        json.set(JsonKeys.CACHE_DURATION, cache());
        json.set(JsonKeys.DESCRIPTION, description());
        json.set(JsonKeys.TYPE, type().name());
        json.set(JsonKeys.DELETED, Boolean.valueOf(isDeleted()));
    }


    /**
     * Populate the specified DTO with data from this resource.
     *
     * @param dto The DTO to populate.
     */
    protected void setDtoProps(final ResourceSnapshot dto) {
        /* These methods are in alphabetical order, for simplicity. */
        dto.setAbsolutePath(absolutePath().removeTop().toString());
        dto.setCacheDuration(computeCache());
        dto.setDateChanged(dateChanged());
        dto.setDateCreated(dateCreated());
        dto.setDescription(description());
        dto.setId(id());
        dto.setInMainMenu(includeInMainMenu());
        dto.setLocked(isLocked());
        dto.setLockedBy((isLocked()) ? lockedBy().id() : null);
        dto.setMetadata(computeMetadata());
        dto.setName(name());
        dto.setParent((null==parent())?null:parent().id());
        dto.setPublished(isPublished());
        dto.setPublishedBy((isPublished()) ? publishedBy().id() : null);
        dto.setSecure(isSecure());
        dto.setTags(tags());
        dto.setTemplate(
            (null==computeTemplate(null)) ? null : computeTemplate(null).id());
        dto.setTitle(getTitle());
        dto.setType(type());
        dto.setVisible(isVisible());
    }
}
