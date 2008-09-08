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
package ccc.contentcreator.widgets;

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.CompositeControl;
import ccc.contentcreator.api.PanelControl;

import com.google.gwt.user.client.ui.ClickListener;


/**
 * A series of buttons in a horizontal row.
 *
 * @author Civic Computing Ltd
 */
public class ButtonBar implements CompositeControl {

    private final Application _app;
    private final PanelControl _hPanel;

    /**
     * Constructor.
     *
     * @param app A reference to the application.
     */
    public ButtonBar(final Application app) {
        _app = app;
        _hPanel = _app.horizontalPanel();
    }

    /**
     * Add a new button.
     *
     * @param buttonTitle The title of the button.
     * @param clickListener The action the button will perform.
     * @return 'this' to allow a fluent API.
     */
    public ButtonBar add(final String buttonTitle,
                         final ClickListener clickListener) {

        _hPanel.add(_app.button(buttonTitle, clickListener));
        return this;
    }

    /** {@inheritDoc} */
    public void setHeight(final String height) {
        _hPanel.setHeight(height);
    }

    /** {@inheritDoc} */
    public void setWidth(final String width) {
        _hPanel.setWidth(width);
    }

    /** {@inheritDoc} */
    public void setVisible(final boolean b) {
        _hPanel.setVisible(b);
    }

    /** {@inheritDoc} */
    public Object rootWidget() {
        return _hPanel;
    }
}
