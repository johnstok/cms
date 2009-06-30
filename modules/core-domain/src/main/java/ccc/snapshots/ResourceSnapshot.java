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

import ccc.api.Duration;
import ccc.api.ResourceType;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;


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
}
