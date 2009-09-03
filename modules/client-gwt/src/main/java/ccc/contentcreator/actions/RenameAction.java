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

import java.util.UUID;

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
    private final UUID _id;


    /**
     * Constructor.
     * @param name The new name for this resource.
     * @param id The id of the resource to update.
     */
    public RenameAction(final UUID id, final String name) {
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
