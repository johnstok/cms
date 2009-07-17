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


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class IsLoggedInAction
    extends
        RemotingAction {

    /**
     * Constructor.
     */
    public IsLoggedInAction() {
        super(USER_ACTIONS.internalAction());
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/current";
    }
}
