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
package ccc.services.api;

import java.io.Serializable;
import java.util.Date;


/**
 * Summary of a CCC scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public final class ActionSummary implements Serializable {

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
     * @param id
     * @param type
     * @param actor
     * @param after
     * @param type
     * @param path
     * @param status
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
        _executeAfter = after;
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
        return _executeAfter;
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
}
