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

import java.util.HashMap;
import java.util.Map;

import ccc.api.types.DBC;
import ccc.api.types.ResourceName;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;



/**
 * A resource that records each update made over time.
 *
 * @param <T> The type of revision this class supports.
 * @param <S> The type of delta the revision supports.
 *
 * @author Civic Computing Ltd.
 */
public abstract class HistoricalResource<S, T extends RevisionEntity<S>>
    extends
        ResourceEntity {

    private int _currentRev = -1;
    private Map<Integer, T> _history = new HashMap<Integer, T>();


    /** Constructor: for persistence only. */
    protected HistoricalResource() { super(); }


    /**
     * Constructor.
     *
     * @param name The name of the resource.
     * @param title The title of the resource.
     */
    HistoricalResource(final ResourceName name, final String title) {
        super(name, title);
    }


    /**
     * Constructor.
     *
     * @param title The title of the resource.
     */
    HistoricalResource(final String title) {
        super(title);
    }


    /**
     * Retrieve the current revision.
     *
     * @return The revision corresponding to the current version.
     */
    public T currentRevision() {
        return revision(_currentRev);
    }


    /**
     * Retrieve the current revision number.
     *
     * @return The revision number corresponding to the current version.
     */
    public int currentRevisionNo() {
        return _currentRev;
    }


    /**
     * Retrieve the specified revision.
     *
     * @param i The revision number.
     *
     * @return The revision corresponding to the specified version.
     */
    public T revision(final int i) {
        final T rev = _history.get(Integer.valueOf(i));
        if (null==rev) {
            throw new RuntimeException("No revision!");
        }
        return rev;
    }


    /**
     * Add a new revision for this resource.
     *
     * @param revision The revision to add.
     */
    protected void addRevision(final T revision) {
        DBC.require().notNull(revision);

        _currentRev++;
        DBC.require().toBeNull(_history.get(Integer.valueOf(_currentRev)));
        _history.put(Integer.valueOf(_currentRev), revision);
    }


    /**
     * Accessor.
     *
     * @return The revisions for this resource.
     */
    public HashMap<Integer, T> revisions() {
        return new HashMap<Integer, T>(_history);
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.REVISION, Long.valueOf(_currentRev));
    }
}
