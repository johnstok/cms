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

package ccc.contentcreator.client.dialogs;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.client.ButtonBar;
import ccc.contentcreator.client.Globals;

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

    private final UIConstants     _constants = Globals.uiConstants();

    private final VerticalPanel _panel = new VerticalPanel();
    private final Frame _previewFrame = new Frame();
    private final String _contentServerBaseUrl;

    /**
     * Constructor.
     *
     * @param resourcePath The content resource to preview.
     */
    public PreviewContentDialog(final String resourcePath) {
        super(false, true);

        setText(_constants.preview());
        _contentServerBaseUrl = Globals.hostURL()+"server";

        _previewFrame.setWidth("800px");
        _previewFrame.setHeight("600px");
        _previewFrame.setStyleName("ccc-Frame");
        DOM.setElementPropertyInt(getElement(), "frameBorder", 0);
        _previewFrame.setUrl(_contentServerBaseUrl+resourcePath);

        _panel.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);
        _panel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        _panel.add(_previewFrame);
        _panel.add(
            new ButtonBar()
                .add(_constants.cancel(),
                     new ClickListener(){
                        public void onClick(final Widget sender) {
                            hide();
                        }
                    }
                )
            );

        add(_panel);
    }
}
