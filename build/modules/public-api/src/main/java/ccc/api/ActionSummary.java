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
package ccc.api;

import static ccc.api.JsonKeys.*;

import java.io.Serializable;
import java.util.Date;


/**
 * Summary of a CCC scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public final class ActionSummary implements Serializable, Jsonable {

    private ID _id;
    private CommandType _type;
    private Username _actor;
    private Date   _executeAfter;
    private ResourceType _subjectType;
    private String _subjectPath;
    private ActionStatus _status;

    @SuppressWarnings("unused") private ActionSummary() { super(); }

    /**
     * Constructor.
     *
     * @param id The action's id.
     * @param type The type of command the action will perform.
     * @param subjectType The subject's type.
     * @param actor The action's actor.
     * @param after The date after which the action should be executed.
     * @param path The absolute path of the action's subject.
     * @param status The action's status.
     */
    public ActionSummary(final ID     id,
                         final CommandType type,
                         final Username actor,
                         final Date   after,
                         final ResourceType subjectType,
                         final String path,
                         final ActionStatus status) {
        _id = id;
        _type = type;
        _actor = actor;
        _executeAfter = new Date(after.getTime());
        _subjectType = subjectType;
        _subjectPath = path;
        _status = status;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public ID getId() {
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
    public Username getActor() {
        return _actor;
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

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(ID, getId());
        json.set(TYPE, getType().name());
        json.set(ACTOR, getActor().toString());
        json.set(EXECUTE_AFTER, getExecuteAfter());
        json.set(SUBJECT_TYPE, getSubjectType().name());
        json.set(SUBJECT_PATH, getSubjectPath());
        json.set(STATUS, getStatus().name());
    }
}
