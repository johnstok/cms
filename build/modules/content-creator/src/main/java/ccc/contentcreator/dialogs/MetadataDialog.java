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
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.Globals;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;


/**
 * Editable view of a resource's metadata.
 *
 * @author Civic Computing Ltd.
 */
public class MetadataDialog extends AbstractBaseDialog {

    private CheckBoxSelectionModel<ModelData> _sm;
    private ID _resourceId;

    private FormPanel _fieldsPanel = new FormPanel();
    private ContentPanel _gridPanel = new ContentPanel();

    private final ListStore<ModelData> _dataStore = new ListStore<ModelData>();
    private final Grid<ModelData> _grid;

    private final TextField<String> _title = new TextField<String>();
    private final TextField<String> _description = new TextField<String>();
    private final TextField<String> _tags = new TextField<String>();

    private final Button _save = new Button(
        constants().save(),
        new SelectionListener<ButtonEvent>(){
            @Override public void componentSelected(final ButtonEvent ce) {
                final Map<String, String> metadata = currentMetadata();
                final String errors = validate(metadata);

                final String tags =
                    (null==_tags.getValue()) ? "" : _tags.getValue();
                if (errors.length() == 0) {
                    commands().updateFullMetaData(
                        _resourceId,
                        _title.getValue(),
                        _description.getValue(),
                        tags,
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
     * @param resource The model data of the resource.
     * @param data The metadata.
     */
    public MetadataDialog(final ResourceSummaryModelData resource,
                          final Collection<Map.Entry<String, String>> data) {
        super(Globals.uiConstants().metadata());

        _resourceId = resource.getId();
        setLayout(new BorderLayout());

        final BorderLayoutData nb =
            new BorderLayoutData(LayoutRegion.NORTH, 100f);
        final BorderLayoutData cb =
            new BorderLayoutData(LayoutRegion.CENTER);

        _save.setId("save");

        _title.setFieldLabel(constants().title());
        _title.setAllowBlank(false);
        _title.setValue(resource.getTitle());

        _description.setFieldLabel(constants().description());
        _description.setAllowBlank(true);
        _description.setValue(resource.getDescription());

        _tags.setFieldLabel(constants().tags());
        _tags.setAllowBlank(true);
        _tags.setId("tags");
        _tags.setValue(resource.getTags());

        _fieldsPanel.add(_title, new FormData("95%"));
        _fieldsPanel.add(_description, new FormData("95%"));
        _fieldsPanel.add(_tags, new FormData("95%"));
        _fieldsPanel.setHeaderVisible(false);
        _fieldsPanel.setBorders(false);


        final ColumnModel cm = defineColumnModel();
        _dataStore.add(DataBinding.bindMetadata(data));
        _grid = new EditorGrid<ModelData>(_dataStore, cm);
        _grid.setId("metadata-grid");
        _grid.setAutoExpandColumn("value");
        _grid.setSelectionModel(_sm);
        _grid.addPlugin(_sm);
        _grid.setId("aBc");
        _grid.setBorders(false);
        _grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        _gridPanel.setHeaderVisible(false);
        _gridPanel.setBorders(false);
        _gridPanel.add(_grid);
        addToolbar();

        add(_fieldsPanel, nb);
        add(_gridPanel, cb);

        addButton(_cancel);
        addButton(_save);
    }




    private ColumnModel defineColumnModel() {

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

        _gridPanel.setTopComponent(toolBar);
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
