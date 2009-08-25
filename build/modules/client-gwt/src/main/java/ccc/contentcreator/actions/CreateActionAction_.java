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
package ccc.contentcreator.actions;

import java.util.Date;
import java.util.Map;

import ccc.contentcreator.client.GwtJson;
import ccc.serialization.JsonKeys;
import ccc.types.CommandType;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create a scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionAction_
    extends
        RemotingAction {

    private ID _resourceId;
    private CommandType _command;
    private Date _executeAfter;
    private Map<String, String> _actionParameters;


    /**
     * Constructor.
     * @param actionParameters Additional parameters for the action.
     * @param executeAfter The date that the action will be performed.
     * @param command The command the action will invoke.
     * @param resourceId The resource the action will operate on.
     */
    public CreateActionAction_(final ID resourceId,
                               final CommandType command,
                               final Date executeAfter,
                               final Map<String, String> actionParameters) {
        super(UI_CONSTANTS.createAction(), RequestBuilder.POST);
        _resourceId = resourceId;
        _command = command;
        _executeAfter = executeAfter;
        _actionParameters = actionParameters;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/actions";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.SUBJECT_ID, _resourceId);
        json.set(JsonKeys.ACTION, _command.name());
        json.set(JsonKeys.EXECUTE_AFTER, _executeAfter);
        json.set(JsonKeys.PARAMETERS, _actionParameters);
        return json.toString();
    }
}
