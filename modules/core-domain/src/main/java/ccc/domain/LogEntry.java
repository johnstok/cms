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
package ccc.domain;

import static ccc.api.DBC.*;

import java.util.Date;
import java.util.UUID;

import ccc.api.CommandType;


/**
 * Captures detail relevant to a single user action for persistence in the audit
 * log.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntry extends Entity {

    private long         _index = -1;  // Only available once persisted
    private Date         _recordedOn;  // Only available once persisted

    private User         _actor;
    private CommandType  _action;
    private Date         _happenedOn;
    private UUID         _subjectId;
    private String       _detail;

    /** Constructor: for persistence only. */
    protected LogEntry() { super(); }

    /**
     * Constructor.
     *
     * @param actor The actor that performed the action.
     * @param action The action performed.
     * @param happenedOn When the action took place.
     * @param subjectId The subject of the action.
     * @param detail Details of the action.
     */
    public LogEntry(final User actor,
                    final CommandType action,
                    final Date happenedOn,
                    final UUID subjectId,
                    final String detail) {
        require().notNull(subjectId);
        require().notNull(actor);
        require().notNull(happenedOn);
        require().notNull(action);

        _actor = actor;
        _action = action;
        _happenedOn = new Date(happenedOn.getTime());
        _subjectId = subjectId;
        _detail = detail;
    }


    /**
     * Accessor.
     *
     * @return The uuid of the resource upon which the action was performed.
     */
    public UUID subjectId() {
        return _subjectId;
    }


    /**
     * Accessor.
     *
     * @return The user that performed the action.
     */
    public User actor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return The action that was performed.
     */
    public CommandType action() {
        return _action;
    }


    /**
     * Accessor.
     *
     * @return The index of the log entry in the audit log.
     */
    public long index() {
        return _index;
    }


    /**
     * Accessor.
     *
     * @return The date that the log entry was recorded to the audit log.
     */
    public Date recordedOn() {
        return (null==_recordedOn) ? null : new Date(_recordedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return The date that the user performed the action.
     */
    public Date happenedOn() {
        return new Date(_happenedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return Details of the state of the object after the action took place.
     */
    public String detail() {
        return _detail;
    }
}
