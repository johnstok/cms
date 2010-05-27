/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.api.core;

import java.util.Date;
import java.util.UUID;

import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.FailureCode;
import ccc.api.types.ResourceType;
import ccc.api.types.Username;


/**
 * Summary of a CCC scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public final class ActionSummary extends Res {

    private UUID _id;
    private CommandType _type;
    private Username _actorUsername;
    private Date   _executeAfter;
    private ResourceType _subjectType;
    private String _subjectPath;
    private ActionStatus _status;
    private FailureCode _fCode;

    /**
     * Constructor.
     */
    public ActionSummary() { super(); }

    /**
     * Constructor.
     *
     * @param id The action's id.
     * @param type The type of command the action will perform.
     * @param subjectType The subject's type.
     * @param actorUsername The action's actor.
     * @param after The date after which the action should be executed.
     * @param path The absolute path of the action's subject.
     * @param status The action's status.
     * @param fCode The action's failure code.
     */
    public ActionSummary(final UUID     id,
                         final CommandType type,
                         final Username actorUsername,
                         final Date   after,
                         final ResourceType subjectType,
                         final String path,
                         final ActionStatus status,
                         final FailureCode fCode) {
        _id = id;
        _type = type;
        _actorUsername = actorUsername;
        _executeAfter = new Date(after.getTime());
        _subjectType = subjectType;
        _subjectPath = path;
        _status = status;
        _fCode = fCode;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
        return _id;
    }


    /**
     * Accessor.
     *
     * @return Returns the type.
     */
    public CommandType getType() {
        return _type;
    }


    /**
     * Accessor.
     *
     * @return Returns the actor.
     */
    public Username getActorUsername() {
        return _actorUsername;
    }


    /**
     * Accessor.
     *
     * @return Returns the executeAfter.
     */
    public Date getExecuteAfter() {
        return new Date(_executeAfter.getTime());
    }


    /**
     * Accessor.
     *
     * @return Returns the subjectType.
     */
    public ResourceType getSubjectType() {
        return _subjectType;
    }


    /**
     * Accessor.
     *
     * @return Returns the subjectPath.
     */
    public String getSubjectPath() {
        return _subjectPath;
    }


    /**
     * Accessor.
     *
     * @return Returns the failure code.
     */
    public FailureCode getFailureCode() {
        return _fCode;
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
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final UUID id) {
        _id = id;
    }


    /**
     * Mutator.
     *
     * @param type The type to set.
     */
    public void setType(final CommandType type) {
        _type = type;
    }


    /**
     * Mutator.
     *
     * @param actorUsername The actorUsername to set.
     */
    public void setActorUsername(final Username actorUsername) {
        _actorUsername = actorUsername;
    }


    /**
     * Mutator.
     *
     * @param executeAfter The executeAfter to set.
     */
    public void setExecuteAfter(final Date executeAfter) {
        _executeAfter = executeAfter;
    }


    /**
     * Mutator.
     *
     * @param subjectType The subjectType to set.
     */
    public void setSubjectType(final ResourceType subjectType) {
        _subjectType = subjectType;
    }


    /**
     * Mutator.
     *
     * @param subjectPath The subjectPath to set.
     */
    public void setSubjectPath(final String subjectPath) {
        _subjectPath = subjectPath;
    }


    /**
     * Mutator.
     *
     * @param code The fCode to set.
     */
    public void setFCode(final FailureCode code) {
        _fCode = code;
    }


    /**
     * Link.
     *
     * @return A link to this action.
     */
    public String self() {
        return getLink("self");
    }
}
