/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;

import ccc.contentcreator.api.ResourceService;
import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;


/**
 * Global factory class.
 *
 * @author Civic Computing Ltd.
 */
public final class Globals {

    private Globals() { super(); }

    /**
     * Factory for {@link UIConstants} objects.
     *
     * @return A new instance of {@link UIConstants}.
     */
    public static UIConstants uiConstants() {
        return GWT.create(UIConstants.class);
    }

    /**
     * Factory for {@link ResourceServiceAsync} objects.
     *
     * @return A new instance of {@link ResourceServiceAsync}.
     */
    public static ResourceServiceAsync resourceService() {
        return GWT.create(ResourceService.class);
    }

    /**
     * Factory for alert dialogs.
     *
     * @param string The message for the dialog.
     */
    public static void alert(final String string) {
        Window.alert(string);
    }
}
