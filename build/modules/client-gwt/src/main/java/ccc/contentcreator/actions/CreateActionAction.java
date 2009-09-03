/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 1744 $
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2009-08-28 16:17:04 +0100 (Fri, 28 Aug 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.serialization.JsonKeys;
import ccc.types.CommandType;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create a scheduled action.
 *
 * @author Civic Computing Ltd.
 */
public class CreateActionAction
    extends
        RemotingAction {

    private UUID _resourceId;
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
    public CreateActionAction(final UUID resourceId,
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
