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


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ComputeTemplateAction
    extends
        RemotingAction {

    private final ID _id;

    /**
     * Constructor.
     *
     * @param actionName The name of this action.
     * @param id The id of the resource.
     */
    public ComputeTemplateAction(final String actionName, final ID id) {
        super(actionName);
        _id = id;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() { return "/resources/"+_id+"/template"; }
}
