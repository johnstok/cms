
package ccc.contentcreator.actions;

import ccc.api.UserSummary;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.dialogs.CreateUserDialog;


/**
 * Create an user.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateUserAction
    implements
        Action {

    /** {@inheritDoc} */
    public void execute() {
        _qs.loggedInUser(
          new ErrorReportingCallback<UserSummary>(UI_CONSTANTS.createUser()) {
              public void onSuccess(final UserSummary user) {
                  new CreateUserDialog().show();
              }
          }
      );
    }
}
