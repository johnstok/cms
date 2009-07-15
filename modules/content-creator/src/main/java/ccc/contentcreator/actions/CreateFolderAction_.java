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

import ccc.api.ID;
import ccc.api.ResourceSummary;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateFolderAction_
    extends
        RemotingAction {

    private final String _name;
    private final ID _parentFolder;

    /**
     * Constructor.
     *
     * @param name The folder's name.
     * @param parentFolder The folder's parent folder.
     */
    public CreateFolderAction_(final ID parentFolder, final String name) {
        super(GLOBALS.uiConstants().createFolder(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() { // FIXME: Escape!!
        return "/folders"
        + "?id="+_parentFolder
        + "&n="+_name;
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }

    protected abstract void execute(ResourceSummary folder);
}
