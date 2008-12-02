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

import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * A feedback panel displays feedback on a user action.
 *
 * @author Civic Computing Ltd.
 */
public class FeedbackPanel extends VerticalPanel {

    /**
     * Display the specified errors.
     *
     * @param errors The errors as a list of strings.
     */
    public void displayErrors(final List<String> errors) {
        clear();
        setStyleName("gwt-FeedbackPanel-error");
        add(new Label("One or more errors occurred:"));
        for (final String error : errors) {
            add(new Label(" * "+error));
        }
    }
}
