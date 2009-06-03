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

    private PreviewContentDialog(final String url) {
        super(Globals.uiConstants().preview());

        _previewFrame.setStyleName("ccc-Frame");
        DOM.setElementPropertyInt(_previewFrame.getElement(), "frameBorder", 0);
        _previewFrame.setUrl(url);

        addButton(_cancel);
        add(_previewFrame);
    }

    /**
     * Constructor.
     *
     * @param resourcePath The content resource to preview.
     * @param showWorkingCopy Should the working copy of the specified resource
     *      be shown.
     */
    public PreviewContentDialog(final String resourcePath,
                                final boolean showWorkingCopy) {
        this(
            Globals.appURL()
            + resourcePath
            + ((showWorkingCopy) ? "?wc" : ""));
    }

    /**
     * Constructor.
     *
     * @param resourcePath The content resource to preview.
     * @param version The version of the resource to preview.
     */
    public PreviewContentDialog(final String resourcePath, final Long version) {
        this(
            Globals.appURL()
            + resourcePath
            + "?v="
            + version);
    }
}
