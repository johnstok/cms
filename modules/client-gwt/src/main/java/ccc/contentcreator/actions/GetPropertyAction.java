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

import ccc.contentcreator.dialogs.LoginDialog;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;



/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GetPropertyAction
    extends
        RemotingAction {

    private final String _key;
    private LoginDialog _dialog;


    /**
     * Constructor.
     *
     * @param key The name of the property to look up.
     * @param dialog The dialog to act on.
     */
    public GetPropertyAction(final String key, final LoginDialog dialog) {
        super(USER_ACTIONS.readProperty(), RequestBuilder.GET, false);
        _key = key;
        _dialog = dialog;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/properties?key="+_key; // FIXME: Escape!
    }

    /** {@inheritDoc} */
    @Override protected void onOK(final Response response) {
        _dialog.setHeading(UI_CONSTANTS.login() +" - "+response.getText());
    }
}
