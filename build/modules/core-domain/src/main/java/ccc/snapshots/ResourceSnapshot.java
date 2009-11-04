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
package ccc.snapshots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.entities.IResource;
import ccc.rendering.NotFoundException;
import ccc.rendering.Response;
import ccc.types.Duration;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;


/**
 * A read-only snapshot of a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSnapshot implements IResource {
    private final Resource _delegate;

    /**
     * Constructor.
     *
     * @param delegate The resource to delegate to.
     */
    public ResourceSnapshot(final Resource delegate) {
        _delegate = delegate;
    }

    /**
     * Compute the template for this resource.
     *
     * @param def The default template to use if no template can be computed.
     *
     * @return The selected template.
     */
    public Template computeTemplate(final Template def) {
        return _delegate.computeTemplate(def);
    }

    /**
     * Is the resource visible.
     *
     * @return True if the resource is visible, false otherwise.
     */
    public boolean isVisible() {
        return _delegate.isVisible();
    }

    /**
     * Accessor.
     *
     * @return The resource's type.
     */
    public ResourceType type() {
        return _delegate.type();
    }


    /** {@inheritDoc} */
    public String description() {
        return _delegate.description();
    }


    /** {@inheritDoc} */
    public ResourceName name() {
        return _delegate.name();
    }


    /**
     * Compute the cache duration for this resource.
     *
     * @return The computed duration.
     */
    public Duration computeCache() {
        return _delegate.computeCache();
    }

    /** {@inheritDoc} */
    public Date dateChanged() {
        return _delegate.dateChanged();
    }

    /** {@inheritDoc} */
    public Date dateCreated() {
        return _delegate.dateCreated();
    }

    /** {@inheritDoc} */
    public String getMetadatum(final String key) {
        return _delegate.getMetadatum(key);
    }

    /**
     * Accessor.
     *
     * @return The resource's id.
     */
    public UUID id() {
        return _delegate.id();
    }

    /** {@inheritDoc} */
    public boolean includeInMainMenu() {
        return _delegate.includeInMainMenu();
    }

    /**
     * Accessor.
     *
     * @return True if the resource is locked, false otherwise.
     */
    public boolean isLocked() {
        return _delegate.isLocked();
    }

    /**
     * Accessor.
     *
     * @return True if the resource is published, false otherwise.
     */
    public boolean isPublished() {
        return _delegate.isPublished();
    }

    /**
     * Accessor.
     *
     * @return The user that locked the resource or false if the resource is not
     *  locked.
     */
    public User lockedBy() {
        return _delegate.lockedBy();
    }

    /**
     * Accessor.
     *
     * @return The resource's metadata, as a map.
     */
    public Map<String, String> metadata() {
        return _delegate.metadata();
    }

    /**
     * Accessor.
     *
     * @return The parent folder for the resource.
     */
    public Folder parent() {
        return _delegate.parent();
    }

    /**
     * Accessor.
     *
     * @return The user that published the resource or null if the resource
     *  isn't published.
     */
    public User publishedBy() {
        return _delegate.publishedBy();
    }

    /**
     * Accessor.
     *
     * @return The root parent folder for this resource.
     */
    public Resource root() {
        return _delegate.root();
    }

    /** {@inheritDoc} */
    public Set<String> tags() {
        return _delegate.tags();
    }

    /**
     * Accessor.
     *
     * @return The tags for this resource, as a comma-delimited string.
     */
    public String tagString() {
        return _delegate.tagString();
    }

    /** {@inheritDoc} */
    public String getTitle() {
        return _delegate.getTitle();
    }

    /**
     * Accessor.
     *
     * @return The absolute path to the resource.
     */
    public ResourcePath absolutePath() {
        return _delegate.absolutePath();
    }

    /**
     * Render the resource, as a response.
     *
     * @return A response representing the resource.
     */
    @SuppressWarnings("unused")
    public Response render() {
        throw new NotFoundException();
    }

    /**
     * Retrieve the parents of this resource, as a list.
     *
     * @return The list of parents.
     */
    public List<IResource> selectPathElements() {

            final List<IResource> elements =
                new ArrayList<IResource>();

            ResourceSnapshot current = this;

            elements.add(current);
            while (current.parent() != null) {
                current = current.parent().forCurrentRevision();
                elements.add(current);
            }
            Collections.reverse(elements);
            return elements;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAccessibleTo(final User user) {
        return _delegate.isAccessibleTo(user);
    }


}
