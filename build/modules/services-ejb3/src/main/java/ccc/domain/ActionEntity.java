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
package ccc.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccc.api.core.ActionSummary;
import ccc.api.exceptions.CycleDetectedException;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.exceptions.InsufficientPrivilegesException;
import ccc.api.exceptions.LockMismatchException;
import ccc.api.exceptions.ResourceExistsException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.exceptions.UnlockedException;
import ccc.api.exceptions.WorkingCopyNotSupportedException;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.Failure;
import ccc.api.types.FailureCode;
import ccc.api.types.URIBuilder;
import ccc.commons.NormalisingEncoder;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * An action that will be performed for a resource.
 *
 * @author Civic Computing Ltd.
 */
public class ActionEntity extends Entity {

    private UserEntity                _actor;
    private CommandType         _type;
    private Map<String, String> _parameters;
    private ResourceEntity            _subject;
    private Date                _executeAfter;
    private FailureCode         _code;
    private ActionStatus        _status = ActionStatus.SCHEDULED;
    private Map<String, String> _params = new HashMap<String, String>();
    private String              _fId;

    /** Constructor: for persistence only. */
    protected ActionEntity() { super(); }

    /**
     * Constructor.
     *
     * @param type The type of action that will be performed.
     * @param executeAfter The earliest the action may be executed.
     * @param actor The user that scheduled the action.
     * @param subject The resource the action will operate on.
     * @param parameters Additional parameters required by the action.
     */
    public ActionEntity(final CommandType type,
                  final Date executeAfter,
                  final UserEntity actor,
                  final ResourceEntity subject,
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
    public CommandType getType() {
        return _type;
    }

    /**
     * Accessor.
     *
     * @return The parameters for this action.
     */
    public Map<String, String> getParameters() {
        return _parameters;
    }

    /**
     * Accessor.
     *
     * @return The user that scheduled the action.
     */
    public UserEntity getActor() {
        return _actor;
    }

    /**
     * Accessor.
     *
     * @return The earliest the action may be executed.
     */
    public Date getExecuteAfter() {
        return new Date(_executeAfter.getTime());
    }

    /**
     * Mark the action as completed.
     */
    public void complete() {
        checkStillScheduled();
        _status = ActionStatus.COMPLETE;
    }

    /**
     * Accessor.
     *
     * @return The status of the action.
     */
    public ActionStatus getStatus() {
        return _status;
    }

    /**
     * Accessor.
     *
     * @return Returns the failure code.
     */
    public FailureCode getCode() {
        return _code;
    }

    /**
     * Accessor.
     *
     * @return Returns the failure param's.
     */
    public Map<String, String> getParams() {
        return _params;
    }

    /**
     * Accessor.
     *
     * @return Returns the failure Id.
     */
    public String getFailureId() {
        return _fId;
    }

    /**
     * Mark the action as failed.
     *
     * @param f Details of the failure.
     */
    public void fail(final Failure f) {
        checkStillScheduled();
        _status = ActionStatus.FAILED;
        _code = mapCode(f.getCode());
        _params = f.getParams();
        _fId = f.getExceptionId();
    }


    @Deprecated // FIXME: Just use exception class names.
    private FailureCode mapCode(final String code) {
        try {
            final Class<?> failureClass = Class.forName(code);

            if (WorkingCopyNotSupportedException.class==failureClass) {
                return FailureCode.WC_UNSUPPORTED;
            } else if (UnlockedException.class==failureClass) {
                return FailureCode.UNLOCKED;
            } else if (UnauthorizedException.class==failureClass) {
                return FailureCode.PRIVILEGES;
            } else if (ResourceExistsException.class==failureClass) {
                return FailureCode.EXISTS;
            } else if (LockMismatchException.class==failureClass) {
                return FailureCode.LOCK_MISMATCH;
            } else if (InsufficientPrivilegesException.class==failureClass) {
                return FailureCode.PRIVILEGES;
            } else if (EntityNotFoundException.class==failureClass) {
                return FailureCode.NOT_FOUND;
            } else if (CycleDetectedException.class==failureClass) {
                return FailureCode.CYCLE;
            }

            return FailureCode.UNEXPECTED;

        } catch (final ClassNotFoundException e) {
            return FailureCode.UNEXPECTED;
        }
    }

    /**
     * Mark the action as cancelled.
     */
    public void cancel() {
        checkStillScheduled();
        _status = ActionStatus.CANCELLED;
    }

    /**
     * Accessor.
     *
     * @return The resource the action will operate on.
     */
    public ResourceEntity getSubject() {
        return _subject;
    }

    private void checkStillScheduled() {
        if (ActionStatus.SCHEDULED!=_status) {
            throw new IllegalStateException("Status is "+_status);
        }
    }



    /**
     * Create summaries for a list of actions.
     *
     * @param actions The actions.
     * @return The corresponding summaries.
     */
    public static List<ActionSummary> mapActions(
                                     final Collection<ActionEntity> actions) {
        final List<ActionSummary> summaries =
            new ArrayList<ActionSummary>();
        for (final ActionEntity a : actions) {
            summaries.add(a.mapAction());
        }
        return summaries;
    }


    /**
     * Create a summary for an action.
     *
     * @return The corresponding summary.
     */
    public ActionSummary mapAction() {
        final ActionSummary summary =
            new ActionSummary(
                getId(),
                getType(),
                getActor().getUsername(),
                getExecuteAfter(),
                getSubject().getType(),
                getSubject().getAbsolutePath().removeTop().toString(),
                getStatus(),
                (null==getCode()) ? null : getCode());
        summary.addLink(
            "self",
            new URIBuilder(
                    ccc.api.core.ResourceIdentifiers.Action.COLLECTION
                    + ccc.api.core.ResourceIdentifiers.Action.ELEMENT)
                .build("id", getId().toString(), new NormalisingEncoder()));
        return summary;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.ACTOR_ID, getActor().getId());
        json.set(JsonKeys.TYPE, getType().name());
        json.set(JsonKeys.PARAMETERS, getParameters());
        json.set(JsonKeys.SUBJECT_ID, getSubject().getId().toString());
        json.set(JsonKeys.EXECUTE_AFTER, getExecuteAfter());
        json.set(JsonKeys.STATUS, getStatus().name());
        json.set(JsonKeys.CODE, (null==_code) ? null : _code.name());
        json.set("failure-params", _params);
    }
}
