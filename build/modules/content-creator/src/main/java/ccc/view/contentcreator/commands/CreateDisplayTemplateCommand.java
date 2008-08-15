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

import ccc.view.contentcreator.dialogs.CreateContentTemplateDialog;

import com.google.gwt.user.client.Command;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class CreateDisplayTemplateCommand implements Command {

    /**
     * {@inheritDoc}
     */
    public void execute() {
        new CreateContentTemplateDialog().center();
    }

}
