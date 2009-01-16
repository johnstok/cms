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
package ccc.contentcreator.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;


/**
 * A series of buttons in a horizontal row.
 *
 * @author Civic Computing Ltd
 */
public class ButtonBar extends HorizontalPanel {

    /**
     * Add a new button.
     *
     * @param buttonTitle The title of the button.
     * @param clickListener The action the button will perform.
     * @return 'this' to allow a fluent API.
     */
    public ButtonBar add(final String buttonTitle,
                         final ClickListener clickListener) {

        final Button button = new Button(buttonTitle, clickListener);
        button.ensureDebugId(buttonTitle);
        add(button);
        return this;
    }
}
