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

import ccc.view.contentcreator.client.GwtApp;
import ccc.view.contentcreator.controls.CompositeControl;
import ccc.view.contentcreator.controls.PanelControl;

import com.google.gwt.user.client.ui.ClickListener;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class ButtonBar implements CompositeControl {

    private final GwtApp _app;
    private final PanelControl _hPanel;

    public ButtonBar(final GwtApp app) {
        _app = app;
        _hPanel = _app.horizontalPanel();
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
