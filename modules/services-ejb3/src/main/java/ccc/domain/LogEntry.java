/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import static ccc.api.types.DBC.*;

import java.util.Date;
import java.util.UUID;

import ccc.api.types.CommandType;


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
    private String       _action;
    private Date         _happenedOn;
    private UUID         _subjectId;
    private String       _detail;
    private boolean      _isSystem;

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
        _action = action.name();
        _happenedOn = new Date(happenedOn.getTime());
        _subjectId = subjectId;
        _detail = detail;
        _isSystem = true;
    }

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
                    final String action,
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
        _isSystem = false;
    }



    /**
     * Accessor.
     *
     * @return The uuid of the resource upon which the action was performed.
     */
    public UUID getSubjectId() {
        return _subjectId;
    }


    /**
     * Accessor.
     *
     * @return The user that performed the action.
     */
    public User getActor() {
        return _actor;
    }


    /**
     * Accessor.
     *
     * @return The action that was performed.
     */
    public String getAction() {
        return _action;
    }


    /**
     * Accessor.
     *
     * @return The index of the log entry in the audit log.
     */
    public long getIndex() {
        return _index;
    }


    /**
     * Accessor.
     *
     * @return The date that the log entry was recorded to the audit log.
     */
    public Date getRecordedOn() {
        return (null==_recordedOn) ? null : new Date(_recordedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return The date that the user performed the action.
     */
    public Date getHappenedOn() {
        return new Date(_happenedOn.getTime());
    }


    /**
     * Accessor.
     *
     * @return Details of the state of the object after the action took place.
     */
    public String getDetail() {
        return _detail;
    }


    /**
     * Accessor.
     *
     * @return Returns true if this is a system log entry, false otherwise.
     */
    public boolean isSystem() {
        return _isSystem;
    }
}
