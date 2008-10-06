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

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


/**
 * A panel with two columns - useful for displaying forms.
 *
 * @author Civic Computing Ltd
 */
public class TwoColumnForm extends Grid {

    private int _nextRow = 0;

    /**
     * Constructor.
     *
     * @param numRows The number of rows in the table.
     */
    public TwoColumnForm(final int numRows) {
        super(numRows, 2);
    }

    /**
     * Add a new row.
     *
     * @param label The label for the widget.
     * @param widget The widget itself.
     * @return Returns 'this' for method chaining.
     */
    public TwoColumnForm add(final String label,
                             final Widget widget) {

        setWidget(_nextRow, 0, new Label(label));
        setWidget(_nextRow, 1, widget);
        _nextRow++;

        return this;
    }

}
