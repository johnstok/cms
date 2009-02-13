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

import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;


/**
 * Updates the sort order for a folder.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFolderSortOrderDialog
    extends
        AbstractEditDialog {

    private final CommandServiceAsync _commands = Globals.commandService();
    private final ComboBox<ModelData> _sortOrder = new ComboBox<ModelData>();

    private final SingleSelectionModel _selectionModel;
    private final ListStore<ModelData> _sortStore = new ListStore<ModelData>();


    /**
     * Constructor.
     *
     * @param ssm
     * @param currentSortOrder The current sort order.
     */
    public UpdateFolderSortOrderDialog(final SingleSelectionModel ssm,
                                       final String currentSortOrder) {
        super(Globals.uiConstants().folderSortOrder());
        setHeight(Globals.DEFAULT_MIN_HEIGHT);

        _selectionModel = ssm;

        populateSortOptions();

        configureComboBox(currentSortOrder,
                          "folder-sort-order",
                          false,
                          constants().folderSortOrder());

        setCurrentValue(currentSortOrder);
    }

    private void setCurrentValue(final String currentValue) {
        for (final ModelData md : _sortStore.getModels()) {
            if(md.get("value").equals(currentValue)) {
                _sortOrder.setValue(md);
                return;
            }
        }
        throw new RuntimeException("Invalid sort order: "+currentValue);
    }


    private void populateSortOptions() {
        final ModelData nameAlphanumAsc = new BaseModelData();
        nameAlphanumAsc.set("name", "Name - alphanumeric, ascending"); // TODO: I18n
        nameAlphanumAsc.set("value", "NAME_ALPHANUM_ASC");
        _sortStore.add(nameAlphanumAsc);
        final ModelData manual = new BaseModelData();
        manual.set("name", "Manual"); // TODO: I18n
        manual.set("value", "MANUAL");
        _sortStore.add(manual);
    }

    private void configureComboBox(final String value,
                              final String id,
                              final boolean allowBlank,
                              final String label) {
        _sortOrder.setFieldLabel(label);
        _sortOrder.setAllowBlank(allowBlank);
        _sortOrder.setId(id);
        _sortOrder.setDisplayField("name");
        _sortOrder.setTemplate("<tpl for=\".\">"
            +"<div class=x-combo-list-item id=\"{name}\">{name}</div></tpl>");
        _sortOrder.setEditable(false);
        _sortOrder.setStore(_sortStore);
        addField(_sortOrder);
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                final ModelData md = _selectionModel.tableSelection();
                final String order = _sortOrder.getValue().<String>get("value");
                _commands.updateFolderSortOrder(
                    md.<String>get("id"),
                    order,
                    new ErrorReportingCallback<Void>(){
                        public void onSuccess(final Void result) {
                            close();
                            md.set("sortOrder", order);
                        }
                    }
                );
            }
        };
    }
}
