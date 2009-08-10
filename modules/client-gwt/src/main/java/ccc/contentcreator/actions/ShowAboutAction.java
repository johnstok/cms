
package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;
import ccc.contentcreator.dialogs.AboutDialog;


/**
 * Show About dialog.
 *
 * @author Civic Computing Ltd.
 */
public final class ShowAboutAction
    implements
        Action {

    /** {@inheritDoc} */
    @Override public void execute() { new AboutDialog().show(); }
}
