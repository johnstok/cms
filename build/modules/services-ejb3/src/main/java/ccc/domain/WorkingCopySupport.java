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
package ccc.domain;

import java.util.ArrayList;
import java.util.List;

import ccc.plugins.s11n.Jsonable;
import ccc.types.DBC;
import ccc.types.ResourceName;



/**
 * Abstract helper class that provides working copy support for resources.
 *
 * @param <T> The type of revision this class requires.
 * @param <U> The type of jsonable this class requires.
 * @param <V> The type of working copy this class requires.
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
     * @param title The resource's title.
     */
    WorkingCopySupport(final String title) {
        super(title);
    }


    /**
     * Constructor.
     *
     * @param name The resource's name.
     * @param title The resource's title.
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


    /**
     * Accessor.
     *
     * @return The current working copy or NULL if no working copy exists.
     */
    public V getWorkingCopy() {
        if (0==_wc.size()) {
            return null;
        }
        return _wc.get(0);
    }


    /**
     * Mutator.
     *
     * @param wc The working copy to set.
     */
    protected void setWorkingCopy(final V wc) {
        DBC.require().toBeFalse(hasWorkingCopy());
        _wc.add(0, wc);
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final RevisionMetadata metadata) {
        DBC.require().notNull(getWorkingCopy());
        update(getWorkingCopy().delta(), metadata);
        clearWorkingCopy();
    }


    /** {@inheritDoc} */
    public void setOrUpdateWorkingCopy(final U snapshot) {
        DBC.require().notNull(snapshot);
        if (hasWorkingCopy()) {
            getWorkingCopy().delta(snapshot);
        } else {
            setWorkingCopy(createWorkingCopy(snapshot));
        }
    }

    /**
     * Set the working copy for this resource from an existing revision.
     *
     * @param revisionNumber The revision number to use as the source for the
     *  working copy.
     */
    public void setWorkingCopyFromRevision(final int revisionNumber) {
        setOrUpdateWorkingCopy(revision(revisionNumber).delta());
    }

    /** {@inheritDoc} */
    @Override
    public U getOrCreateWorkingCopy() {
        if (null!=getWorkingCopy()) {
            return getWorkingCopy().delta();
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
