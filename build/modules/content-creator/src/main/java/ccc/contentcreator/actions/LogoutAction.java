
package ccc.contentcreator.actions;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.IGlobalsImpl;


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
        Globals.securityService().logout(
            new ErrorReportingCallback<Void>(UI_CONSTANTS.logout()) {
                public void onSuccess(final Void result) {
                    Globals.currentUser(null);
                    new IGlobalsImpl().disableExitConfirmation();
                    Globals.redirectTo(Globals.APP_URL);
                }
            }
        );
    }
}
