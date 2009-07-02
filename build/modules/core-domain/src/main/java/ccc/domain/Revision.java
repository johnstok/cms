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

import java.util.Date;



/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class Revision<T> extends Entity {
    private Date _timestamp;
    private User _actor;
    private boolean _majorChange;
    private String _comment;

    /** Constructor: for persistence only. */
    protected Revision() { super(); }

    /**
     * Constructor.
     *
     * @param index
     * @param timestamp
     * @param actor
     * @param majorChange
     * @param comment
     */
    protected Revision(final Date timestamp,
                       final User actor,
                       final boolean majorChange,
                       final String comment) {
        _timestamp = timestamp;
        _actor = actor;
        _majorChange = majorChange;
        _comment = comment;
    }


    /**
     * Accessor.
     *
     * @return Returns the timestamp.
     */
    public final Date getTimestamp() {
        return _timestamp;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public final User getActor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return Returns true if the version was a major change, false otherwise.
     */
    public final boolean isMajorChange() {
        return _majorChange;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public final String getComment() {
        return _comment;
    }

    /**
     * Retrieve the revision's state.
     *
     * @return A delta describing this revision's state.
     */
    protected abstract T delta();
}
