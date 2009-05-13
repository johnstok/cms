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
package ccc.actions;

import java.util.Date;

import ccc.commons.Exceptions;
import ccc.domain.Entity;
import ccc.domain.Resource;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.services.api.ActionStatus;
import ccc.services.api.ActionType;


/**
 * An action that will be performed for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class Action extends Entity {
    private User         _actor;
    private ActionType   _type;
    private Snapshot     _parameters;
    private Resource     _subject;
    private String       _comment = "";
    private boolean      _isMajorEdit;

    private Date         _executeAfter;
    private ActionStatus _status = ActionStatus.Scheduled;
    private Snapshot     _failure;

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
    public Action(final ActionType type,
                  final Date executeAfter,
                  final User actor,
                  final Resource subject,
                  final Snapshot parameters,
                  final String comment,
                  final boolean isMajorEdit) {
        _type = type;
        _executeAfter = new Date(executeAfter.getTime());
        _actor = actor;
        _subject = subject;
        _parameters = parameters;
        _comment = comment;
        _isMajorEdit = isMajorEdit;
    }


    /**
     * Accessor.
     *
     * @return The type of the action.
     */
    public ActionType type() {
        return _type;
    }

    /**
     * Accessor.
     *
     * @return The parameters for this action.
     */
    public Snapshot parameters() {
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
     * Mark the action as failed.
     *
     * @param e The exception that caused the action to fail.
     */
    public void fail(final Exception e) {
        checkStillScheduled();
        _status = ActionStatus.Failed;
        final Snapshot failure = new Snapshot();
        failure.set("message", Exceptions.rootCause(e).getMessage());
        failure.set("stack", Exceptions.stackTraceFor(e));
        _failure = failure;
    }

    /**
     * Accessor.
     *
     * @return The failure, or NULL if the action hasn't failed.
     */
    public Snapshot failure() {
        return _failure;
    }

    /**
     * Mark the action as cancelled.
     * TODO: Pass the actor who cancelled?
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

    /**
     * Accessor.
     *
     * @return The comment for this action.
     */
    public String getComment() {
        return _comment;
    }

    /**
     * Accessor.
     *
     * @return True if this action is a major edit, false otherwise.
     */
    public boolean isMajorEdit() {
        return _isMajorEdit;
    }
}
