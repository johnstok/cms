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
package ccc.contentcreator.dialogs;

import java.util.Collection;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Generic dialog for displaying read-only, tabular data.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractTableDialog<T,S extends ModelData>
    extends
        AbstractBaseDialog {

    protected final Collection<T> _data;
    protected final ListStore<S> _dataStore = new ListStore<S>();
    protected final Grid<S> _grid;

    /**
     * Constructor.
     *
     * @param title The title of the dialog.
     * @param data The data the dialog should display.
     */
    public AbstractTableDialog(final String title,
                               final Collection<T> data,
                               final boolean editable) {

        super(title);

        _data = data;

        setLayout(new FitLayout());

        final ColumnModel cm = defineColumnModel();

        if (editable) {
            _grid = new EditorGrid<S>(_dataStore, cm);
        } else {
            _grid = new Grid<S>(_dataStore, cm);
        }
        _grid.setId(title);
        _grid.setBorders(false);
        _grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        add(_grid);
    }

    protected abstract ColumnModel defineColumnModel();
}
