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
package ccc.contentcreator.widgets;

import ccc.contentcreator.controls.Application;
import ccc.contentcreator.controls.CompositeControl;
import ccc.contentcreator.controls.Control;
import ccc.contentcreator.controls.GridControl;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class TwoColumnForm implements CompositeControl {

    private final Application      _app;
    private final GridControl _grid;
    private int               _nextRow = 0;

    public TwoColumnForm(final Application app, final int numRows) {
        _app = app;
        _grid = _app.grid(numRows, 2);
    }

    /**
     * Add a new control.
     *
     * @param label
     * @param widget
     * @return
     */
    public TwoColumnForm add(final String label,
                             final Control widget) {

        _grid.setWidget(_nextRow, 0, _app.label(label));
        _grid.setWidget(_nextRow, 1, widget);
        _nextRow++;

        return this;
    }

    /** {@inheritDoc} */
    public void setHeight(final String string) {
        _grid.setHeight(string);
    }

    /** {@inheritDoc} */
    public void setVisible(final boolean b) {
        _grid.setVisible(b);
    }

    /** {@inheritDoc} */
    public void setWidth(final String string) {
        _grid.setWidth(string);
    }

    /** {@inheritDoc} */
    public Object rootWidget() {
        return _grid;
    }
}
