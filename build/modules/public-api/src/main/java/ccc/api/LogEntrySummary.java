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
package ccc.api;

import java.io.Serializable;
import java.util.Date;


/**
 * A summary of a log entry.
 *
 * @author Civic Computing Ltd.
 */
public final class LogEntrySummary implements Serializable {
    private ID _subject;
    private CommandType _action;
    private Username _actor;
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
                           final CommandType  action,
                           final Username  actor,
                           final Date    on,
                           final String  comment,
                           final boolean majorEdit,
                           final long    index) {
        _subject = subject;
        _action = action;
        _actor = actor;
        _happenedOn = new Date(on.getTime());
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
    public CommandType getAction() {
        return _action;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public Username getActor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return Returns the happenedOn.
     */
    public Date getHappenedOn() {
        return new Date(_happenedOn.getTime());
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
