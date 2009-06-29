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

import ccc.api.DBC;



/**
 * TODO: Add a description for this type.
 * @param <T> The type of revision this class supports.
 *
 * @author Civic Computing Ltd.
 */
public abstract class HistoricalResource<T extends Revision>
    extends
        Resource {
    private static final int WC_REV_NO = -1;

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
    public final T currentRevision() {
        return revision(_currentRev);
    }

    /**
     * Retrieve the specified revision.
     *
     * @return The revision corresponding to the specified version.
     */
    public final T revision(final int i) {
        final T rev = _history.get(i);
        if (null==rev) {
            throw new RuntimeException("No revision!");
        }
        return rev;
    }

    protected final void addRevision(final T revision) {
        DBC.require().notNull(revision);

        _currentRev++;
        DBC.require().toBeNull(_history.get(_currentRev));
        _history.put(Integer.valueOf(_currentRev), revision);
    }
}
