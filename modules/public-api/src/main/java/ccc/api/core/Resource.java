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
package ccc.api.core;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.types.Duration;
import ccc.api.types.DurationSerializer;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.api.types.Link;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * A read-only snapshot of a resource.
 *
 * @author Civic Computing Ltd.
 */
public class Resource
    extends
        Res {

    private String              _absolutePath;
    private Duration            _cacheDuration;
    private Date                _dateChanged;
    private Date                _dateCreated;
    private String              _description;
    private UUID                _id;
    private boolean             _inMainMenu;
    private boolean             _isLocked;
    private boolean             _isPublished;
    private boolean             _isSecure;
    private boolean             _isVisible;
    private UUID                _lockedBy;
    private Map<String, String> _metadata = new HashMap<String, String>();
    private ResourceName        _name;
    private UUID                _parent;
    private UUID                _publishedBy;
    private int                 _revision;
    private Set<String>         _tags;
    private UUID                _template;
    private String              _title;
    private ResourceType        _type;


    /**
     * Constructor.
     */
    public Resource() { super(); }


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     */
    @Deprecated
    public Resource(final Duration cacheDuration) {
        _cacheDuration = cacheDuration;
        _revision = -1;
        _template = null;
    }

    /**
     * Constructor.
     *
     * @param revision The revision used to create the working copy.
     */
    @Deprecated
    public Resource(final Long revision) {
        _revision = revision.intValue();
        _cacheDuration = null;
        _template = null;
    }


    /**
     * Constructor.
     *
     * @param cacheDuration The duration to set (may be NULL).
     * @param revision The revision used to create the working copy.
     * @param templateId The template id.
     */
    @Deprecated
    public Resource(final Duration cacheDuration,
                                   final Long revision,
                                   final UUID templateId) {
        _revision = revision.intValue();
        _cacheDuration = cacheDuration;
        _template = templateId;
    }

    /**
     * Constructor.
     *
     * @param templateId The template id.
     */
    @Deprecated
    public Resource(final UUID templateId) {
        _template = templateId;
        _cacheDuration = null;
        _revision = -1;
    }


    /**
     * Compute the template for this resource.
     *
     * @return The selected template.
     */
    public final UUID getTemplate() {
        return _template;
    }


    /**
     * Is the resource visible.
     *
     * @return True if the resource is visible, false otherwise.
     */
    public final boolean isVisible() {
        return _isVisible;
    }


    /**
     * Accessor.
     *
     * @return The resource's type.
     */
    public final ResourceType getType() {
        return _type;
    }


    /**
     * Accessor for the file's description.
     *
     * @return The description as a string.
     */
    public final String getDescription() {
        return _description;
    }


    /**
     * Accessor for name.
     *
     * @return The name for this resource, as a {@link ResourceName}.
     */
    public final ResourceName getName() {
        return _name;
    }


    /**
     * Compute the cache duration for this resource.
     *
     * @return The computed duration.
     */
    public final Duration getCacheDuration() {
        return _cacheDuration;
    }


    /**
     * Accessor for the date the resource last changed.
     *
     * @return The date the resource last changed.
     */
    public final Date getDateChanged() {
        return _dateChanged;
    }


    /**
     * Accessor for the date the resource was created.
     *
     * @return The date of creation.
     */
    public final Date getDateCreated() {
        return _dateCreated;
    }


    /**
     * Retrieve metadata for this resource.
     *
     * @param key The key with which the datum was stored.
     * @return The value of the datum. NULL if the datum doesn't exist.
     */
    public final String getMetadatum(final String key) {
        return _metadata.get(key);
    }


    /**
     * Accessor.
     *
     * @return The resource's id.
     */
    public final UUID getId() {
        return _id;
    }


    /**
     * Accessor for 'include in main menu' property.
     *
     * @return True if the resource should be included, false otherwise.
     */
    public final boolean isInMainMenu() {
        return _inMainMenu;
    }


    /**
     * Accessor.
     *
     * @return True if the resource is locked, false otherwise.
     */
    public final boolean isLocked() {
        return _isLocked;
    }


    /**
     * Accessor.
     *
     * @return True if the resource is published, false otherwise.
     */
    public final boolean isPublished() {
        return _isPublished;
    }


    /**
     * Accessor.
     *
     * @return The user that locked the resource or false if the resource is not
     *  locked.
     */
    public final UUID getLockedBy() {
        return _lockedBy;
    }


    /**
     * Accessor.
     *
     * @return The resource's metadata, as a map.
     */
    public final Map<String, String> getMetadata() {
        return _metadata;
    }


    /**
     * Accessor.
     *
     * @return The parent folder for the resource.
     */
    public final UUID getParent() {
        return _parent;
    }


    /**
     * Accessor.
     *
     * @return The user that published the resource or null if the resource
     *  isn't published.
     */
    public final UUID getPublishedBy() {
        return _publishedBy;
    }


    /**
     * Accessor for a resource's tags.
     *
     * @return The tags for this resource as a list.
     */
    public final Set<String> getTags() {
        return _tags;
    }


    /**
     * Accessor for the title.
     *
     * @return The content's title, as a string.
     */
    public final String getTitle() {
        return _title;
    }


    /**
     * Accessor.
     *
     * @return The absolute path to the resource.
     */
    public final String getAbsolutePath() {
        return _absolutePath;
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
     * @param id The id to set.
     */
    public void setId(final UUID id) {
        _id = id;
    }


    /**
     * Mutator.
     *
     * @param name The name to set.
     */
    public void setName(final ResourceName name) {
        _name = name;
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
     * @param parent The parent to set.
     */
    public void setParent(final UUID parent) {
        _parent = parent;
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
     * Accessor.
     *
     * @return True if the resource requires security privileges to access;
     *  false otherwise.
     */
    public boolean isSecure() {
        return _isSecure;
    }


    /**
     * Mutator.
     *
     * @param isSecure The isSecure to set.
     */
    public void setSecure(final boolean isSecure) {
        _isSecure = isSecure;
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
     * Mutator.
     *
     * @param tags The tags to set.
     */
    public void setTags(final Set<String> tags) {
        _tags =
            (null==tags)
                ? new HashSet<String>()
                : new HashSet<String>(tags);
    }


    /**
     * Mutator.
     *
     * @param publishedBy The publishedBy to set.
     */
    public void setPublishedBy(final UUID publishedBy) {
        _publishedBy = publishedBy;
    }


    /**
     * Mutator.
     *
     * @param metadata The metadata to set.
     */
    public void setMetadata(final Map<String, String> metadata) {
        _metadata =
            (null==metadata)
                ? new HashMap<String, String>()
                : new HashMap<String, String>(metadata);
    }


    /**
     * Mutator.
     *
     * @param isPublished The isPublished to set.
     */
    public void setPublished(final boolean isPublished) {
        _isPublished = isPublished;
    }


    /**
     * Mutator.
     *
     * @param lockedBy The lockedBy to set.
     */
    public void setLockedBy(final UUID lockedBy) {
        _lockedBy = lockedBy;
    }


    /**
     * Mutator.
     *
     * @param isLocked The isLocked to set.
     */
    public void setLocked(final boolean isLocked) {
        _isLocked = isLocked;
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
     * @param isVisible The isVisible to set.
     */
    public void setVisible(final boolean isVisible) {
        _isVisible = isVisible;
    }


    /**
     * Mutator.
     *
     * @param template The template to set.
     */
    public void setTemplate(final UUID template) {
        _template = template;
    }



    /**
     * Mutator.
     *
     * @param cacheDuration The cacheDuration to set.
     */
    public void setCacheDuration(final Duration cacheDuration) {
        _cacheDuration = cacheDuration;
    }



    /**
     * Mutator.
     *
     * @param inMainMenu The inMainMenu to set.
     */
    public void setInMainMenu(final boolean inMainMenu) {
        _inMainMenu = inMainMenu;
    }


    /**
     * Mutator.
     *
     * @param revision The revision to set.
     */
    public void setRevision(final int revision) {
        _revision = revision;
    }


    /**
     * Accessor.
     *
     * @return Returns the revision.
     */
    public int getRevision() {
        return _revision;
    }


    /**
     * Accessor.
     *
     * @return True if the snapshot is for a working copy, false otherwise.
     */
    public boolean isWorkingCopy() {
        return _revision<0;
    }


    /**
     * Query.
     *
     * @return True if the resource is cache-able, false otherwise.
     */
    public boolean isCacheable() {
        return null!=_cacheDuration && _cacheDuration.time()>0;
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);
        setAbsolutePath(json.getString(JsonKeys.ABSOLUTE_PATH));
        final Json duration = json.getJson(JsonKeys.CACHE_DURATION);
        setCacheDuration(
            (null==duration) ? null : new DurationSerializer().read(duration));
        setDateChanged(json.getDate(JsonKeys.DATE_CHANGED));
        setDateCreated(json.getDate(JsonKeys.DATE_CREATED));
        setDescription(json.getString(JsonKeys.DESCRIPTION));
        setId(json.getId(JsonKeys.ID));
        setInMainMenu(
            json.getBool(JsonKeys.INCLUDE_IN_MAIN_MENU).booleanValue());
        setLocked(json.getBool(JsonKeys.LOCKED).booleanValue());
        setPublished(json.getBool(JsonKeys.PUBLISHED).booleanValue());
        setSecure(json.getBool(JsonKeys.SECURE).booleanValue());
        setVisible(json.getBool(JsonKeys.VISIBLE).booleanValue());
        setLockedBy(json.getId(JsonKeys.LOCKED_BY));
        setMetadata(json.getStringMap(JsonKeys.METADATA));
        final String name = json.getString(JsonKeys.NAME);
        setName((null==name) ? null : new ResourceName(name));
        setParent(json.getId(JsonKeys.PARENT_ID));
        setPublishedBy(json.getId(JsonKeys.PUBLISHED_BY));
        setRevision(json.getInt(JsonKeys.REVISION).intValue());
        final Collection<String> tags = json.getStrings(JsonKeys.TAGS);
        setTags((null==tags) ? null : new HashSet<String>(tags));
        setTemplate(json.getId(JsonKeys.TEMPLATE_ID));
        setTitle(json.getString(JsonKeys.TITLE));
        final String type = json.getString(JsonKeys.TYPE);
        setType((null==type) ? null : ResourceType.valueOf(type));
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.ABSOLUTE_PATH, _absolutePath);
        json.set(
            JsonKeys.CACHE_DURATION,
            new DurationSerializer().write(json.create(), _cacheDuration));
        json.set(JsonKeys.DATE_CHANGED, _dateChanged);
        json.set(JsonKeys.DATE_CREATED, _dateCreated);
        json.set(JsonKeys.DESCRIPTION, _description);
        json.set(JsonKeys.ID, _id);
        json.set(JsonKeys.INCLUDE_IN_MAIN_MENU, Boolean.valueOf(_inMainMenu));
        json.set(JsonKeys.LOCKED, Boolean.valueOf(_isLocked));
        json.set(JsonKeys.PUBLISHED, Boolean.valueOf(_isPublished));
        json.set(JsonKeys.SECURE, Boolean.valueOf(_isSecure));
        json.set(JsonKeys.VISIBLE, Boolean.valueOf(_isVisible));
        json.set(JsonKeys.LOCKED_BY, _lockedBy);
        json.set(JsonKeys.METADATA, _metadata);
        json.set(JsonKeys.NAME, (null==_name) ? null : _name.toString());
        json.set(JsonKeys.PARENT_ID, _parent);
        json.set(JsonKeys.PUBLISHED_BY, _publishedBy);
        json.set(JsonKeys.REVISION, Long.valueOf(_revision));
        json.setStrings(JsonKeys.TAGS, _tags);
        json.set(JsonKeys.TEMPLATE_ID, _template);
        json.set(JsonKeys.TITLE, _title);
        json.set(JsonKeys.TYPE, (null==_type) ? null : _type.name());
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public Link uriMetadata() {
        return new Link(getLink(METADATA));
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public Link uriTemplate() {
        return new Link(getLink(TEMPLATE));
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public Link duration() {
        return new Link(getLink(DURATION));
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public final Link self() {
        return new Link(getLink(SELF));
    }


    /** NAME : String. */
    public static final String NAME = "name";
    /** WC_APPLY : String. */
    public static final String WC = "wc";
    /** LIST : String. */
    public static final String LIST = "list";
    /** REVISIONS : String. */
    public static final String REVISIONS = "revisions";
    /** ABSOLUTE_PATH : String. */
    public static final String ABSOLUTE_PATH = "absolute-path";
    /** INCLUDE_MM : String. */
    public static final String INCLUDE_MM = "include_mm";
    /** LOCK : String. */
    public static final String LOCK = "lock";
    /** PARENT : String. */
    public static final String PARENT = "parent";
    /** DURATION : String. */
    public static final String DURATION = "duration";
    /** ACL : String. */
    public static final String ACL = "acl";
    /** PUBLISH : String. */
    public static final String PUBLISH = "publish";
    /** EXCLUDE_MM : String. */
    public static final String EXCLUDE_MM = "exclude_mm";
    /** METADATA : String. */
    public static final String METADATA = "metadata";
    /** TEMPLATE : String. */
    public static final String TEMPLATE = "template";
    /** SELF : String. */
    public static final String SELF = "self";
}
