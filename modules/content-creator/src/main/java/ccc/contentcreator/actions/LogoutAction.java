
package ccc.contentcreator.actions;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;


/**
 * Log current user out.
 *
 * @author Civic Computing Ltd.
 */
public final class LogoutAction
    implements
        Action {

    private IGlobals _globals = new IGlobalsImpl();

    /** {@inheritDoc} */
    public void execute() {
        _globals.securityService().logout(
            new ErrorReportingCallback<Void>(UI_CONSTANTS.logout()) {
                public void onSuccess(final Void result) {
                    _globals.currentUser(null);
                    new IGlobalsImpl().disableExitConfirmation();
                    _globals.redirectTo(IGlobals.APP_URL);
                }
            }
        );
    }
}
