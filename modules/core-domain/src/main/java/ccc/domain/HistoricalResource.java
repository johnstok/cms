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

import java.util.HashMap;
import java.util.Map;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.DBC;
import ccc.types.ResourceName;



/**
 * A resource that records each update made over time.
 *
 * @param <T> The type of revision this class supports.
 * @param <S> The type of delta the revision supports.
 *
 * @author Civic Computing Ltd.
 */
public abstract class HistoricalResource<S, T extends Revision<S>>
    extends
        Resource {

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
