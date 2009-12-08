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
package ccc.rest.snapshots;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.rest.entities.IResource;
import ccc.types.Duration;
import ccc.types.ResourceName;
import ccc.types.ResourceType;


/**
 * A read-only snapshot of a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSnapshot implements IResource, Serializable {

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
    private Map<String, String> _metadata;
    private ResourceName        _name;
    private UUID                _parent;
    private UUID                _publishedBy;
    private int                 _revision;
    private Set<String>         _tags;
    private UUID                _template;
    private String              _title;
    private ResourceType        _type;


    /** {@inheritDoc} */
    @Override
    public final UUID getTemplate() {
        return _template;
    }


    /** {@inheritDoc} */
    @Override
    public final boolean isVisible() {
        return _isVisible;
    }


    /** {@inheritDoc} */
    @Override
    public final ResourceType getType() {
        return _type;
    }


    /** {@inheritDoc} */
    @Override
    public final String getDescription() {
        return _description;
    }


    /** {@inheritDoc} */
    @Override
    public final ResourceName getName() {
        return _name;
    }


    /** {@inheritDoc} */
    @Override
    public final Duration getCacheDuration() {
        return _cacheDuration;
    }


    /** {@inheritDoc} */
    @Override
    public final Date getDateChanged() {
        return _dateChanged;
    }


    /** {@inheritDoc} */
    @Override
    public final Date getDateCreated() {
        return _dateCreated;
    }


    /** {@inheritDoc} */
    @Override
    public final String getMetadatum(final String key) {
        return _metadata.get(key);
    }


    /** {@inheritDoc} */
    @Override
    public final UUID getId() {
        return _id;
    }


    /** {@inheritDoc} */
    @Override
    public final boolean isInMainMenu() {
        return _inMainMenu;
    }


    /** {@inheritDoc} */
    @Override
    public final boolean isLocked() {
        return _isLocked;
    }


    /** {@inheritDoc} */
    @Override
    public final boolean isPublished() {
        return _isPublished;
    }


    /** {@inheritDoc} */
    @Override
    public final UUID getLockedBy() {
        return _lockedBy;
    }


    /** {@inheritDoc} */
    @Override
    public final Map<String, String> getMetadata() {
        return _metadata;
    }


    /** {@inheritDoc} */
    @Override
    public final UUID getParent() {
        return _parent;
    }


    /** {@inheritDoc} */
    @Override
    public final UUID getPublishedBy() {
        return _publishedBy;
    }


    /** {@inheritDoc} */
    @Override
    public final Set<String> getTags() {
        return _tags;
    }


    /** {@inheritDoc} */
    @Override
    public final String getTitle() {
        return _title;
    }


    /** {@inheritDoc} */
    @Override
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


    /** {@inheritDoc} */
    @Override
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
        _tags = new HashSet<String>(tags);
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
        _metadata = new HashMap<String, String>(metadata);
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
}
