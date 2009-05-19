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
package ccc.domain;

import ccc.api.DBC;
import ccc.api.Json;



/**
 * API for working-copy support.
 *
 * @param <T> The type of the working copy.
 *
 * @author Civic Computing Ltd.
 */
public abstract class WorkingCopyAware<T> extends Resource {

    protected T _workingCopy = null;

    /** Constructor. */
    protected WorkingCopyAware() { super(); }

    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     */
    public WorkingCopyAware(final ResourceName name, final String title) {
        super(name, title);
    }

    /**
     * Constructor.
     *
     * @param title The title of the resource.
     */
    public WorkingCopyAware(final String title) {
        super(title);
    }


    /**
     * Clear the current working copy.
     */
    public final void clearWorkingCopy() {
        DBC.require().notNull(_workingCopy);
        _workingCopy = null;
    }


    /**
     * Mutator.
     *
     * @param snapshot The new working copy for this page.
     */
    public final void workingCopy(final T snapshot) {
        DBC.require().notNull(snapshot);
        _workingCopy = snapshot;
    }


    /**
     * Apply a snapshot to this resource.
     */
    public abstract void applySnapshot(); // Should be applyWorkingCopy()


    /**
     * Accessor.
     *
     * @return The current working copy for this page, or a new working copy if
     *  none exists.
     */
    public abstract T workingCopy();

    /**
     * Mutator.
     *
     * @param snapshot The new working copy for this page.
     */
    public abstract void workingCopy(final Json snapshot);
}
