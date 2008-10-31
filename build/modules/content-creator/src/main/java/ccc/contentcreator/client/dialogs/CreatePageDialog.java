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
import ccc.contentcreator.callbacks.DisposingCallback;
import ccc.contentcreator.client.DefinitionPanel;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.TemplateDTO;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageDialog
    extends
        AbstractWizardDialog {

    private final FormPanel _first = new FormPanel();
    private final FormPanel _second = new FormPanel();

    private ListStore<TemplateDTO> _templatesStore =
        new ListStore<TemplateDTO>();
    private Grid<TemplateDTO> _grid;

    private final TextField<String> _title = new TextField<String>();
    private final TextField<String> _name = new TextField<String>();

    private final FolderDTO _parent;

    private final ResourceServiceAsync _resourceService =
        Globals.resourceService();

    private DefinitionPanel _dp = new DefinitionPanel();
    private ContentPanel _upperPanel;

    /**
     * Constructor.
     *
     * @param list List of templates.
     * @param parent The Folder in which page will created.
     */
    public CreatePageDialog(final List<TemplateDTO> list,
                            final FolderDTO parent) {
        super(Globals.uiConstants().createPage());
        _parent = parent;

        setWidth(Globals.DEFAULT_WIDTH);
        setHeight(Globals.DEFAULT_HEIGHT);

        setHeading(Globals.uiConstants().createPage());

        final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        final ColumnConfig nameColumn = new ColumnConfig();
        nameColumn.setId("name");
        nameColumn.setHeader("Name");
        nameColumn.setWidth(200);
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
                _dp = new DefinitionPanel();
                _dp.renderFields(template.getDefinition());
                _second.removeAll(); // in order to avoid zombie field labels
                _second.add(_upperPanel);
                _second.add(_dp);

            }
        };
        _grid.addListener(Events.RowClick, listener);

        // add right side description
        // TODO validation

        _templatesStore.add(list);
        _first.add(_grid);
        _first.setLayout(new FitLayout());
        addCard(_first);


        _upperPanel = new ContentPanel();
        _upperPanel.setWidth("100%");
        _upperPanel.setBorders(false);
        _upperPanel.setBodyBorder(false);
        _upperPanel.setHeaderVisible(false);
        _upperPanel.setLayout(new FormLayout());

        _name.setFieldLabel(_constants.name());
        _name.setAllowBlank(false);
        _name.setId(_constants.name());
        _upperPanel.add(_name, new FormData("90%"));

        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);
        _title.setId(_constants.title());
        _upperPanel.add(_title, new FormData("90%"));

        _second.add(_upperPanel);
        _second.add(_dp);
        _second.setLayout(new RowLayout());
        addCard(_second);
        refresh();
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final Map<String, String> paragraphs =
                    new HashMap<String, String>();

                final List<Component> definitions =_dp.getItems();
                for (final Component c : definitions) {
                    if (c.getClass().equals(TextField.class)) {
                       final TextField<String> f = (TextField<String>) c;
                       paragraphs.put(f.getId(), f.getValue());
                    } else if (c.getClass().equals(TextArea.class)) {
                        final TextArea f = (TextArea) c;
                        paragraphs.put(f.getId(), f.getValue());
                     }
                    // FIXME date handling - requires PageDTO change?
                }

                final PageDTO page = new PageDTO(
                    null,
                    -1,
                    _name.getValue(),
                    _title.getValue(),
                    paragraphs);

                final TemplateDTO template =
                    _grid.getSelectionModel().getSelectedItem();

                _resourceService.createPage(_parent, page,
                    template,
                    new DisposingCallback(CreatePageDialog.this));
            }
        };
    }
}
