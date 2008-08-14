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
import ccc.view.contentcreator.widgets.ButtonBar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Previews a resource.
 *
 * @author Civic Computing Ltd
 */
public class PreviewContentDialog extends DialogBox {

    private final Constants _constants = GWT.create(Constants.class);
    private final String    _title     = _constants.preview();
    private final VerticalPanel _widget = new VerticalPanel();

    private final Frame _previewFrame;
    private final String _contentServerBaseUrl =
        GWT.getHostPageBaseURL()
            .substring(
                0,
                GWT.getHostPageBaseURL().lastIndexOf("content-creator/"))
            +"content-server/content";

    /**
     * Constructor.
     *
     * @param resourcePath The content resource to preview.
     */
    public PreviewContentDialog(final String resourcePath) {
        super(false, true);
        setText(_title);
        setWidget(_widget);
        _widget.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);
        _widget.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        _previewFrame = new Frame(_contentServerBaseUrl+resourcePath);
        _previewFrame.setStyleName("ccc-Frame");
        DOM.setElementPropertyInt(_previewFrame.getElement(), "frameBorder", 0);
        setDefaultDimensions();
        drawGUI();
    }

    private void setDefaultDimensions() {
        /*
         * TODO: Revisit this when GWT 1.5 is released. At present setWidth /
         * setHeight not working correctly for DialogBox class. See issues:
         * http://code.google.com/p/google-web-toolkit/issues/detail?id=2595
         * http://code.google.com/p/google-web-toolkit/issues/detail?id=1424
         */
        _previewFrame.setWidth("800px");
        _previewFrame.setHeight("600px");
    }

    private void drawGUI() {

        _widget.add(_previewFrame);

        _widget.add(
            new ButtonBar()
                .add(
                    _constants.cancel(),
                    new ClickListener() {
                        public void onClick(final Widget sender) {
                            hide();
                        }})
            );
    }
}
