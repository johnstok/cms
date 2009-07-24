
package ccc.contentcreator.actions;

import ccc.contentcreator.client.IGlobals;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Log current user out.
 *
 * @author Civic Computing Ltd.
 */
public final class LogoutAction
    extends
        RemotingAction {


    /**
     * Constructor.
     */
    public LogoutAction() {
        super(UI_CONSTANTS.logout(), RequestBuilder.POST, false);
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        GLOBALS.currentUser(null);
        GLOBALS.disableExitConfirmation();
        GLOBALS.redirectTo(IGlobals.APP_URL);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/current";
    }
}
