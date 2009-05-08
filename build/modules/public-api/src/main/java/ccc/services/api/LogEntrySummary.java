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
public class LogEntrySummary implements Serializable {
    public String _subject;
    public Action _action;
    public String _actor;
    public Date _happenedOn;
    public String _comment;
    public boolean _isMajorEdit;
    public long _index;

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
    public LogEntrySummary(final String  subject,
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
}
