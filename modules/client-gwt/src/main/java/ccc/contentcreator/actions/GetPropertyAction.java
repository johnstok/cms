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

import ccc.contentcreator.dialogs.AboutDialog;
import ccc.contentcreator.dialogs.LoginDialog;

import com.google.gwt.http.client.RequestBuilder;



/**
 * Abstract action for property loading. Implement onOK method for accessing
 * map values. See {@link LoginDialog} or {@link AboutDialog}.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetPropertyAction
    extends
        RemotingAction {


    /**
     * Constructor.
     *
     * @param key The name of the property to look up.
     * @param dialog The dialog to act on.
     */
    public GetPropertyAction() {
        super(USER_ACTIONS.readProperties(), RequestBuilder.GET, false);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/allproperties";
    }

}
