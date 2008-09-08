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
package ccc.contentcreator.commands;

import ccc.contentcreator.api.Application;
import ccc.contentcreator.client.CreateContentTemplateDialog;


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
