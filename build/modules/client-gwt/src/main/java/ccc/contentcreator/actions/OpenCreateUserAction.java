
package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.dialogs.CreateUserDialog;


/**
 * Create an user.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateUserAction
    implements
        Action {

    /** {@inheritDoc} */
    @Override public void execute() { new CreateUserDialog().show(); }
}
