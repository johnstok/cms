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

import com.google.gwt.http.client.RequestBuilder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class RenameAction_
    extends
        RemotingAction {

    private final String _name;
    private final ID _id;


    /**
     * Constructor.
     * @param name The new name for this resource.
     * @param id The id of the resource to update.
     */
    public RenameAction_(final ID id, final String name) {
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
