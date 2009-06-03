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

import ccc.api.ID;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;

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
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;


/**
 * Editable view of a resource's metadata.
 *
 * @author Civic Computing Ltd.
 */
public class MetadataDialog
    extends
        AbstractTableDialog<Map.Entry<String, String>, ModelData> {

    private CheckBoxSelectionModel<ModelData> _sm;
    private ID _resourceId;

    private final Button _save = new Button(
        constants().save(),
        new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {
                final Map<String, String> metadata = currentMetadata();
                final String errors = validate(metadata);
                if (errors.length() == 0) {
                    commands().updateMetadata(
                        _resourceId,
                        metadata,
                        new DisposingCallback(
                            MetadataDialog.this, _constants.updateMetadata()));
                } else {
                    Globals.alert(errors);
                }
            }
        });


    /**
     * Constructor.
     *
     * @param resourceId The id of the resource.
     * @param data The metadata.
     */
    public MetadataDialog(final ID resourceId,
                          final Collection<Map.Entry<String, String>> data) {
        super(Globals.uiConstants().metadata(), data, true);

        _resourceId = resourceId;

        _save.setId("save");

        _dataStore.add(DataBinding.bindMetadata(_data));
        _grid.setId("metadata-grid");
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

        final ColumnConfig keyColumn =
            new ColumnConfig("key", constants().key(), 100);
        final TextField<String> keyField = new TextField<String>();
        keyField.setId("key-editor");
        keyField.setAllowBlank(false);
        keyField.setAutoValidate(true);
        keyColumn.setEditor(new CellEditor(keyField));
        configs.add(keyColumn);

        final ColumnConfig valueColumn = new ColumnConfig();
        valueColumn.setId("value");
        valueColumn.setHeader(constants().value());
        final TextField<String> valueField = new TextField<String>();
        valueField.setId("value-editor");
        valueField.setAllowBlank(false);
        valueField.setAutoValidate(true);
        valueColumn.setEditor(new CellEditor(valueField));
        configs.add(valueColumn);

        return new ColumnModel(configs);
    }


    private void addToolbar() {

        final ToolBar toolBar = new ToolBar();

        toolBar.add(new SeparatorToolItem());

        final TextToolItem add = new TextToolItem(constants().newLabel());
        add.setId("new-metadatum");
        add.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override public void componentSelected(final ToolBarEvent ce) {
                final ModelData datum = new BaseModelData();
                datum.set("key", "");
                datum.set("value", "");
                ((EditorGrid<ModelData>) _grid).stopEditing();
                _dataStore.insert(datum, 0);
                ((EditorGrid<ModelData>) _grid).startEditing(0, 0);
            }
        });
        toolBar.add(add);

        toolBar.add(new SeparatorToolItem());

        final TextToolItem remove = new TextToolItem(constants().remove());
        remove.setId("remove-metadatum");
        remove.addSelectionListener(new SelectionListener<ToolBarEvent>() {
            @Override public void componentSelected(final ToolBarEvent ce) {
                ((EditorGrid<ModelData>) _grid).stopEditing();
                for (final ModelData item
                        : _grid.getSelectionModel().getSelectedItems()) {
                    _dataStore.remove(item);
                }
            }
        });
        toolBar.add(remove);

        toolBar.add(new SeparatorToolItem());

        setTopComponent(toolBar);
    }

    private Map<String, String> currentMetadata() {
        final Map<String, String> metadata = new HashMap<String, String>();

        for (final ModelData item : _dataStore.getModels()) {
            metadata.put(item.<String>get("key"), item.<String>get("value"));
        }

        return metadata;
    }

    private String validate(final Map<String, String> metadata) {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> datum : metadata.entrySet()) {
            if (null==datum.getKey()
                || datum.getKey().trim().length() < 1) {
                sb.append(_constants.noEmptyKeysAllowed());
            }
            if (null==datum.getValue()
                || datum.getValue().trim().length() < 1) {
                sb.append(_constants.noEmptyValuesAllowed());
            }
            if (!datum.getKey().matches("[^<^>]*")) {
                sb.append(_constants.keysMustNotContainBrackets());
            }
            if (!datum.getValue().matches("[^<^>]*")) {
                sb.append(_constants.valuesMustNotContainBrackets());
            }
        }
        return sb.toString();
    }
}
