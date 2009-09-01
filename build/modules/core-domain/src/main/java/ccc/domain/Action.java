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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.types.ActionStatus;
import ccc.types.CommandType;
import ccc.types.FailureCode;


/**
 * An action that will be performed for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class Action extends Entity {

    private User                _actor;
    private CommandType         _type;
    private Map<String, String> _parameters;
    private Resource            _subject;
    private Date                _executeAfter;
    private FailureCode         _code;
    private ActionStatus        _status = ActionStatus.Scheduled;
    private Map<String, String> _params = new HashMap<String, String>();

    /** Constructor: for persistence only. */
    protected Action() { super(); }

    /**
     * Constructor.
     *
     * @param type The type of action that will be performed.
     * @param executeAfter The earliest the action may be executed.
     * @param actor The user that scheduled the action.
     * @param subject The resource the action will operate on.
     * @param parameters Additional parameters required by the action.
     */
    public Action(final CommandType type,
                  final Date executeAfter,
                  final User actor,
                  final Resource subject,
                  final Map<String, String> parameters) {
        _type = type;
        _executeAfter = new Date(executeAfter.getTime());
        _actor = actor;
        _subject = subject;
        _parameters = parameters;
    }


    /**
     * Accessor.
     *
     * @return The type of the action.
     */
    public CommandType type() {
        return _type;
    }

    /**
     * Accessor.
     *
     * @return The parameters for this action.
     */
    public Map<String, String> parameters() {
        return _parameters;
    }

    /**
     * Accessor.
     *
     * @return The user that scheduled the action.
     */
    public User actor() {
        return _actor;
    }

    /**
     * Accessor.
     *
     * @return The earliest the action may be executed.
     */
    public Date executeAfter() {
        return new Date(_executeAfter.getTime());
    }

    /**
     * Mark the action as completed.
     */
    public void complete() {
        checkStillScheduled();
        _status = ActionStatus.Complete;
    }

    /**
     * Accessor.
     *
     * @return The status of the action.
     */
    public ActionStatus status() {
        return _status;
    }

    /**
     * Accessor.
     *
     * @return Returns the code.
     */
    public FailureCode getCode() {
        return _code;
    }

    /**
     * Accessor.
     *
     * @return Returns the params.
     */
    public Map<String, String> getParams() {
        return _params;
    }

    /**
     * Mark the action as failed.
     *
     * @param e The exception that caused the action to fail.
     */
    public void fail(final CommandFailedException e) {
        checkStillScheduled();
        _status = ActionStatus.Failed;
        _code = e.getCode();
        _params = e.getFailure().getParams();
    }


    /**
     * Mark the action as cancelled.
     */
    public void cancel() {
        checkStillScheduled();
        _status = ActionStatus.Cancelled;
    }

    /**
     * Accessor.
     *
     * @return The resource the action will operate on.
     */
    public Resource subject() {
        return _subject;
    }

    private void checkStillScheduled() {
        if (ActionStatus.Scheduled!=_status) {
            throw new IllegalStateException("Status is "+_status);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.ACTOR, actor().id().toString());
        json.set(JsonKeys.TYPE, type().name());
        json.set(JsonKeys.PARAMETERS, parameters());
        json.set(JsonKeys.SUBJECT_ID, subject().id().toString());
        json.set(JsonKeys.EXECUTE_AFTER, executeAfter());
        json.set(JsonKeys.STATUS, status().name());
        json.set(JsonKeys.CODE, (null==_code) ? null : _code.name());
        json.set("failure-params", _params);
    }
}
