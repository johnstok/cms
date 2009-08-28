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

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for renaming.
 *
 * @author Civic Computing Ltd.
 */
public class RenameAction
    extends
        RemotingAction {

    private final String _name;
    private final ID _id;


    /**
     * Constructor.
     * @param name The new name for this resource.
     * @param id The id of the resource to update.
     */
    public RenameAction(final ID id, final String name) {
        super(UI_CONSTANTS.rename(), RequestBuilder.POST);
        _name = name;
        _id = id;
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
}
