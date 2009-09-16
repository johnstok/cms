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

import ccc.contentcreator.client.CommandResponseHandler;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Remote action for renaming.
 *
 * @author Civic Computing Ltd.
 */
public class RenameAction
    extends
        RemotingAction {

    private final String _name;
    private final UUID _id;
    private final CommandResponseHandler<Void> _rHandler;


    /**
     * Constructor.
     *
     * @param name The new name for this resource.
     * @param id The id of the resource to update.
     * @param responseHandler The handler for the server response.
     */
    public RenameAction(final UUID id,
                        final String name,
                        final CommandResponseHandler<Void> responseHandler) {
        super(UI_CONSTANTS.rename(), RequestBuilder.POST);
        _name = name;
        _id = id;
        _rHandler = responseHandler;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_id+"/name";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return _name;
    }

    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        _rHandler.onSuccess(null);
    }
}
