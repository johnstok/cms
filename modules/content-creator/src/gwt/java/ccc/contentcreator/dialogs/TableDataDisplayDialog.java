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
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;


/**
 * Generic dialog for displaying read-only, tabular data.
 *
 * @author Civic Computing Ltd.
 */
public abstract class TableDataDisplayDialog<T>
    extends
        AbstractBaseDialog {

    protected final Collection<T> _data;
    protected final ListStore<ModelData> _dataStore = new ListStore<ModelData>();

    /**
     * Constructor.
     *
     * @param title The title of the dialog.
     * @param data The data the dialog should display.
     */
    public TableDataDisplayDialog(final String title,
                                  final Collection<T> data) {

        super(title);

        _data = data;

        setLayout(new FitLayout());

        final TableColumnModel cm = defineColumnModel();

        final Table tbl = new Table(cm);
        tbl.setId(title);
        tbl.setSelectionMode(SelectionMode.SINGLE);
        tbl.setHorizontalScroll(true);
        tbl.setBorders(false);

        final TableBinder<ModelData> binder =
            new TableBinder<ModelData>(tbl, _dataStore);
        binder.init();

        add(tbl);
    }

    protected abstract TableColumnModel defineColumnModel();
}
