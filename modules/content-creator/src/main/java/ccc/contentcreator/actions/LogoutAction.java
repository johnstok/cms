
package ccc.contentcreator.actions;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;


/**
 * TODO: Add Description for this type.
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
                    Globals.disableExitConfirmation();
                    Globals.redirectTo(Globals.APP_URL);
                }
            }
        );
    }
}
