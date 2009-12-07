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
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.CreateActionDialog;

/**
 * Create an action.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenCreateActionAction
    implements
        Action {

    private SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param ssm The selection model.
     */
    public OpenCreateActionAction(final SingleSelectionModel ssm) {
        _ssm = ssm;
    }

    /** {@inheritDoc} */
    public void execute() {
        new CreateActionDialog(_ssm.tableSelection().getId()).show();
    }
}
