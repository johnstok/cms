
package ccc.contentcreator.actions;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;


/**
 * Log current user out.
 *
 * @author Civic Computing Ltd.
 */
public final class LogoutAction
    implements
        Action {


    /** {@inheritDoc} */
    public void execute() {
        GLOBALS.securityService().logout(
            new ErrorReportingCallback<Void>(UI_CONSTANTS.logout()) {
                public void onSuccess(final Void result) {
                    GLOBALS.currentUser(null);
                    GLOBALS.disableExitConfirmation();
                    GLOBALS.redirectTo(IGlobals.APP_URL);
                }
            }
        );
    }
}
