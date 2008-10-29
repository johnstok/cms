/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client.dialogs;

import ccc.contentcreator.client.DefinitionPanel;
import ccc.contentcreator.client.Globals;

import com.extjs.gxt.ui.client.widget.Window;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageDialog
    extends
        Window {

    public CreatePageDialog() {
        super();
        setWidth(Globals.DEFAULT_WIDTH);
        setHeight(Globals.DEFAULT_HEIGHT);

        setHeading(Globals.uiConstants().createPage());

        add(new DefinitionPanel());
    }
}
