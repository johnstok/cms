/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.core;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.FailureCode;


/**
 * Criteria class for action queries.
 *
 * @author Civic Computing Ltd.
 */
public class ActionCriteria implements Serializable {

    private String _username;
    private CommandType _commandType;
    private ActionStatus _status;
    private FailureCode _failureCode;
    private Date _executeAfter;
    private UUID _subject;

    /**
     * Constructor.
     *
     */
    public ActionCriteria() {
        super();
    }

    /**
     * Constructor.
     *
     * @param username The username.
     * @param commandType The command type.
     * @param status The action status.
     * @param failureCode The failure code.
     * @param executeAfter The execute after date.
     * @param subject The subject UUID.
     */
    public ActionCriteria(final String username,
                          final CommandType commandType,
                          final ActionStatus status,
                          final FailureCode failureCode,
                          final Date executeAfter,
                          final UUID subject) {
        _username = username;
        _commandType = commandType;
        _status = status;
        _failureCode = failureCode;
        _executeAfter = executeAfter;
        _subject = subject;
    }

    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    public String getUsername() {
        return _username;
    }

    /**
     * Mutator.
     *
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        _username = username;
    }

    /**
     * Accessor.
     *
     * @return Returns the type.
     */
    public CommandType getCommandType() {
        return _commandType;
    }

    /**
     * Mutator.
     *
     * @param commandType The type to set.
     */
    public void setCommandType(final CommandType commandType) {
        _commandType = commandType;
    }

    /**
     * Accessor.
     *
     * @return Returns the status.
     */
    public ActionStatus getStatus() {
        return _status;
    }

    /**
     * Mutator.
     *
     * @param status The status to set.
     */
    public void setStatus(final ActionStatus status) {
        _status = status;
    }

    /**
     * Accessor.
     *
     * @return Returns the failure code.
     */
    public FailureCode getFailureCode() {
        return _failureCode;
    }

    /**
     * Mutator.
     *
     * @param failureCode The failure code to set.
     */
    public void setFailureCode(final FailureCode failureCode) {
        _failureCode = failureCode;
    }

    /**
     * Accessor.
     *
     * @return Returns the execute after date.
     */
    public Date getExecuteAfter() {
        return _executeAfter;
    }

    /**
     * Mutator.
     *
     * @param executeAfter The execute after to set.
     */
    public void setExecuteAfter(final Date executeAfter) {
        _executeAfter = executeAfter;
    }

    /**
     * Accessor.
     *
     * @return Returns the subject date.
     */
    public UUID getSubject() {
        return _subject;
    }


    /**
     * Mutator.
     *
     * @param subject The subject to set.
     */
    public void setSubject(final UUID subject) {
        _subject = subject;
    }

}
