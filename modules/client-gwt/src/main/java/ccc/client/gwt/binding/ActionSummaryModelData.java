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
package ccc.client.gwt.binding;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.Action;
import ccc.api.core.ActionSummary;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceType;
import ccc.api.types.URIBuilder;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.client.gwt.events.ActionCancelled;
import ccc.client.gwt.events.ActionCreated;
import ccc.client.gwt.i18n.ActionStatusConstants;
import ccc.client.gwt.i18n.CommandTypeConstants;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.plugins.s11n.JsonKeys;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * {@link ModelData} implementation for the {@link ActionSummary} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionSummaryModelData
    implements
        ModelData {

    private final ActionSummary _as;
    private Globals _globals;

    /**
     * Constructor.
     *
     * @param as The action summary.
     * @param globals The globals.
     */
    public ActionSummaryModelData(final ActionSummary as,
                                  final Globals globals) {
        _as = as;
        _globals = globals;
    }

    /** {@inheritDoc} */
    @Override @SuppressWarnings("unchecked") @Deprecated
    public <X> X get(final String property) {

        final Property p = Property.valueOf(property);

        switch (p) {

            case USERNAME:
                return (X) _as.getActorUsername();

            case EXECUTE_AFTER:
                return (X) _as.getExecuteAfter();

            case ID:
                return (X) _as.getId();

            case PATH:
                return (X) _as.getSubjectPath();

            case STATUS:
                return (X) _as.getStatus();

            case FAILURE_CODE:
                return (X) _as.getFailureCode();

            case LOCALISED_STATUS:
                return (X) getLocalisedStatus();

            case SUBJECT_TYPE:
                return (X) _as.getSubjectType();

            case TYPE:
                return (X) _as.getType();

            case LOCALISED_TYPE:
                return (X) getLocalisedType();

            default:
                throw new UnsupportedOperationException(
                    "Key not supported: "+property);
        }
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<String, Object>();
        for (final Property p : Property.values()) {
            properties.put(p.name(), get(p.name()));
        }
        return properties;
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public Collection<String> getPropertyNames() {
        final Set<String> names = new HashSet<String>();
        for (final Property p : Property.values()) {
            names.add(p.name());
        }
        return names;
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override @Deprecated
    public <X> X set(final String property, final X value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }

    /**
     * Property names.
     *
     * @author Civic Computing Ltd.
     */
    public enum Property {
        /** ID : Property. */
        ID,
        /** TYPE : Property. */
        TYPE,
        /** LOCALISED_TYPE : Property. */
        LOCALISED_TYPE,
        /** USERNAME : Property. */
        USERNAME,
        /** EXECUTE_AFTER : Property. */
        EXECUTE_AFTER,
        /** PATH : Property. */
        PATH,
        /** SUBJECT_TYPE : Property. */
        SUBJECT_TYPE,
        /** STATUS : Property. */
        STATUS,
        /** FAILURE : Property. */
        FAILURE_CODE,
        /** LOCALISED_STATUS : Property. */
        LOCALISED_STATUS;
    }

    /**
     * Accessor.
     *
     * @return The action status.
     */
    public ActionStatus getStatus() {
        return _as.getStatus();
    }

    /**
     * Accessor.
     *
     * @return The action's subject type.
     */
    public ResourceType getSubjectType() {
        return _as.getSubjectType();
    }

    /**
     * Looks up for localised string for the {@link ActionStatus}.
     *
     * @return The localised string or name of the enum if nothing found.
     */
    public String getLocalisedStatus() {
        final ActionStatusConstants types = _globals.actionStatusConstants();

        String local = null;
        try {
            local = types.getString(_as.getStatus().camelCaseName());
        } catch (final MissingResourceException e) {
            local = _as.getStatus().name();
        }
        return local;
    }

    /**
     * Looks up for localised string for the command type.
     *
     * @return The localised string or name of the enum if nothing found.
     */
    public String getLocalisedType() {
        final CommandTypeConstants types = _globals.commandTypeConstants();

        String local = null;
        try {
            local = types.getString(_as.getType().camelCaseName());
        } catch (final MissingResourceException e) {
            local = _as.getType().name();
        }
        return local;
    }

    /**
     * Accessor.
     *
     * @return The ID.
     */
    public UUID getId() {
        return _as.getId();
    }

    /**
     * Mutator.
     *
     * @param status The status.
     */
    public void setStatus(final ActionStatus status) {
        _as.setStatus(status);
    }

    /**
     * Cancel an action.
     *
     * @return The HTTP request to cancel this action.
     */
    public Request cancel() {
        final String path =
            Globals.API_URL
            + new URIBuilder(Action.COLLECTION+Action.CANCEL)
                .replace("id", getId().toString())
                .toString();
        return
            new Request(
                RequestBuilder.POST,
                path,
                "",
                new ActionCancelledCallback(this));
    }

    /**
     * Create a new action.
     *
     * @param subject The action's subject.
     * @param command The command to apply.
     * @param executeAfter The earliest the command can execute.
     * @param params The command's parameters.
     *
     * @return The HTTP request to create the action.
     */
    // FIXME: Should pass an action here.
    public static Request createAction(final UUID subject,
                                       final CommandType command,
                                       final Date executeAfter,
                                       final Map<String, String> params) {
        final String path = Globals.API_URL+Action.COLLECTION;

        final GwtJson json = new GwtJson();
        json.set(JsonKeys.SUBJECT_ID, subject);
        json.set(JsonKeys.COMMAND, command.name());
        json.set(JsonKeys.EXECUTE_AFTER, executeAfter);
        json.set(JsonKeys.PARAMETERS, params);

        return
            new Request(
                RequestBuilder.POST,
                path,
                json.toString(),
                new ActionCreatedCallback());
    }


    /**
     * Callback handler for applying a working copy.
     *
     * @author Civic Computing Ltd.
     */
    public static class ActionCancelledCallback extends ResponseHandlerAdapter {

        private final ActionCancelled _event;

        /**
         * Constructor.
         *
         * @param action The resource whose WC has been applied.
         */
        public ActionCancelledCallback(final ActionSummaryModelData action) {
            super(new GlobalsImpl().uiConstants().cancel());
            _event = new ActionCancelled(action);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final Response response) {
            ContentCreator.EVENT_BUS.fireEvent(_event);
        }
    }


    /**
     * Callback handler for applying a working copy.
     *
     * @author Civic Computing Ltd.
     */
    public static class ActionCreatedCallback extends ResponseHandlerAdapter {

        /**
         * Constructor.
         */
        public ActionCreatedCallback() {
            super(new GlobalsImpl().uiConstants().createAction());
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final Response response) {
            ContentCreator.EVENT_BUS.fireEvent(new ActionCreated());
        }
    }
}
