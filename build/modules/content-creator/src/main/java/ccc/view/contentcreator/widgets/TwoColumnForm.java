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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class TwoColumnForm extends Composite {

    private final Grid _grid;
    private int _nextRow = 0;

    /**
     * Constructor.
     *
     * @param numRows
     */
    public TwoColumnForm(final int numRows) {
        super();
        _grid = new Grid(numRows, 2);
        initWidget(_grid);
    }

    /**
     * Add a new control.
     *
     * @param label
     * @param widget
     * @return
     */
    public TwoColumnForm add(final String label,
                         final FocusWidget widget) {

        _grid.setWidget(_nextRow, 0, new Label(label));
        _grid.setWidget(_nextRow, 1, widget);
        _nextRow++;

        return this;
    }
}
