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

import ccc.types.ID;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ResourceNameExistsAction
    extends
        RemotingAction {

    private ID _folderId;
    private String _resourceName;

    /**
     * Constructor.
     * @param resourceName The resource name to check.
     * @param folderId The id of the folder to check.
     */
    public ResourceNameExistsAction(final ID folderId,
                                    final String resourceName) {
        super(USER_ACTIONS.checkUniqueResourceName());
        _folderId = folderId;
        _resourceName = resourceName;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            "/folders/"
            + encode(_folderId.toString())
            +"/"
            + encode(_resourceName)
            +"/exists";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final boolean nameExists =
            JSONParser.parse(response.getText()).isBoolean().booleanValue();
        execute(nameExists);
    }

    protected abstract void execute(boolean nameExists);
}
