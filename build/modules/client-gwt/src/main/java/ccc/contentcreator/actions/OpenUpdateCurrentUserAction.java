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
import ccc.contentcreator.dialogs.UpdateCurrentUserDialog;


/**
 * Show current edit dialog for current user's details.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenUpdateCurrentUserAction
    implements
        Action {

    /** {@inheritDoc} */
    @Override public void execute() { new UpdateCurrentUserDialog().show(); }
}
