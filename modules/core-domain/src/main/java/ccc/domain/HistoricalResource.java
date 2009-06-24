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

    private List<T> _history = new ArrayList<T>();

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
        return revision(_history.size()-1);
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
        _history.add(revision);
    }
}
