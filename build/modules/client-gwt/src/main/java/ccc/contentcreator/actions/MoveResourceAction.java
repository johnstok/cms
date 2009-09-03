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

import com.google.gwt.http.client.RequestBuilder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MoveResourceAction
    extends
        RemotingAction {

    private final UUID _resource;
    private final UUID _parent;


    /**
     * Constructor.
     *
     * @param newParent The new parent folder the resource.
     * @param resource The resource to move.
     */
    public MoveResourceAction(final UUID resource, final UUID newParent) {
        super(UI_CONSTANTS.move(), RequestBuilder.POST);
        _resource = resource;
        _parent = newParent;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/resources/"+_resource+"/parent";
    }


    /** {@inheritDoc} */
    @Override protected String getBody() {
        return _parent.toString();
    }
}
