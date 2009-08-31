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

import ccc.contentcreator.client.GwtJson;
import ccc.rest.ResourceSummary;
import ccc.serialization.JsonKeys;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateFolderAction
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
    public CreateFolderAction(final ID parentFolder, final String name) {
        super(GLOBALS.uiConstants().createFolder(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentFolder);
        json.set(JsonKeys.NAME, _name);
        return json.toString();
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }

    protected abstract void execute(ResourceSummary folder);
}
