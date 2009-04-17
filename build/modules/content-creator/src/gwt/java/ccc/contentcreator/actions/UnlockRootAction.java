/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 1090 $
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.services.api.ResourceSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UnlockRootAction
    implements
        Action {

    private final CommandServiceAsync _commands = Globals.commandService();

    private final String _id;

    /**
     * Constructor.
     *
     * @param id The root id.
     */
    public UnlockRootAction(final String id) {
        _id = id;
    }

    /** {@inheritDoc} */
    public void execute() {
        _commands.unlock(
            _id,
            new ErrorReportingCallback<ResourceSummary>(){
                public void onSuccess(final ResourceSummary arg0) {
                   Globals.alert("Done");
                }
            }
        );
    }

}
