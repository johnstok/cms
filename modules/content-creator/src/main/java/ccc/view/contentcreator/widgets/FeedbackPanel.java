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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class FeedbackPanel extends Composite {

    private final VerticalPanel _vPanel = new VerticalPanel();

    public FeedbackPanel() {
        super();
        initWidget(_vPanel);
    }

    public void displayErrors(final List<String> errors) {
        _vPanel.clear();
        _vPanel.setStyleName("gwt-FeedbackPanel-error");
        _vPanel.add(new Label("One or more errors occurred:"));
        for (final String error : errors) {
            _vPanel.add(new Label(" * "+error));
        }
    }
}
