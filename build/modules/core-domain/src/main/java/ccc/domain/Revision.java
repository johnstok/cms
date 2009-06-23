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
public abstract class Revision extends Entity {
//    private final Date _timestamp;
//    private final User _actor;
    private int _index;
    private boolean _majorChange;
    private String _comment;

    /** Constructor: for persistence only. */
    protected Revision() { super(); }

    /**
     * Constructor.
     *
//     * @param timestamp
//     * @param actor
     * @param index
     * @param majorChange
     * @param comment
     * @param subject
     */
    protected Revision(final int index,
//                    final Date timestamp,
//                    final User actor,
                    final boolean majorChange,
                    final String comment) {
//        _timestamp = timestamp;
//        _actor = actor;
        _index = index;
        _majorChange = majorChange;
        _comment = comment;
    }


//    /**
//     * Accessor.
//     *
//     * @return Returns the timestamp.
//     */
//    public final Date getTimestamp() {
//        return _timestamp;
//    }
//
//
//    /**
//     * Accessor.
//     *
//     * @return Returns the actor.
//     */
//    public final User getActor() {
//        return _actor;
//    }


    /**
     * Accessor.
     *
     * @return Returns the index.
     */
    public final int getIndex() {
        return _index;
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
}
