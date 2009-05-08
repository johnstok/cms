/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.api;

import java.io.Serializable;
import java.util.Date;


/**
 * A summary of a log entry.
 *
 * @author Civic Computing Ltd.
 */
public final class LogEntrySummary implements Serializable {
    private ID _subject;
    private Action _action;
    private String _actor;
    private Date _happenedOn;
    private String _comment;
    private boolean _isMajorEdit;
    private long _index;

    @SuppressWarnings("unused") private LogEntrySummary() { super(); }

    /**
     * Constructor.
     *
     * @param subject
     * @param action
     * @param actor
     * @param on
     * @param comment
     * @param majorEdit
     * @param index
     */
    public LogEntrySummary(final ID      subject,
                           final Action  action,
                           final String  actor,
                           final Date    on,
                           final String  comment,
                           final boolean majorEdit,
                           final long    index) {
        _subject = subject;
        _action = action;
        _actor = actor;
        _happenedOn = on;
        _comment = comment;
        _isMajorEdit = majorEdit;
        _index = index;
    }


    /**
     * Accessor.
     *
     * @return Returns the subject.
     */
    public ID getSubject() {
        return _subject;
    }


    /**
     * Accessor.
     *
     * @return Returns the action.
     */
    public Action getAction() {
        return _action;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public String getActor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return Returns the happenedOn.
     */
    public Date getHappenedOn() {
        return _happenedOn;
    }


    /**
     * Accessor.
     *
     * @return Returns the comment.
     */
    public String getComment() {
        return _comment;
    }


    /**
     * Accessor.
     *
     * @return Returns the isMajorEdit.
     */
    public boolean isMajorEdit() {
        return _isMajorEdit;
    }


    /**
     * Accessor.
     *
     * @return Returns the index.
     */
    public long getIndex() {
        return _index;
    }
}
