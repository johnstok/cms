package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.i18n.UIConstants;
import ccc.client.validation.Validate;
import ccc.client.validation.Validator;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;


/**
 * Metadata grid panel.
 *
 * @author Civic Computing Ltd.
 */
public class MetadataGrid extends ContentPanel {
    private static final int GRID_WIDTH = 610;
    private static final int GRID_HEIGHT = 270;
    private final UIConstants _constants = I18n.UI_CONSTANTS;
    private final Grid<ModelData> _grid;
    private final ListStore<ModelData> _dataStore = new ListStore<ModelData>();
    private CheckBoxSelectionModel<ModelData> _sm;

    /**
     * Constructor.
     *
     * @param data Metadata.
     */
    public MetadataGrid(final Collection<Map.Entry<String, String>> data) {
        _dataStore.add(DataBinding.bindMetadata(data));
        final ColumnModel cm = defineColumnModel();

        setBodyBorder(false);
        setBorders(true);
        setHeaderVisible(false);
        setLayout(new FitLayout());

        _grid = new EditorGrid<ModelData>(_dataStore, cm);
        _grid.setId("metadata-grid");
        _grid.setAutoExpandColumn("value");
        _grid.setSelectionModel(_sm);
        _grid.addPlugin(_sm);
        _grid.setBorders(false);
        _grid.setHeight(GRID_HEIGHT);
        _grid.setWidth(GRID_WIDTH);
        _grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addToolbar();
        add(_grid);

    }

    private ColumnModel defineColumnModel() {

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        _sm = new CheckBoxSelectionModel<ModelData>();
        configs.add(_sm.getColumn());

        final ColumnConfig keyColumn =
            new ColumnConfig("key", _constants.key(), 100);
        final TextField<String> keyField = new TextField<String>();
        keyField.setId("key-editor");
        keyField.setAllowBlank(false);
        keyField.setAutoValidate(true);
        keyColumn.setEditor(new CellEditor(keyField));
        configs.add(keyColumn);

        final ColumnConfig valueColumn = new ColumnConfig();
        valueColumn.setId("value");
        valueColumn.setHeader(_constants.value());
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

        final Button add = new Button(_constants.newLabel());
        add.setId("new-metadatum");
        add.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
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

        final Button remove = new Button(_constants.remove());
        remove.setId("remove-metadatum");
        remove.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                ((EditorGrid<ModelData>) _grid).stopEditing();
                for (final ModelData item
                        : _grid.getSelectionModel().getSelectedItems()) {
                    _dataStore.remove(item);
                }
            }
        });
        toolBar.add(remove);

        toolBar.add(new SeparatorToolItem());
        setBottomComponent(toolBar);
    }

    /**
     * Accessor for current metadata.
     *
     * @return Map of current metadata.
     */
    public Map<String, String> currentMetadata() {
        final Map<String, String> metadata = new HashMap<String, String>();

        for (final ModelData item : _dataStore.getModels()) {
            metadata.put(item.<String>get("key"), item.<String>get("value"));
        }
        return metadata;
    }

    /**
     * Resize grid.
     *
     * @param be Event of resizing.
     */
    public void handleResize(final BoxComponentEvent be) {
        final int newHeight =
            be.getHeight() - (Globals.DEFAULT_HEIGHT - GRID_HEIGHT);
        if (newHeight > (Globals.DEFAULT_HEIGHT - GRID_HEIGHT)) {
            _grid.setHeight(height);
        }
    }

    /**
     * Factory method for metadata validators.
     *
     * @return A new instance of the metaDataValues validator.
     */
    public Validator validateMetadataValues() {
        final Map<String, String> data = currentMetadata();
        return new Validator() {
            public void validate(final Validate validate) {
                final StringBuilder sb = new StringBuilder();
                for (final Map.Entry<String, String> datum : data.entrySet()) {
                    if (null==datum.getKey()
                        || datum.getKey().trim().length() < 1) {
                        sb.append(_constants.noEmptyKeysAllowed());
                    }
                    if (null==datum.getValue()
                        || datum.getValue().trim().length() < 1) {
                        sb.append(_constants.noEmptyValuesAllowed());
                    }
                    if (!datum.getKey().matches("[^<^>]*")) {
                        sb.append(
                            _constants.keysMustNotContainBrackets());
                    }
                    if (!datum.getValue().matches("[^<^>]*")) {
                        sb.append(
                            _constants.valuesMustNotContainBrackets());
                    }
                }
                if (sb.length() > 0) {
                    validate.addMessage(sb.toString());
                }
                validate.next();
            }
        };
    }
}
