
package ccc.contentcreator.actions;

import ccc.contentcreator.client.Action;

import com.google.gwt.user.client.Window;


/**
 * Display help window.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenHelpAction
    implements
        Action {

    /** {@inheritDoc} */
    public void execute() {
        Window.open("manual/ContentCreatorManual.html",
          "_blank", "resizable=yes,scrollbars=yes,status=no");
    }
}
