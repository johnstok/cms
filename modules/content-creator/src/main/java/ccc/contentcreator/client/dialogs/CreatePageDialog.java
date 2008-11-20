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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.EditPagePanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;
import ccc.contentcreator.client.Validate;
import ccc.contentcreator.client.Validations;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ParagraphDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageDialog
    extends
        AbstractWizardDialog {

    private final UIConstants _uiConstants = GWT.create(UIConstants.class);

    private final ContentPanel _first = new ContentPanel();
    private final EditPagePanel _second = new EditPagePanel();

    private ListStore<TemplateDTO> _templatesStore =
        new ListStore<TemplateDTO>();
    private Grid<TemplateDTO> _grid;

    private ContentPanel _descriptionPanel = new ContentPanel(new RowLayout());
    private ContentPanel _rightPanel = new ContentPanel(new RowLayout());

    private final ResourceTable _rt;
    private final FolderDTO _parent;

    private final ResourceServiceAsync _resourceService =
        Globals.resourceService();



    private Text _description = new Text("");

    /**
     * Constructor.
     *
     * @param list List of templates.
     * @param parent The Folder in which page will created.
     * @param rt ResourceTable to update.
     */
    public CreatePageDialog(final List<TemplateDTO> list,
                            final FolderDTO parent, final ResourceTable rt) {
        super(Globals.uiConstants().createPage());
        _rt = rt;
        _parent = parent;

        setWidth(Globals.DEFAULT_WIDTH);
        setHeight(Globals.DEFAULT_HEIGHT);
        setHeading(Globals.uiConstants().createPage());

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig nameColumn = new ColumnConfig();
        nameColumn.setId(_uiConstants.name());
        nameColumn.setHeader(_uiConstants.name());
        nameColumn.setWidth(200);
        nameColumn.setRenderer(createIdRenderer());

        configs.add(nameColumn);

        final ColumnModel cm = new ColumnModel(configs);

        _grid = new Grid<TemplateDTO>(_templatesStore, cm);
        _grid.setLoadMask(true);
        _grid.setId("TemplateGrid");

        final Listener<GridEvent> listener =
            new Listener<GridEvent>() {
            public void handleEvent(final GridEvent ge) {
                TemplateDTO template =
                    (TemplateDTO) ge.grid.getSelectionModel().getSelectedItem();
                _second.createFields(template.getDefinition());
                _description.setText(template.getDescription());
            }
        };
        _grid.addListener(Events.RowClick, listener);

        _templatesStore.add(list);

        final BorderLayoutData westData =
            new BorderLayoutData(LayoutRegion.WEST, 205);
        westData.setMargins(new Margins(5));

        final BorderLayoutData centerData =
            new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5));

        _first.setLayout(new BorderLayout());

        _rightPanel.setHeaderVisible(true);
        _rightPanel.setHeading(_uiConstants.template());

        _rightPanel.add(createCheckbox());
        _rightPanel.add(_grid);

        _first.add(_rightPanel, westData);

        _descriptionPanel.setHeaderVisible(true);
        _descriptionPanel.setHeading(_uiConstants.description());
        _descriptionPanel.add(_description);

        _first.add(_descriptionPanel, centerData);
        _first.setBorders(false);
        _first.setBodyBorder(false);
        _first.setHeaderVisible(false);
        addCard(_first);

        addCard(_second);
        refresh();
    }

    private CheckBox createCheckbox() {

        final CheckBox cb = new CheckBox();
        cb.setBoxLabel(_uiConstants.useDefaultTemplate());
        cb.setId(_uiConstants.useDefaultTemplate());
        _resourceService.getTemplateForResource(_parent,
            new ErrorReportingCallback<TemplateDTO>() {
            public void onSuccess(final TemplateDTO result) {
                if (result == null) {
                    cb.setValue(false);
                    cb.disable();
                    _grid.enable();
                    _description.setText("");
                } else {
                    cb.setValue(true);
                    _grid.disable();
                    _grid.getSelectionModel().deselectAll();
                    _second.createFields(result.getDefinition());
                    _description.setText(result.getDescription());
                }
            }
        });

        cb.addListener(Events.Change, new Listener<FieldEvent>() {
            public void handleEvent(final FieldEvent be) {
                if (cb.getValue()) {
                    _resourceService.getTemplateForResource(_parent,
                        new ErrorReportingCallback<TemplateDTO>() {
                        public void onSuccess(final TemplateDTO result) {
                            if (result == null) {
                                cb.disable();
                                _grid.enable();
                                _description.setText("");
                            } else {
                                _grid.disable();
                                _grid.getSelectionModel().deselectAll();
                                _second.createFields(result.getDefinition());
                                _description.setText(result.getDescription());
                            }
                        }
                    });
                } else {
                    _second.removeAll();
                    _grid.enable();
                    _description.setText("");
                }
            }
        });
        return cb;
    }

    private GridCellRenderer<TemplateDTO> createIdRenderer() {

        return new GridCellRenderer<TemplateDTO>() {
            public String render(final TemplateDTO model,
                                 final String property,
                                 final ColumnData config,
                                 final int rowIndex,
                                 final int colIndex,
                                 final ListStore<TemplateDTO> store) {

                String value = "";

                if (null != model) {
                final StringBuilder html = new StringBuilder();
                html.append("<div id='");
                html.append(model.getName());
                html.append("'>");
                html.append(model.getName());
                html.append("</div>");
                value = html.toString();
                }
                return value;
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                Validate.callTo(createPage())
                    .check(Validations.notEmpty(_second.name()))
                    .check(Validations.notValidResourceName(_second.name()))
                    .check(Validations.notEmpty(_second.title()))
                    .stopIfInError()
                    .check(Validations.uniqueResourceName(
                        _parent, _second.name()))
                    .callMethodOr(Validations.reportErrors());
            }
        };
    }

    private Runnable createPage() {
        return new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {

                final Map<String, ParagraphDTO> paragraphs =
                    new HashMap<String, ParagraphDTO>();

                final List<Component> definitions =_second.definitionItems();
                for (final Component c : definitions) {
                    if ("TEXT".equals(c.getData("type"))) {
                        final Field<String> f = (Field<String>) c;
                        paragraphs.put(c.getId(),
                            new ParagraphDTO("TEXT", f.getValue()));
                    } else if ("DATE".equals(c.getData("type"))) {
                        final DateField f = (DateField) c;
                        paragraphs.put(c.getId(),
                            new ParagraphDTO("DATE",
                                ""+f.getValue().getTime()));
                    }
                }

                final PageDTO page = new PageDTO(
                    null,
                    -1,
                    _second.name().getValue(),
                    _second.title().getValue(),
                    paragraphs,
                    "");

                final TemplateDTO template =
                    _grid.getSelectionModel().getSelectedItem();

                _resourceService.createPage(_parent, page,
                    template,
                    new ErrorReportingCallback<Void>() {
                        public void onSuccess(final Void result) {
                            _rt.refreshTable();
                            close();
                        }
                    }
                );
            }
        };
    }
}
