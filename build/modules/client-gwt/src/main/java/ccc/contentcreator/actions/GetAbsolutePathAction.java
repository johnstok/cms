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

import java.util.UUID;

import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetAbsolutePathAction
    extends
        RemotingAction {

    private final UUID _resourceId;

    /**
     * Constructor.
     *
     * @param actionName The name of the action.
     * @param resourceId The resource's id.
     */
    public GetAbsolutePathAction(final String actionName,
                                 final UUID resourceId) {
        super(actionName);
        _resourceId = resourceId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_resourceId+"/path";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final String path = response.getText();
        execute(path);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param path The path returned.
     */
    protected abstract void execute(String path);
}
