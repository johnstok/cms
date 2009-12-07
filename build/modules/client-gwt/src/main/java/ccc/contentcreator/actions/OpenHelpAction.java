/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
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
