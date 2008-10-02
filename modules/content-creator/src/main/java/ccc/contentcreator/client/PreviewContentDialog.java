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

import ccc.contentcreator.api.Application;
import ccc.contentcreator.api.Control;
import ccc.contentcreator.api.PanelControl;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.DisposingClickListener;

import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Previews a resource.
 *
 * @author Civic Computing Ltd
 */
public class PreviewContentDialog {

    private final ApplicationDialog     _delegate;
    private final Application        _app;
    private final UIConstants     _constants;
    private final PanelControl _gui;

    private final Control _previewFrame;
    private final String _contentServerBaseUrl;

    /**
     * Constructor.
     *
     * @param app The application for this dialog.
     * @param resourcePath The content resource to preview.
     */
    public PreviewContentDialog(final Application app,
                                final String resourcePath) {

        _app = app;
        _constants = _app.constants();
        _delegate = _app.dialog(_constants.preview());
        _contentServerBaseUrl = _app.hostURL()+"content-server";

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
                     new DisposingClickListener(_delegate)));

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
