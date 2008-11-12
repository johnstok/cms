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
package ccc.contentcreator.client.dialogs;

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.api.JsonModelData;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class TableDataDisplayDialog
    extends
        AbstractBaseDialog {

    private final List<JsonModelData> _data;
    private final ListStore<JsonModelData> _dataStore =
        new ListStore<JsonModelData>();

    /**
     * Constructor.
     *
     * @param title
     */
    public TableDataDisplayDialog(final String title,
                                  final List<JsonModelData> data) {

        super(title);

        _data = data;
        _dataStore.add(_data);

        setLayout(new FitLayout());

        final List<TableColumn> columns = new ArrayList<TableColumn>();

        TableColumn col = new TableColumn("action", "Action", 0.1f);
        columns.add(col);

        col = new TableColumn("happenedOn", "Happened On", 0.1f);
        columns.add(col);

        final TableColumnModel cm = new TableColumnModel(columns);

        final Table tbl = new Table(cm);
        tbl.setSelectionMode(SelectionMode.SINGLE);
        tbl.setHorizontalScroll(true);
        tbl.setBorders(false);

        final TableBinder<JsonModelData> binder =
            new TableBinder<JsonModelData>(tbl, _dataStore) {

            /** {@inheritDoc} */
            @Override
            protected TableItem createItem(final JsonModelData model) {

                TableItem ti = super.createItem(model);
//                ti.setId(model.getName());
                return ti;
            }
        };
        binder.init();

        add(tbl);

//        _cancel.setId("cancel");
//        addButton(_cancel);
    }


    /** _cancel : Button. */
    protected final Button _cancel = new Button(
            _constants.cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    hide();
                }
            }
        );
}
