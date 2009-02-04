/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.DisposingCallback;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MetadataDialog
    extends
        TableDataDisplayDialog<Map.Entry<String, String>> {

    private CheckBoxSelectionModel<ModelData> _sm;
    private String _resourceId;

    private final Button _save = new Button(
        constants().save(),
        new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {
                commands().updateProperties(
                    _resourceId,
                    currentMetadata(),
                    new DisposingCallback(MetadataDialog.this));
            }
        });


    /**
     * Constructor.
     *
     * @param resourceId
     * @param data
     */
    public MetadataDialog(final String resourceId,
                          final Collection<Map.Entry<String, String>> data) {
        super("Metadata", data); // I18n

        _resourceId = resourceId;

        _dataStore.add(DataBinding.bindMetadata(_data));
        _grid.setAutoExpandColumn("value");
        _grid.setSelectionModel(_sm);
        _grid.addPlugin(_sm);

        addToolbar();
        addButton(_cancel);
        addButton(_save);
    }


    /** {@inheritDoc} */
    @Override
    protected ColumnModel defineColumnModel() {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _sm = new CheckBoxSelectionModel<ModelData>();
        configs.add(_sm.getColumn());

        final ColumnConfig keyColumn = new ColumnConfig("key", "Key", 100);
        final TextField<String> keyField = new TextField<String>();
        keyField.setAllowBlank(false);
        keyField.setAutoValidate(true);
        keyColumn.setEditor(new CellEditor(keyField));
        configs.add(keyColumn);

        final ColumnConfig valueColumn = new ColumnConfig();
        valueColumn.setId("value");
        valueColumn.setHeader("Value");
        final TextField<String> valueField = new TextField<String>();
        valueField.setAllowBlank(false);
        valueField.setAutoValidate(true);
        valueColumn.setEditor(new CellEditor(valueField));
        configs.add(valueColumn);

        return new ColumnModel(configs);
    }


    private void addToolbar() {

        final ToolBar toolBar = new ToolBar();

        final TextToolItem add = new TextToolItem("New");
        add.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override public void componentSelected(final ToolBarEvent ce) {
                final ModelData datum = new BaseModelData();
                datum.set("key", "");
                datum.set("value", "");
                _grid.stopEditing();
                _dataStore.insert(datum, 0);
                _grid.startEditing(0, 0);
            }
        });
        toolBar.add(add);

        final TextToolItem remove = new TextToolItem("Remove");
        remove.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override public void componentSelected(final ToolBarEvent ce) {
                _grid.stopEditing();
                for (final ModelData item
                        : _grid.getSelectionModel().getSelectedItems()) {
                    _dataStore.remove(item);
                }
            }
        });
        toolBar.add(remove);

        setTopComponent(toolBar);
    }

    private Map<String, String> currentMetadata() {
        final Map<String, String> metadata = new HashMap<String, String>();

        for (final ModelData item : _dataStore.getModels()) {
            metadata.put(item.<String>get("key"), item.<String>get("value"));
        }

        return metadata;
    }
}
