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

package ccc.view.contentcreator.dialogs;

import ccc.view.contentcreator.client.Constants;
import ccc.view.contentcreator.client.GwtApp;
import ccc.view.contentcreator.widgets.ButtonBar;
import ccc.view.contentcreator.widgets.PanelControl;

import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Previews a resource.
 *
 * @author Civic Computing Ltd
 */
public class PreviewContentDialog {

    private final AppDialog     _delegate;
    private final GwtApp        _app;
    private final Constants     _constants;
    private final PanelControl _gui;

    private final Control _previewFrame;
    private final String _contentServerBaseUrl;

    /**
     * Constructor.
     *
     * @param app The application for this dialog.
     * @param resourcePath The content resource to preview.
     */
    public PreviewContentDialog(final GwtApp app, final String resourcePath) {

        _app = app;
        _constants = _app.constants();
        _delegate = _app.dialog(_constants.preview());
        _contentServerBaseUrl = _app.hostURL()+"content-server/content";

        _previewFrame = _app.frame(_contentServerBaseUrl+resourcePath);
        _previewFrame.setWidth("800px");
        _previewFrame.setHeight("600px");

        _gui = _app.verticalPanel();
        _gui.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);
        _gui.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        _gui.add(_previewFrame);
        _gui.add(
            new ButtonBar(_app)
                .add(_constants.cancel(),
                     new HidingClickListener(_delegate)));

        _delegate.gui(_gui);
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    public void center() {
        _delegate.center();
    }
}
