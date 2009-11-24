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
package ccc.rest.snapshots;

import java.io.Serializable;
import java.util.Date;
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

    private String _absolutePath;
    private String _title;
    private Set<String> _tags;
    private UUID _root;
    private UUID _publishedBy;
    private UUID _parent;
    private Map<String, String> _metadata;
    private boolean _isPublished;
    private UUID _lockedBy;
    private boolean _isLocked;
    private boolean _includeInMainMenu;
    private UUID _id;
    private Date _dateCreated;
    private Date _dateChanged;
    private Duration _cache;
    private ResourceName _name;
    private String _description;
    private ResourceType _type;
    private boolean _isVisible;
    private UUID _template;
    private boolean _isSecure;
    private int _revision = 0;


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
        return _cache;
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
        return _includeInMainMenu;
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
    public final UUID getRoot() {
        return _root;
    }


    /** {@inheritDoc} */
    @Override
    public final Set<String> getTags() {
        return _tags;
    }


    /** {@inheritDoc} */
    @Override
    public final String getTagString() {
        final StringBuilder sb = new StringBuilder();
        for (final String tag : getTags()) {
            sb.append(tag);
            sb.append(',');
        }

        String tagString = sb.toString();
        if (tagString.endsWith(",")) {
            tagString = tagString.substring(0, tagString.length()-1);
        }

        return tagString;
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
        _tags = tags;
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
        _metadata = metadata;
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
     * Accessor..
     *
     * @return True if the snapshot is for the current revision, false
     *  otherwise.
     */
    public boolean isCurrentRevision() {
        return 0==_revision;
    }



//    /** {@inheritDoc} */
//    @Override
//    public final List<IResource> selectPathElements() {
//
//            final List<IResource> elements =
//                new ArrayList<IResource>();
//
//            ResourceSnapshot current = this;
//
//            elements.add(current);
//            while (current.parent() != null) {
//                current = current.parent().forCurrentRevision();
//                elements.add(current);
//            }
//            Collections.reverse(elements);
//            return elements;
//    }
//
//
//    /** {@inheritDoc} */
//    @Override
//    public final boolean isAccessibleTo(final User user) {
//        return _delegate.isAccessibleTo(user);
//    }
}
