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



/**
 * TODO: Add a description for this type.
 * @param <T> The type of revision this class supports.
 *
 * @author Civic Computing Ltd.
 */
public abstract class HistoricalResource<T extends Revision>
    extends
        Resource {

    private int _pageVersion = -1;
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
     * TODO: Add a description for this method.
     *
     * @return
     */
    public final T currentRevision() {
        for (final T r : _history) {
            if (_pageVersion==r.getIndex()) {
                return r;
            }
        }
        throw new RuntimeException("No current revision!");
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param i
     * @return
     */
    public final T revision(final int i) {
        for (final T r : _history) {
            if (i==r.getIndex()) {
                return r;
            }
        }
        throw new RuntimeException("No current revision!");
    }

    protected final int currentVersion() {
        return _pageVersion;
    }

    protected final void incrementVersion() {
        _pageVersion++;
    }

    protected final void addRevision(final T revision) {
        _history.add(revision);
    }
}
