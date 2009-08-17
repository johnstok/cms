
package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.dialogs.UpdateCurrentUserDialog;


/**
 * Show current edit dialog for current user's details.
 *
 * @author Civic Computing Ltd.
 */
public final class ShowUpdateCurrentUserAction
    implements
        Action {

    /** {@inheritDoc} */
    @Override public void execute() { new UpdateCurrentUserDialog().show(); }
}
