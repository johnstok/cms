
package ccc.contentcreator.actions;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dialogs.CreateUserDialog;
import ccc.services.api.UserSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateUserAction
    implements
        Action {

    /** {@inheritDoc} */
    public void execute() {
        Globals.queriesService().loggedInUser(
          new ErrorReportingCallback<UserSummary>() {
              public void onSuccess(final UserSummary user) {
                  new CreateUserDialog().show();
              }
          }
      );
    }
}
