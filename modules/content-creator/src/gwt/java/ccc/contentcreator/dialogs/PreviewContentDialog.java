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
     * @param showWorkingCopy Should the working copy of the specified resource
     *      be shown.
     */
    public PreviewContentDialog(final String resourcePath,
                                final boolean showWorkingCopy) {
        super(Globals.uiConstants().preview());

        _previewFrame.setStyleName("ccc-Frame");
        DOM.setElementPropertyInt(_previewFrame.getElement(), "frameBorder", 0);
        _previewFrame.setUrl(
            _contentServerBaseUrl
            + resourcePath
            + ((showWorkingCopy) ? "?wc" : ""));

        addButton(_cancel);
        add(_previewFrame);
    }
}
