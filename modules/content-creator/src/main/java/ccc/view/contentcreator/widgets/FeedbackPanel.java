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

import java.util.List;

import ccc.view.contentcreator.client.Application;
import ccc.view.contentcreator.controls.CompositeControl;
import ccc.view.contentcreator.controls.PanelControl;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class FeedbackPanel implements CompositeControl {

    private final Application _app;
    private final PanelControl _vPanel;

    public FeedbackPanel(final Application app) {
        _app = app;
        _vPanel = _app.verticalPanel();
    }

    public void displayErrors(final List<String> errors) {
        _vPanel.clear();
        _vPanel.setStyleName("gwt-FeedbackPanel-error");
        _vPanel.add(_app.label("One or more errors occurred:"));
        for (final String error : errors) {
            _vPanel.add(_app.label(" * "+error));
        }
    }

    /** {@inheritDoc} */
    public void setHeight(final String height) {
        _vPanel.setHeight(height);
    }

    /** {@inheritDoc} */
    public void setWidth(final String width) {
        _vPanel.setWidth(width);
    }

    /** {@inheritDoc} */
    public void setVisible(final boolean b) {
        _vPanel.setVisible(b);
    }

    /** {@inheritDoc} */
    public Object rootWidget() {
        return _vPanel;
    }
}
