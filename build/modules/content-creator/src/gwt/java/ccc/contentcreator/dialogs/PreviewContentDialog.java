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

package ccc.contentcreator.dialogs;

import ccc.contentcreator.client.Globals;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Frame;


/**
 * Previews a resource.
 *
 * @author Civic Computing Ltd
 */
public class PreviewContentDialog extends AbstractBaseDialog {

    private final Frame _previewFrame = new Frame();
    private final String _contentServerBaseUrl = Globals.appURL();

    /**
     * Constructor.
     *
     * @param resourcePath The content resource to preview.
     */
    public PreviewContentDialog(final String resourcePath) {
        super(Globals.uiConstants().preview());

        _previewFrame.setStyleName("ccc-Frame");
        DOM.setElementPropertyInt(_previewFrame.getElement(), "frameBorder", 0);
        _previewFrame.setUrl(_contentServerBaseUrl+resourcePath);

        _cancel.setId("cancel");
        addButton(_cancel);
        add(_previewFrame);
    }

    /** _cancel : Button. */
    private final Button _cancel = new Button(
        constants().cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    close();
                }
            }
        );
}
