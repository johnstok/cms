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


/**
 * A working copy for a resource.
 *
 * @param <T> The type of deltas this working copy handles.
 *
 * @author Civic Computing Ltd.
 */
public abstract class WorkingCopy<T>
    extends
        Entity {

    /** Constructor: for persistence only. */
    protected WorkingCopy() { super(); }

    /**
     * Retrieve the working copy's state.
     *
     * @return A delta describing this working copy's state.
     */
    protected abstract T delta();

    /**
     * Update this working copy.
     *
     * @param delta The delta describing this working copy's state.
     */
    protected abstract void delta(final T delta);
}
