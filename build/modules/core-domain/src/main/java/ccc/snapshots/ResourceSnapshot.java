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

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.api.Duration;
import ccc.api.ResourceType;
import ccc.domain.Folder;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceSnapshot {
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


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String description() {
        return _delegate.description();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public ResourceName name() {
        return _delegate.name();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public Duration computeCache() {
        return _delegate.computeCache();
    }

    /**
     * @return
     * @see ccc.domain.Resource#dateChanged()
     */
    public Date dateChanged() {
        return _delegate.dateChanged();
    }

    /**
     * @return
     * @see ccc.domain.Resource#dateCreated()
     */
    public Date dateCreated() {
        return _delegate.dateCreated();
    }

    /**
     * @param key
     * @return
     * @see ccc.domain.Resource#getMetadatum(java.lang.String)
     */
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

    /**
     * @return
     * @see ccc.domain.Resource#includeInMainMenu()
     */
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

    /**
     * @return
     * @see ccc.domain.Resource#tags()
     */
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

    /**
     * @return
     * @see ccc.domain.Resource#title()
     */
    public String title() {
        return _delegate.title();
    }
}
