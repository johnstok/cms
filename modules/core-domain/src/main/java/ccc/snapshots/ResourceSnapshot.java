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

import ccc.api.Duration;
import ccc.api.ResourceType;
import ccc.api.template.IResourceSnapshot;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.rendering.NotFoundException;
import ccc.rendering.Response;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.StatefulReader;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSnapshot implements IResourceSnapshot {
    private final Resource _delegate;

    /**
     * Constructor.
     *
     * @param delegate
     */
    public ResourceSnapshot(final Resource delegate) {
        _delegate = delegate;
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param builtInPageTemplate
     * @return
     */
    public Template computeTemplate(final Template def) {
        return _delegate.computeTemplate(def);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public boolean isVisible() {
        return _delegate.isVisible();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public ResourceType type() {
        return _delegate.type();
    }


    /** {@inheritDoc} */
    public String description() {
        return _delegate.description();
    }


    /** {@inheritDoc} */
    public String name() {
        return _delegate.name().toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
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
     * @return
     * @see ccc.domain.Entity#id()
     */
    public UUID id() {
        return _delegate.id();
    }

    /** {@inheritDoc} */
    public boolean includeInMainMenu() {
        return _delegate.includeInMainMenu();
    }

    /**
     * @return
     * @see ccc.domain.Resource#isLocked()
     */
    public boolean isLocked() {
        return _delegate.isLocked();
    }

    /**
     * @return
     * @see ccc.domain.Resource#isPublished()
     */
    public boolean isPublished() {
        return _delegate.isPublished();
    }

    /**
     * @return
     * @see ccc.domain.Resource#lockedBy()
     */
    public User lockedBy() {
        return _delegate.lockedBy();
    }

    /**
     * @return
     * @see ccc.domain.Resource#metadata()
     */
    public Map<String, String> metadata() {
        return _delegate.metadata();
    }

    /**
     * @return
     * @see ccc.domain.Resource#parent()
     */
    public Folder parent() {
        return _delegate.parent();
    }

    /**
     * @return
     * @see ccc.domain.Resource#publishedBy()
     */
    public User publishedBy() {
        return _delegate.publishedBy();
    }

    /**
     * @return
     * @see ccc.domain.Resource#root()
     */
    public Resource root() {
        return _delegate.root();
    }

    /** {@inheritDoc} */
    public Set<String> tags() {
        return _delegate.tags();
    }

    /**
     * @return
     * @see ccc.domain.Resource#tagString()
     */
    public String tagString() {
        return _delegate.tagString();
    }

    /** {@inheritDoc} */
    public String title() {
        return _delegate.title();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     * @see @link ccc.domain.Resource#absolutePath()
     */
    public ResourcePath absolutePath() {
        return _delegate.absolutePath();
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param parameters
     * @param _search
     * @param reader
     * @param _dm
     * @return
     */
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataManager dm) {
        throw new NotFoundException();
    }

    /** {@inheritDoc} */
    @Override
    public List<IResourceSnapshot> selectPathElements() {

            final List<IResourceSnapshot> elements =
                new ArrayList<IResourceSnapshot>();

            ResourceSnapshot current = this;

            elements.add(current);
            while (current.parent() != null) {
                current = current.parent().forCurrentRevision();
                elements.add(current);
            }
            Collections.reverse(elements);
            return elements;
    }
}
