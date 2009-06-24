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
public final class RevisionMetadata {

    private final Date _timestamp;
    private final User _actor;
    private final boolean _majorChange;
    private final String _comment;


    /**
     * Constructor.
     *
     * @param timestamp
     * @param actor
     * @param majorChange
     * @param comment
     */
    public RevisionMetadata(final Date timestamp,
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
    public Date getTimestamp() {
        return _timestamp;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public User getActor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return Returns the majorChange.
     */
    public boolean isMajorChange() {
        return _majorChange;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public String getComment() {
        return _comment;
    }
}
