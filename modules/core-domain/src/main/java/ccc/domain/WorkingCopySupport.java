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
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class WorkingCopySupport<T,U extends Revision>
    extends
        HistoricalResource<U>
    implements
        WCAware<T> {



    /** Constructor: for persistence only. */
    protected WorkingCopySupport() { super(); }

    /**
     * Constructor.
     *
     * @param name
     * @param title
     */
    WorkingCopySupport(final ResourceName name, final String title) {
        super(name, title);
    }

    /**
     * Constructor.
     *
     * @param title
     */
    WorkingCopySupport(final String title) {
        super(title);
    }

    public abstract void setWorkingCopyFromRevision(final int revisionNumber);
}
