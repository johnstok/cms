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

import java.util.ArrayList;
import java.util.List;

import ccc.serialization.Jsonable;
import ccc.types.DBC;
import ccc.types.ResourceName;



/**
 * Abstract helper class that provides working copy support for resources.
 *
 * @author Civic Computing Ltd.
 */
public abstract class WorkingCopySupport<T extends Revision<U>,
                                         U extends Jsonable,
                                         V extends WorkingCopy<U>>
    extends
        HistoricalResource<U, T>
    implements
        WCAware<U> {

    private List<V> _wc = new ArrayList<V>();


    /** Constructor: for persistence only. */
    protected WorkingCopySupport() { super(); }


    /**
     * Constructor.
     *
     * @param title
     */
    WorkingCopySupport(final String title) {
        super(title);
    }


    /**
     * Constructor.
     *
     * @param name
     * @param title
     */
    WorkingCopySupport(final ResourceName name, final String title) {
        super(name, title);
    }


    /** {@inheritDoc} */
    public void clearWorkingCopy() {
        DBC.require().toBeTrue(hasWorkingCopy());
        _wc.clear();
    }


    /** {@inheritDoc} */
    public boolean hasWorkingCopy() {
        return 0!=_wc.size();
    }


    protected V wc() {
        if (0==_wc.size()) {
            return null;
        }
        return _wc.get(0);
    }


    private void wc(final V pageWorkingCopy) {
        DBC.require().toBeFalse(hasWorkingCopy());
        _wc.add(0, pageWorkingCopy);
    }


    /** {@inheritDoc} */
    @Override
    public void applySnapshot(final RevisionMetadata metadata) {
        DBC.require().notNull(wc());
        update(wc().delta(), metadata);
        clearWorkingCopy();
    }


    /** {@inheritDoc} */
    public void workingCopy(final U snapshot) {
        DBC.require().notNull(snapshot);
        if (hasWorkingCopy()) {
            wc().delta(snapshot);
        } else {
            wc(createWorkingCopy(snapshot));
        }
    }

    /**
     * Set the working copy for this resource from an existing revision.
     *
     * @param revisionNumber The revision number to use as the source for the
     *  working copy.
     */
    public void setWorkingCopyFromRevision(final int revisionNumber) {
        workingCopy(revision(revisionNumber).delta());
    }

    /** {@inheritDoc} */
    @Override
    public U workingCopy() {
        if (null!=wc()) {
            return wc().delta();
        }
        return currentRevision().delta();
    }

    /**
     * Create a working copy from a delta.
     *
     * @param delta The delta for the working copy.
     * @return The new working copy.
     */
    protected abstract V createWorkingCopy(U delta);


    /**
     * Update the resource from the specified delta.
     *
     * @param delta The delta to apply.
     * @param metadata The metadata for the update.
     */
    protected abstract void update(final U delta,
                                   final RevisionMetadata metadata);
}
