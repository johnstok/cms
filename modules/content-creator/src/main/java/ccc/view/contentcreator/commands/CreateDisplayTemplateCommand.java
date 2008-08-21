/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.view.contentcreator.commands;

import ccc.view.contentcreator.client.Application;
import ccc.view.contentcreator.dialogs.CreateContentTemplateDialog;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateDisplayTemplateCommand extends ApplicationCommand {

    /**
     * Constructor.
     *
     * @param application
     */
    public CreateDisplayTemplateCommand(final Application application) {
        super(application);
    }

    /**
     * {@inheritDoc}
     */
    public void execute() {
        new CreateContentTemplateDialog(_app).center();
    }

}
