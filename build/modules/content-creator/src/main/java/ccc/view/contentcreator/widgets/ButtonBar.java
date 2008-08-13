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
package ccc.view.contentcreator.widgets;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class ButtonBar extends Composite {

    private final HorizontalPanel _hPanel = new HorizontalPanel();

    public ButtonBar() {
        super();
        initWidget(_hPanel);
    }

    /**
     * Add a new button.
     *
     * @param buttonTitle
     * @param clickListener
     * @return
     */
    public ButtonBar add(final String buttonTitle,
                         final ClickListener clickListener) {

        _hPanel.add(new Button(buttonTitle, clickListener));
        return this;
    }
}
