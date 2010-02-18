/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views.gxt;

import java.util.Collection;

import ccc.contentcreator.core.Globals;

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
 * @param <T> The type of data this table will render.
 * @param <S> The model data implementation used by the data store.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractTableDialog<T, S extends ModelData>
    extends
        AbstractBaseDialog {

    private final Collection<T> _data;
    private final ListStore<S> _dataStore = new ListStore<S>();
    private final Grid<S> _grid;

    /**
     * Constructor.
     *
     * @param title The title of the dialog.
     * @param globals The globals for this dialog.
     * @param data The data the dialog should display.
     * @param editable EditorGrid is used if editable is true.
     */
    public AbstractTableDialog(final String title,
                               final Globals globals,
                               final Collection<T> data,
                               final boolean editable) {

        super(title, globals);

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


    /**
     * Accessor.
     *
     * @return Returns the data.
     */
    protected Collection<T> getData() {
        return _data;
    }


    /**
     * Accessor.
     *
     * @return Returns the dataStore.
     */
    protected ListStore<S> getDataStore() {
        return _dataStore;
    }


    /**
     * Accessor.
     *
     * @return Returns the grid.
     */
    protected Grid<S> getGrid() {
        return _grid;
    }


    /**
     * Create the column model for the GXT grid.
     *
     * @return A fully initialised column model.
     */
    protected abstract ColumnModel defineColumnModel();
}
